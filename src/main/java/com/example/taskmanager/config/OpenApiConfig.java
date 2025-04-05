package com.example.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("로컬 개발 서버");

        Contact contact = new Contact()
                .name("태스크 매니저 팀")
                .email("support@taskmanager.com")
                .url("https://www.taskmanager.com");

        License mitLicense = new License()
                .name("MIT 라이센스")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("태스크 매니저 API")
                .version("1.0.0")
                .description("태스크 매니저 애플리케이션을 위한 RESTful API")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
