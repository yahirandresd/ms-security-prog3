package com.rml.ms_security.Interceptors;

import com.rml.ms_security.Services.ValidatorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private ValidatorsService validatorService; //El tombo
    @Override

    public boolean preHandle(HttpServletRequest request, // Validación de permisos antes de que la solicitud sea procesada
                             HttpServletResponse response,
                             Object handler)
            throws Exception { //Si es true o false, determina si puede entra o no.
        boolean success=this.validatorService.validationRolePermission(request,request.getRequestURI(),request.getMethod()); //De aca se puede sacar la url y el metodo que esta pidiendo, tambien viene el token
        return success;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, //Lógica a ejecutar después de procesar la solicitud, antes de renderizar la vista
                           ModelAndView modelAndView) throws Exception {
        // Lógica a ejecutar después de que se haya manejado la solicitud por el controlador
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, //Tareas finales después de que todo el procesamiento haya concluido
                                Exception ex) throws Exception {
        // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de la vista
    }
}
