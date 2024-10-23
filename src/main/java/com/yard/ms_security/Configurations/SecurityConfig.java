package com.yard.ms_security.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers("/api/public/security/login", "/api/public/security/login/google", "/api/public/security/recovery/{email}", "api/public/security/reset-password").permitAll();
                            request.anyRequest().authenticated(); // Asegúrate de que otras rutas requieran autenticación
                        })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/home", true) // Redirige a /home tras un login exitoso
                        .failureUrl("/login?error=true")) // Redirige a /login si falla el login

                .build();
    }
}
