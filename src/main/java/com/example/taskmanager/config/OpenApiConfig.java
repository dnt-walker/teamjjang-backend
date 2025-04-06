package com.example.taskmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

//    @Bean
//    public OpenAPI openAPI() {
//        Server localServer = new Server()
//                .url("http://localhost:8080")
//                .description("로컬 개발 서버");
//
//        Contact contact = new Contact()
//                .name("태스크 매니저 팀")
//                .email("support@taskmanager.com")
//                .url("https://www.taskmanager.com");
//
//        License mitLicense = new License()
//                .name("MIT 라이센스")
//                .url("https://opensource.org/licenses/MIT");
//
//        Info info = new Info()
//                .title("태스크 매니저 API")
//                .version("1.0.0")
//                .description("태스크 매니저 애플리케이션을 위한 RESTful API")
//                .contact(contact)
//                .license(mitLicense);
//
//        return new OpenAPI()
//                .info(info)
//                .servers(List.of(localServer));
//    }

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//                .components(new Components()
//                        .addSecuritySchemes("bearerAuth",
//                                new SecurityScheme()
//                                        .name("Authorization")
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                        )
//                );
//    }
//

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("로컬 개발 서버");
        License mitLicense = new License()
                .name("MIT 라이센스")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("태스크 매니저 API")
                .version("1.0.0")
                .description("태스크 매니저 애플리케이션을 위한 RESTful API")
//                .contact(contact)
                .license(mitLicense);
        return new OpenAPI()
                .servers(List.of(localServer))
//                .info(new Info().title("Task Manager API").version("v1"))
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

}
