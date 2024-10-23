package com.yard.ms_security.Services;


import com.yard.ms_security.Models.*;
import com.yard.ms_security.Repositories.PermissionRepository;
import com.yard.ms_security.Repositories.RolePermissionRepository;
import com.yard.ms_security.Repositories.UserRepository;
import com.yard.ms_security.Repositories.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidatorsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private RolePermissionRepository theRolePermissionRepository;

    @Autowired
    private UserRoleRepository theUserRoleRepository;

    private static final String BEARER_PREFIX = "Bearer ";
    public boolean validationRolePermission(HttpServletRequest request, //Hace el analisis para saber si deja entrar, devuelve true o false
                                            String url,
                                            String method){
        boolean success=false;
        User theUser=this.getUser(request);//Trae el usuario que se formo en getUser
        if(theUser!=null){ // Si el usuario si existe hacer:
            System.out.println("Antes URL "+url+" metodo "+method);
            url = url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");//Obtenemos la Url y el metodo
            System.out.println("URL "+url+" metodo "+method);
            Permission thePermission=this.thePermissionRepository.getPermission(url,method); //Obtengo el permiso

            List<UserRole> roles=this.theUserRoleRepository.getRolesByUser(theUser.get_id()); //Obtenemos la lista de Roles del Usuario, preguntamos que roles tiene es usuario
            int i=0;
            while(i<roles.size() && success==false){//Analizo por cada uno de los roles si tiene un intermedio para saber que permisos tiene ese Usuario. El succes es si con un solo permiso que obtenga ya lo deja pasar, siempre y cuando tenga un rol
                UserRole actual=roles.get(i);
                Role theRole=actual.getRole(); //Aquí ya tengo el Rol y si tengo ese ya tengo el permiso
                if(theRole!=null && thePermission!=null){//Valido si existe una relacion entre ese rol y el permiso, si existe un rol y existe un permiso
                    System.out.println("Rol "+theRole.get_id()+ " Permission "+thePermission.get_id());
                    RolePermission theRolePermission=this.theRolePermissionRepository.getRolePermission(theRole.get_id(),thePermission.get_id());//Necesito que me obtenga un RolePermission, conociendo el id del Role y del Permission
                    if (theRolePermission!=null){//Si es diferente de Null pues si se encontro el Permiso de ese Rol
                        success=true;
                    }
                }else{
                    success=false;
                }
                i+=1;
            }

        }
        return success;
    }
    public User getUser(final HttpServletRequest request) {
        User theUser=null;
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Header "+authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            System.out.println("Bearer Token: " + token);
            User theUserFromToken=jwtService.getUserFromToken(token);
            if(theUserFromToken!=null) {
                theUser= this.theUserRepository.findById(theUserFromToken.get_id())//Busco al usuario por el ID que me deolvio al descodificar el token
                        .orElse(null);

            }
        }
        return theUser;
    }
}
