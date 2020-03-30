package io.eventuate.examples.tram.ordersandcustomers.commonswagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class CommonSwaggerConfiguration {

  @Bean
  public Docket api(TypeResolver typeResolver) {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("io.eventuate.examples.tram.ordersandcustomers"))
            .build()
            .pathMapping("/")
            .genericModelSubstitutes(ResponseEntity.class)
            .useDefaultResponseMessages(false);
  }

  @Bean
  public LinkDiscoverers discovers() {
    List<LinkDiscoverer> plugins = new ArrayList<>();
    plugins.add(new CollectionJsonLinkDiscoverer());
    return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
  }
}
