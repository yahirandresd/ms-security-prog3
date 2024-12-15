package com.rml.ms_security.Configurations;

import com.rml.ms_security.Interceptors.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Indico que esta clase es una configuración de Spring
public class WebConfig implements WebMvcConfigurer { // Implemento WebMvcConfigurer para personalizar el manejo de las solicitudes web

    @Autowired
    private SecurityInterceptor securityInterceptor; // Inyecto el interceptor que controlará el acceso a los endpoints

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*registry.addInterceptor(securityInterceptor) // Añado el interceptor para manejar la seguridad de las rutas.addPathPatterns("/api/**") // Todas las rutas que comiencen con /api estarán protegidas
               .excludePathPatterns("/api/public/**"); // Excluyo las rutas públicas, permitiendo acceso sin autenticación
//        ///.excludePathPatterns("/api/users/**"); // Puedo añadir más rutas aquí si quiero que también sean públicas*/
    }
}
