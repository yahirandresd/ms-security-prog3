package com.rml.ms_security.Configurations;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // Permitirá realizar peticiones HTTP
    // hacia otros servicios o APIs externas desde mi app.
    // Utilizo RestTemplateBuilder para tener más flexibilidad si en algún momento
    // necesito configurarlo con características adicionales.
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Retorno una instancia simple de RestTemplate
        return new RestTemplate();
    }
}
