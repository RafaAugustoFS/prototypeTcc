package com.onAcademy.tcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    /**
     * Swagger é uma ferramenta que gera documentação interativa para APIs REST.
     * Ele permite visualizar e testar os endpoints da API diretamente no navegador,
     * facilitando o entendimento e o uso da API por desenvolvedores e consumidores.
     */

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("OnAcademy Api")
                .description("Aplicação para TCC - Sistema de gerenciamento educacional")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Equipe OnAcademy")
                    .email("onacademy.tcc@gmail.com")
                    .url("")
                )
            );
    }
}