# SAGA - Passo a Passo

Este guia demonstra o fluxo de uma SAGA no exemplo "Customers and Orders" (Clientes e Pedidos) utilizando o padrão SAGA do Eventuate Tram.

---

## Passo 1: Ponto de Partida

**Arquivo:** [OrderService.java:28](order-service/order-service-domain/src/main/java/io/eventuate/examples/tram/ordersandcustomers/orders/domain/OrderService.java#L28)

No `OrderService` (Serviço de Pedidos), o método `createOrder()` (criar pedido) inicia a SAGA publicando um evento.

```java
// Exemplo conceitual
@Transactional
public void createOrder(Order order) {
    // Salva o pedido no banco de dados
    orderRepository.save(order);
    
    // Publica o evento que inicia a SAGA
    domainEventPublisher.publish("Order", order.getId(), 
        Collections.singletonList(new OrderCreatedEvent(order)));
}
```

**O que acontece:**
- O pedido é criado com status `PENDENTE`
- Um evento `OrderCreatedEvent` (Evento de Pedido Criado) é publicado no Kafka
- Este evento dispara a próxima etapa da SAGA no `Customer Service`

---

## Passo 2: Configuração do Handler (Manipulador) de Eventos de Pedido

**Arquivo:** [OrderEventConsumer.java:23](customer-service/customer-service-event-handling/src/main/java/io/eventuate/examples/tram/ordersandcustomers/customers/eventhandlers/OrderEventConsumer.java#L23)

No `Customer Service` (Serviço de Clientes), o handler (manipulador) do evento `OrderCreatedEvent` é configurado.

```java
// Exemplo conceitual
@Configuration
public class OrderEventConsumerConfiguration {
    
    @Bean
    public DomainEventDispatcher orderEventDispatcher(
            DomainEventDispatcherFactory dispatcherFactory,
            OrderEventConsumer orderEventConsumer) {
        
        return dispatcherFactory.make(
            "orderEventConsumerId",
            orderEventConsumer.domainEventHandlers()
        );
    }
}
```

**O que acontece:**
- O `DomainEventDispatcher` (Despachador de Eventos de Domínio) é configurado para ouvir eventos de pedidos
- Quando um `OrderCreatedEvent` chega, o handler correspondente é acionado

---

## Passo 3: Implementação do Handler de Eventos de Pedido

**Arquivo:** [OrderEventConsumer.java:28](customer-service/customer-service-event-handling/src/main/java/io/eventuate/examples/tram/ordersandcustomers/customers/eventhandlers/OrderEventConsumer.java#L28)

Aqui está a implementação do handler do evento `OrderCreatedEvent` no `Customer Service`.

```java
// Exemplo conceitual
public class OrderEventConsumer {
    
    @Autowired
    private CustomerService customerService;
    
    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
            .forAggregateType("Order")
            .onEvent(OrderCreatedEvent.class, this::handleOrderCreated)
            .build();
    }
    
    public void handleOrderCreated(DomainEventEnvelope<OrderCreatedEvent> event) {
        OrderCreatedEvent orderCreatedEvent = event.getEvent();
        
        // Chama o serviço de clientes para reservar crédito
        customerService.reserveCredit(
            orderCreatedEvent.getCustomerId(),
            orderCreatedEvent.getOrderTotal()
        );
    }
}
```

**O que acontece:**
- O evento `OrderCreatedEvent` é recebido
- O método `handleOrderCreated()` (manipular pedido criado) é acionado
- O `Customer Service` é chamado para tentar reservar o crédito do cliente

---

## Passo 4: Evento de Crédito Reservado do Cliente

**Arquivo:** [CustomerService.java:58](customer-service/customer-service-domain/src/main/java/io/eventuate/examples/tram/ordersandcustomers/customers/domain/CustomerService.java#L58)

No caminho feliz (happy path - quando tudo funciona corretamente), o `Customer Service` publica um evento `CustomerCreditReservedEvent` (Evento de Crédito do Cliente Reservado).

```java
// Exemplo conceitual
@Transactional
public void reserveCredit(String customerId, BigDecimal amount) {
    Customer customer = customerRepository.findById(customerId);
    
    // Verifica se o cliente tem crédito suficiente
    if (customer.getAvailableCredit().compareTo(amount) >= 0) {
        // Deduz o crédito disponível
        customer.setAvailableCredit(
            customer.getAvailableCredit().subtract(amount)
        );
        
        customerRepository.save(customer);
        
        // Publica o evento de sucesso
        domainEventPublisher.publish("Customer", customerId,
            Collections.singletonList(
                new CustomerCreditReservedEvent(customerId, amount)
            )
        );
    } else {
        // Se não houver crédito, publica evento de falha
        domainEventPublisher.publish("Customer", customerId,
            Collections.singletonList(
                new CustomerCreditLimitExceededEvent(customerId, amount)
            )
        );
    }
}
```

**O que acontece:**
- O cliente é consultado no banco de dados
- Verifica-se se há crédito disponível
- Se sim: o crédito é reservado e o evento `CustomerCreditReservedEvent` é publicado
- Se não: o evento `CustomerCreditLimitExceededEvent` (Limite de Crédito Excedido) é publicado, iniciando a compensação

---

## Passo 5: Configuração do Handler de Eventos de Cliente

**Arquivo:** [CustomerEventConsumer.java:20](order-service/order-service-event-handling/src/main/java/io/eventuate/examples/tram/ordersandcustomers/orders/eventhandlers/CustomerEventConsumer.java#L20)

No `Order Service`, o handler do evento `CustomerCreditReservedEvent` é configurado.

```java
// Exemplo conceitual
@Configuration
public class CustomerEventConsumerConfiguration {
    
    @Bean
    public DomainEventDispatcher customerEventDispatcher(
            DomainEventDispatcherFactory dispatcherFactory,
            CustomerEventConsumer customerEventConsumer) {
        
        return dispatcherFactory.make(
            "customerEventConsumerId",
            customerEventConsumer.domainEventHandlers()
        );
    }
}
```

**O que acontece:**
- O `DomainEventDispatcher` é configurado para ouvir eventos de clientes
- Quando um `CustomerCreditReservedEvent` chega, o handler correspondente é acionado

---

## Passo 6: Implementação do Handler de Eventos de Cliente

**Arquivo:** [CustomerEventConsumer.java:26](order-service/order-service-event-handling/src/main/java/io/eventuate/examples/tram/ordersandcustomers/orders/eventhandlers/CustomerEventConsumer.java#L26)

Aqui está a implementação do handler do evento `CustomerCreditReservedEvent` no `Order Service`, que chama o `OrderService` para aprovar o pedido.

```java
// Exemplo conceitual
public class CustomerEventConsumer {
    
    @Autowired
    private OrderService orderService;
    
    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
            .forAggregateType("Customer")
            .onEvent(CustomerCreditReservedEvent.class, 
                this::handleCustomerCreditReserved)
            .onEvent(CustomerCreditLimitExceededEvent.class,
                this::handleCustomerCreditLimitExceeded)
            .build();
    }
    
    public void handleCustomerCreditReserved(
            DomainEventEnvelope<CustomerCreditReservedEvent> event) {
        
        CustomerCreditReservedEvent creditReservedEvent = event.getEvent();
        
        // Aprova o pedido
        orderService.approveOrder(creditReservedEvent.getOrderId());
    }
    
    public void handleCustomerCreditLimitExceeded(
            DomainEventEnvelope<CustomerCreditLimitExceededEvent> event) {
        
        CustomerCreditLimitExceededEvent creditLimitEvent = event.getEvent();
        
        // Rejeita o pedido (compensação)
        orderService.rejectOrder(creditLimitEvent.getOrderId());
    }
}
```

**O que acontece:**
- O evento `CustomerCreditReservedEvent` é recebido
- O método `handleCustomerCreditReserved()` (manipular crédito de cliente reservado) é acionado
- O `OrderService` é chamado para aprovar o pedido, alterando seu status para `APROVADO`
- Se o crédito não foi suficiente, o método `handleCustomerCreditLimitExceeded()` rejeita o pedido

---

## Fluxo Completo da SAGA

```
┌─────────────────────────────────────────────────────────────────────┐
│                    FLUXO COMPLETO DA SAGA                           │
└─────────────────────────────────────────────────────────────────────┘

1. ORDER SERVICE (Serviço de Pedidos)
   ├─ Cria pedido com status PENDENTE
   └─ Publica: OrderCreatedEvent
                    │
                    ▼
2. CUSTOMER SERVICE (Serviço de Clientes) recebe OrderCreatedEvent
   ├─ Valida crédito disponível do cliente
   ├─ Se OK: Reserva crédito
   │         Publica: CustomerCreditReservedEvent
   │                    │
   │                    ▼
   │         3. ORDER SERVICE recebe CustomerCreditReservedEvent
   │            ├─ Aprova o pedido (status = APROVADO)
   │            └─ SAGA CONCLUÍDA COM SUCESSO ✓
   │
   └─ Se FALHA: Não reserva crédito
               Publica: CustomerCreditLimitExceededEvent
                           │
                           ▼
               3. ORDER SERVICE recebe CustomerCreditLimitExceededEvent
                  ├─ Rejeita o pedido (status = REJEITADO)
                  └─ SAGA CONCLUÍDA COM FALHA ✗
```

---

## Resumo das Transições de Estado

| Etapa | Serviço | Ação | Evento Publicado |
| :--- | :--- | :--- | :--- |
| 1 | Order Service | Cria pedido (PENDENTE) | `OrderCreatedEvent` |
| 2 | Customer Service | Valida e reserva crédito | `CustomerCreditReservedEvent` ou `CustomerCreditLimitExceededEvent` |
| 3 | Order Service | Aprova ou rejeita pedido | (Fim da SAGA) |

---

## Conceitos-Chave

- **Event (Evento):** Mensagem que representa algo que aconteceu no sistema (ex: um pedido foi criado)
- **Handler (Manipulador):** Função que reage a um evento específico
- **Dispatcher (Despachador):** Componente que roteia eventos para os handlers corretos
- **Transactional (Transacional):** Garante que a operação é atômica (tudo ou nada)
- **Happy Path (Caminho Feliz):** Cenário onde tudo funciona conforme esperado
- **Compensating Transaction (Transação de Compensação):** Ação que desfaz uma transação anterior em caso de falha

---

## Próximos Passos

1. Estudar como implementar **transações de compensação** para cenários de falha mais complexos
2. Entender o papel do **CDC (Change Data Capture)** na garantia de entrega de eventos
3. Configurar **retry policies** (políticas de retentativa) para eventos que falharam
4. Implementar **idempotência** para garantir que eventos processados múltiplas vezes não causem problemas
