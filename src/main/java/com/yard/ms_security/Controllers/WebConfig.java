package com.yard.ms_security.Configurations;

import com.yard.ms_security.Interceptors.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //Establece parametros para saber que es una configuración
public class WebConfig implements WebMvcConfigurer { // Me ayuda a levantar el muro para verificar
    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor) //Este es el muro
                .addPathPatterns("/api/**") //Protege lo que sea asi
                .excludePathPatterns("/api/public/**");// Excluye esto; esto no vamos a proteger
                ///.excludePathPatterns("/api/users/**");
    }
}