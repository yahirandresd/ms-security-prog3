package com.yard.ms_security.Controllers;

import com.yard.ms_security.Models.EmailSent;
import com.yard.ms_security.Models.User;
import com.yard.ms_security.Repositories.UserRepository;
import com.yard.ms_security.Services.EncryptionService;
import com.yard.ms_security.Services.RequestService;
import com.yard.ms_security.Services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private EncryptionService theEncryptionService;

    @Autowired
    private JwtService theJwtService;

    @Autowired
    private RequestService requestService;

    private String confirmCode2fa;

    // Método login con recuperación de contraseña
    @PostMapping("/login")
    public HashMap<String, Object> login(@RequestBody User theNewUser, final HttpServletResponse response) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        if (theActualUser != null && theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
            // Generar código 2FA y enviarlo por correo
            int code2fa = requestService.codGenerator();
            confirmCode2fa = String.valueOf(code2fa);
            EmailSent emailSent = new EmailSent(theActualUser.getEmail(), "NUEVO INICIO DE SESIÓN", "Código de seguridad: " + code2fa);
            requestService.sendEmail(emailSent);
            theResponse.put("message", "Email enviado");
            return theResponse;
        } else {
            // Enviar correo para recuperación de contraseña si la contraseña es incorrecta
            String recoveryUrl = "http://localhost:8081/api/public/security/recovery/" + theNewUser.getEmail();
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

            EmailSent emailSent = new EmailSent(theNewUser.getEmail(),
                    "Recuperación de contraseña",
                    htmlContent);
            requestService.sendEmail(emailSent);

            // Establecer el estado HTTP 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Incluir un mensaje en el cuerpo de la respuesta
            theResponse.put("message", "Contraseña incorrecta. Email de recuperación enviado.");
            return theResponse; // Retorna la respuesta con el mensaje
        }
    }



    // Verificar 2fa
    @PostMapping("/verify2fa")
    public HashMap<String,Object> verify2fa(@RequestBody User theNewUser,
                                            final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>();

        User theActualUser=this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        String code = theNewUser.getPassword();

        if (confirmCode2fa!=null && confirmCode2fa.equals(code)) {
            String token = theJwtService.generateToken(theActualUser);
            theActualUser.setPassword("ENCRIPTADO POR EL IRL");
            theResponse.put("token", token);
            theResponse.put("user", theActualUser);
            return theResponse;
        }else{
            response.sendError(401);
            return theResponse;
        }

    }

    // Método GET para generar y enviar código 2FA al correo de recuperación
    @GetMapping("/recovery/{email}")
    public HashMap<String, Object> sendRecovery2FA(@PathVariable("email") String email) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        User theActualUser = this.theUserRepository.getUserByEmail(email);
        if (theActualUser != null) {
            // Generar código 2FA y enviar enlace para cambiar la contraseña
            int code2fa = requestService.codGenerator();
            confirmCode2fa = String.valueOf(code2fa);

            // URL para restablecer la contraseña
            String resetUrl = "http://localhost:8081/api/public/security/reset-password" + email;

            // Envío del correo con el código y enlace
            EmailSent emailSent = new EmailSent(email,
                    "Recuperación de contraseña",
                    "Tu código de seguridad para cambiar tu contraseña es: " + code2fa + ". Ingresa a este enlace para cambiar tu contraseña: <a href=\"" + resetUrl + "\">Restablecer contraseña</a>");
            requestService.sendEmail(emailSent);

            theResponse.put("message", "Código de recuperación enviado.");
            return theResponse;
        } else {
            theResponse.put("message", "Usuario no encontrado.");
            return theResponse;
        }
    }


    // Método POST para cambiar la contraseña validando el código 2FA
    @PostMapping("/reset-password")
    public HashMap<String, Object> resetPassword(@RequestBody HashMap<String, String> request) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();

        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");

        User theActualUser = this.theUserRepository.getUserByEmail(email);

        if (theActualUser != null && confirmCode2fa != null && confirmCode2fa.equals(code)) {
            // Cambiar la contraseña del usuario
            theActualUser.setPassword(theEncryptionService.convertSHA256(newPassword));
            theUserRepository.save(theActualUser);

            theResponse.put("message", "Contraseña cambiada con éxito.");
            return theResponse;
        } else {
            theResponse.put("message", "Código 2FA incorrecto o expirado.");
            return theResponse;
        }
    }

    // Método GET para redirigir a Google para la autenticación OAuth 2.0
    @GetMapping("/login/google")
    public RedirectView loginWithGoogle() {
        return new RedirectView("/oauth2/authorization/google"); // Redirigir a Google para la autenticación
    }
}