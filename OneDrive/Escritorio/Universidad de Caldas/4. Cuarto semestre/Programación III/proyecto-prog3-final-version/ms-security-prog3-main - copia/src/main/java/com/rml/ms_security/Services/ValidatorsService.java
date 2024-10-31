package com.rml.ms_security.Services;

import com.rml.ms_security.Models.*;
import com.rml.ms_security.Repositories.PermissionRepository;
import com.rml.ms_security.Repositories.RolePermissionRepository;
import com.rml.ms_security.Repositories.UserRepository;
import com.rml.ms_security.Repositories.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marca esta clase como un servicio de Spring, que puede ser inyectado en otros componentes
public class ValidatorsService {

    @Autowired
    private JwtService jwtService; // Servicio para manejar la autenticación y validación de tokens JWT

    @Autowired
    private PermissionRepository thePermissionRepository; // Repositorio para manejar permisos
    @Autowired
    private UserRepository theUserRepository; // Repositorio para manejar usuarios
    @Autowired
    private RolePermissionRepository theRolePermissionRepository; // Repositorio para manejar la relación entre roles y permisos
    @Autowired
    private UserRoleRepository theUserRoleRepository; // Repositorio para manejar la relación entre usuarios y roles

    private static final String BEARER_PREFIX = "Bearer "; // Prefijo para los tokens Bearer

    // Método para validar los roles y permisos del usuario basado en la solicitud HTTP
    public boolean validationRolePermission(HttpServletRequest request, // Hace el análisis para determinar si se permite el acceso, devuelve true o false
                                            String url,
                                            String method) {
        boolean success = false; // Inicializa la variable de éxito como falsa
        User theUser = this.getUser(request); // Obtiene el usuario desde la solicitud HTTP

        if (theUser != null) { // Si el usuario existe
            System.out.println("Antes URL " + url + " metodo " + method);
            // Reemplaza cualquier ID en la URL con un comodín
            url = url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
            System.out.println("URL " + url + " metodo " + method);
            Permission thePermission = this.thePermissionRepository.getPermission(url, method); // Obtiene el permiso para la URL y método

            // Obtiene la lista de roles del usuario
            List<UserRole> roles = this.theUserRoleRepository.getRolesByUser(theUser.get_id());
            int i = 0;

            // Analiza cada rol para verificar los permisos
            while (i < roles.size() && !success) { // Continua mientras haya roles y no se haya encontrado un permiso
                UserRole actual = roles.get(i);
                Role theRole = actual.getRole(); // Obtiene el rol actual

                // Verifica que ambos, rol y permiso, existan
                if (theRole != null && thePermission != null) {
                    System.out.println("Rol " + theRole.get_id() + " Permission " + thePermission.get_id());
                    // Obtiene la relación entre el rol y el permiso
                    RolePermission theRolePermission = this.theRolePermissionRepository.getRolePermission(theRole.get_id(), thePermission.get_id());
                    if (theRolePermission != null) { // Si se encontró la relación, se permite el acceso
                        success = true;
                    }
                } else {
                    success = false; // Si alguno es nulo, no se permite el acceso
                }
                i += 1; // Avanza al siguiente rol
            }
        }
        return success; // Retorna el resultado de la validación
    }

    // Método para obtener el usuario de la solicitud HTTP a partir del token
    public User getUser(final HttpServletRequest request) {
        User theUser = null; // Inicializa el usuario como nulo
        String authorizationHeader = request.getHeader("Authorization"); // Obtiene el header de autorización
        System.out.println("Header " + authorizationHeader);

        // Verifica que el header no sea nulo y comience con el prefijo Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length()); // Extrae el token
            System.out.println("Bearer Token: " + token);
            User theUserFromToken = jwtService.getUserFromToken(token); // Obtiene el usuario desde el token

            // Si el usuario es válido, busca el usuario en la base de datos
            if (theUserFromToken != null) {
                theUser = this.theUserRepository.findById(theUserFromToken.get_id()) // Busca al usuario por su ID
                        .orElse(null); // Devuelve nulo si no se encuentra
            }
        }
        return theUser; // Retorna el usuario encontrado o nulo
    }
}
