package com.rml.ms_security.Controllers;

//import com.yard.ms_security.Services.OAuth2Service;
import com.rml.ms_security.Services.RequestService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

@RestController
public class OauthController {

    @Autowired
    private RequestService requestService;

//    @PostMapping("/login")
//    public ResponseEntity<String> sendemail(@RequestBody EmailContent emailContent) {
//        requestService.sendEmail(emailContent);
//        return new ResponseEntity<>("Email sent", HttpStatus.OK);
//    }

//    @GetMapping("/users")
//    public ResponseEntity<List<UserEntity>> getUsers() {
//        List<UserEntity> users = requestService.getUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to User page";
    }

//    @GetMapping("/google")
//    public RedirectView authenticateWithGoogle(HttpSession session) {
//        String state = UUID.randomUUID().toString();
//        session.setAttribute("oauth_state", state);
//        String authUrl = oauth2Service.getGoogleAuthUrl(state);
//        return new RedirectView(authUrl); //Le paso la URL para redireccionar al usuario
//    }

//    @GetMapping("/callback/{provider}")
//    public ResponseEntity<?> callback (@PathVarible String provider,
//                                       @RequestParam String code,
//                                       @RequestParam String state,
//                                       HttpSession session) {
//        String sessionState = (String) session.getAttribute("outh_state");
//    }
}
