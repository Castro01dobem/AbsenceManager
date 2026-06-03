package com.itb.inf2dm.absencemanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // 👉 http://localhost:8080/swagger-ui.html

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AbsenceManager API")
                        .description("API para gerenciamento de faltas e assiduidade")
                        .version("1.0.0"));
    }
}
