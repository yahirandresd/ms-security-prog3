package com.rml.ms_security.Controllers;

import com.rml.ms_security.Models.EmailSent;
import com.rml.ms_security.Models.Permission;
import com.rml.ms_security.Models.User;
import com.rml.ms_security.Repositories.UserRepository;
import com.rml.ms_security.Services.EncryptionService;
import com.rml.ms_security.Services.RequestService;
import com.rml.ms_security.Services.JwtService;
import com.rml.ms_security.Services.ValidatorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;

@CrossOrigin // Permite peticiones desde diferentes orígenes (CORS)
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/public/security") // Define el prefijo de la URL para todos los métodos de este controlador
public class SecurityController {

    @Autowired
    private UserRepository theUserRepository; // Repositorio para manejar operaciones de usuario

    @Autowired
    private EncryptionService theEncryptionService; // Servicio para manejar la encriptación de contraseñas

    @Autowired
    private JwtService theJwtService; // Servicio para generar tokens JWT

    @Autowired
    private RequestService requestService; // Servicio para manejar las solicitudes de email

    private String confirmCode2fa; // Código de verificación para la autenticación de dos factores (2FA)

    @Autowired
    private ValidatorsService theValidatorsService;


    // Método para autenticar usuarios y manejar la recuperación de contraseñas
    /*@PostMapping("/login")
    public HashMap<String, Object> login(@RequestBody User theNewUser, final HttpServletResponse response) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        // Obtener el usuario real basado en el email proporcionado
        User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        // Verificar si la contraseña ingresada coincide con la almacenada (encriptada)
        if (theActualUser != null && theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
            // Generar código 2FA y enviarlo por correo electrónico
            int code2fa = requestService.codGenerator(); // Generar el código 2FA
            confirmCode2fa = String.valueOf(code2fa); // Almacenar el código para su posterior verificación
            // Preparar y enviar el correo electrónico con el código de seguridad
            EmailSent emailSent = new EmailSent(theActualUser.getEmail(), "NUEVO INICIO DE SESIÓN", "Código de seguridad: " + code2fa);
            requestService.sendEmail(emailSent);
            theResponse.put("message", "Email enviado"); // Informar que se envió el email
            return theResponse; // Retornar la respuesta
        } else {
            // Si la contraseña es incorrecta, enviar correo para recuperación de contraseña
            String recoveryUrl = "http://localhost:8081/api/public/security/recovery/" + theNewUser.getEmail(); // URL para recuperación
            String htmlContent = "<html>"
                    + "<body>"
                    + "<h2>Recuperación de contraseña</h2>"
                    + "<p>Has ingresado una contraseña incorrecta.</p>"
                    + "<p>Si deseas recuperar tu contraseña, haz clic en el siguiente botón:</p>"
                    + "<a href=\"" + recoveryUrl + "\" style=\""
                    + "display: inline-block;"
                    + "padding: 10px 20px;"
                    + "color: white;"
                    + "background-color: #007BFF;"
                    + "border: none;"
                    + "border-radius: 5px;"
                    + "text-decoration: none;"
                    + "\">Recuperar Contraseña</a>"
                    + "</body>"
                    + "</html>";

            // Enviar el correo electrónico para la recuperación de contraseña
            EmailSent emailSent = new EmailSent(theNewUser.getEmail(),
                    "Recuperación de contraseña",
                    htmlContent);
            requestService.sendEmail(emailSent);

            // Establecer el estado HTTP 401 (no autorizado)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // Incluir un mensaje en el cuerpo de la respuesta
            theResponse.put("message", "Contraseña incorrecta. Email de recuperación enviado.");
            return theResponse; // Retornar la respuesta con el mensaje
        }
    }*/

    @GetMapping("/login/google")
    public ResponseEntity<HashMap<String, Object>> loginWithGoogle() {
        HashMap<String, Object> theResponse = new HashMap<>();
        theResponse.put("message", "No autorizado. Debes autenticarte primero.");

        // Devuelve la respuesta con el código de estado 401 (Unauthorized)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(theResponse);
    }




    // Método para verificar el código 2FA ingresado por el usuario
    @PostMapping("/verify2fa")
    public HashMap<String, Object> verify2fa(@RequestBody User theNewUser,
                                             final HttpServletResponse response) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        // Obtener el usuario real basado en el email proporcionado
        User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        String code = theNewUser.getPassword(); // El código ingresado por el usuario se almacena como contraseña

        // Verificar si el código 2FA ingresado es correcto
        if (confirmCode2fa != null && confirmCode2fa.equals(code)) {
            String token = theJwtService.generateToken(theActualUser); // Generar el token JWT para el usuario
            theActualUser.setPassword("ENCRIPTADO POR EL IRL"); // Proteger la contraseña antes de enviarla
            theResponse.put("token", token); // Incluir el token en la respuesta
            theResponse.put("user", theActualUser); // Incluir información del usuario en la respuesta
            return theResponse; // Retornar la respuesta
        } else {
            response.sendError(401); // Enviar un error 401 si la verificación falla
            return theResponse; // Retornar la respuesta vacía
        }
    }

    // Método GET para generar y enviar el código 2FA al correo para la recuperación de contraseña
    @GetMapping("/recovery/{email}")
    public HashMap<String, Object> sendRecovery2FA(@PathVariable("email") String email) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        // Obtener el usuario real basado en el email proporcionado
        User theActualUser = this.theUserRepository.getUserByEmail(email);
        if (theActualUser != null) {
            // Generar código 2FA y enviar enlace para cambiar la contraseña
            int code2fa = requestService.codGenerator(); // Generar el código 2FA
            confirmCode2fa = String.valueOf(code2fa); // Almacenar el código para su posterior verificación

            // URL para restablecer la contraseña
            String resetUrl = "http://localhost:8081/api/public/security/reset-password" + email; // URL para el restablecimiento

            // Envío del correo con el código y enlace
            EmailSent emailSent = new EmailSent(email,
                    "Recuperación de contraseña",
                    "Tu código de seguridad para cambiar tu contraseña es: " + code2fa + ". Ingresa a este enlace para cambiar tu contraseña: <a href=\"" + resetUrl + "\">Restablecer contraseña</a>");
            requestService.sendEmail(emailSent); // Enviar el correo electrónico

            theResponse.put("message", "Código de recuperación enviado."); // Mensaje de éxito
            return theResponse; // Retornar la respuesta
        } else {
            theResponse.put("message", "Usuario no encontrado."); // Mensaje si el usuario no existe
            return theResponse; // Retornar la respuesta
        }
    }

    // Método POST para cambiar la contraseña validando el código 2FA
    @PostMapping("/reset-password")
    public HashMap<String, Object> resetPassword(@RequestBody HashMap<String, String> request) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        String email = request.get("email"); // Obtener el email del cuerpo de la solicitud
        String code = request.get("code"); // Obtener el código de seguridad del cuerpo de la solicitud
        String newPassword = request.get("newPassword"); // Obtener la nueva contraseña del cuerpo de la solicitud

        User theActualUser = this.theUserRepository.getUserByEmail(email); // Obtener el usuario basado en el email

        // Verificar que el usuario existe y que el código 2FA es correcto
        if (theActualUser != null && confirmCode2fa != null && confirmCode2fa.equals(code)) {
            // Cambiar la contraseña del usuario
            theActualUser.setPassword(theEncryptionService.convertSHA256(newPassword)); // Encriptar la nueva contraseña
            theUserRepository.save(theActualUser); // Guardar el usuario con la nueva contraseña

            theResponse.put("message", "Contraseña cambiada con éxito."); // Mensaje de éxito
            return theResponse; // Retornar la respuesta
        } else {
            theResponse.put("message", "Código 2FA incorrecto o expirado."); // Mensaje si el código es incorrecto
            return theResponse; // Retornar la respuesta
        }
    }

    // Método GET para redirigir a Google para la autenticación OAuth 2.0


    @PostMapping("permissions-validation")
    public boolean permissionsValidation(final HttpServletRequest request,
                                         @RequestBody Permission thePermission) {
        boolean success=this.theValidatorsService.validationRolePermission(request,thePermission.getUrl(),thePermission.getMethod());
        return success;
    }

    @PostMapping("/loginOld")
    public HashMap<String,Object> login(@RequestBody User theNewUser,
                                        final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>();
        String token="";
        User theActualUser=this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        if(theActualUser!=null &&
                theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))){
            token=theJwtService.generateToken(theActualUser);
            theActualUser.setPassword("");
            theResponse.put("token",token);
            theResponse.put("user",theActualUser);
            return theResponse;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return  theResponse;
        }

    }


}
