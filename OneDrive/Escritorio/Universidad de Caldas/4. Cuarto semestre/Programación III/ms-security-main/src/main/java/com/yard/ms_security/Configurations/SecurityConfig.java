package com.yard.ms_security.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()) // Cualquier otra solicitud debe estar autenticada
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/home", true) // Redirige a /home tras un login exitoso
                        .failureUrl("/login?error=true")); // Redirige a /login si falla el login
                // Deshabilita CSRF (útil si estás haciendo API REST sin formularios)

        return http.build();
    }
}
