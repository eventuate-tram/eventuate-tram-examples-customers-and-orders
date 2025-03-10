package io.eventuate.examples.tram.customersandorders.application;

import io.eventuate.examples.tram.customersandorders.endtoendtests.ApplicationUnderTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(excludeName = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
@Controller
public class CustomersAndOrdersMain {


    private static final ApplicationUnderTest application =ApplicationUnderTest.make(); //


    @RequestMapping(path = "/")
    public String index(Model model) {

        int apigatewayPort = application.getApigatewayPort();
        int customerServicePort = application.getCustomerServicePort();
        int orderServicePort = application.getOrderServicePort();
        int orderHistoryServicePort = application.getOrderHistoryServicePort();

        model.addAttribute("apiGatewayUrl", "http://localhost:" + apigatewayPort);
        model.addAttribute("customerServiceUrl", "http://localhost:" + customerServicePort);
        model.addAttribute("orderServiceUrl", "http://localhost:" + orderServicePort);
        model.addAttribute("orderHistoryServiceUrl", "http://localhost:" + orderHistoryServicePort);

        return "index";

    }

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {

        System.out.printf("""
                
                
                Visit http://localhost:%s/ for more information
                
                
                """, webServerAppCtxt.getWebServer().getPort());
    }

    public static void main(String[] args) {

        application.start();

        SpringApplication.run(CustomersAndOrdersMain.class, args);
    }

}
