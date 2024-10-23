package com.yard.ms_security.Controllers;

import com.yard.ms_security.Models.EmailSent;
import com.yard.ms_security.Models.User;
import com.yard.ms_security.Repositories.UserRepository;
import com.yard.ms_security.Services.EncryptionService;
import com.yard.ms_security.Services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import com.yard.ms_security.Services.JwtService;

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

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody User theNewUser,
                                        final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>();

        User theActualUser=this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        if(theActualUser!=null &&
           theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))){
            int code2fa = requestService.codGenerator();
            confirmCode2fa = String.valueOf(code2fa);
            EmailSent emailSent = new EmailSent(theActualUser.getEmail(), "NUEVO INICIO DE SESIÓN", "Codigo de seguridad: " + String.valueOf(code2fa));
            requestService.sendEmail(emailSent);
            theResponse.put("message", "Email sent");
            return theResponse;
        }else{

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return  theResponse;
        }

    }

    @PostMapping("/verify2fa")
    public HashMap<String,Object> verify2fa(@RequestBody User theNewUser,
                                        final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>();

        User theActualUser=this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        String code = theNewUser.getPassword();

        if (confirmCode2fa!=null && confirmCode2fa.equals(code)) {
            String token = theJwtService.generateToken(theActualUser);
            theActualUser.setPassword("ENCRIPTADO POR EL IRLgg");
            theResponse.put("token", token);
            theResponse.put("user", theActualUser);
            return theResponse;
        }else{
            response.sendError(401);
            return theResponse;
        }

    }
    // Método GET para redirigir a Google para la autenticación OAuth 2.0
    @GetMapping("/login/google")
    public RedirectView loginWithGoogle() {
        return new RedirectView("/oauth2/authorization/google"); // Redirigir a Google para la autenticación
    }

}
