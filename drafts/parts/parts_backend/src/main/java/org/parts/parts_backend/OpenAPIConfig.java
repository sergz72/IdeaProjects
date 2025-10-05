package org.parts.parts_backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parts Backend API")
                        .version("1.0.0")
                        .description("API documentation for Parts Backend application")
                        .contact(new Contact()
                                .name("Serhii Zashchelkin")
                                .email("serg.zz@ukr.net")));
    }
}
