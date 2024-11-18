package com.rml.ms_security.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Defino el bean SecurityFilterChain que establece la configuración de seguridad para mi aplicación.
    // Aquí es donde especifico qué rutas estarán protegidas y cuáles no.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        request -> {
                            // Estas rutas estarán accesibles sin autenticación
                            request.requestMatchers("/api/public/security/login",
                                    "/api/public/security/login/google",
                                    "/api/public/security/verify2fa",
                                    "/api/public/security/recovery/{email}",
                                    "api/public/security/reset-password").permitAll();
                            // Cualquier otra ruta va a requerir que el usuario esté autenticado
                            request.anyRequest().permitAll();
                        })
                // Desactivo CSRF ya que no es necesario para la autenticación OAuth2 en esta configuración
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        // Redirijo al usuario a /home después de un login exitoso
                        .defaultSuccessUrl("/home", true)
                        // En caso de fallo de login, redirijo al usuario a la página de login con un parámetro de error
                        .failureUrl("/login?error=true"))

                // Finalmente, construyo la configuración con las reglas definidas
                .build();
    }
}
