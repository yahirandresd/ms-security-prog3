package com.yard.ms_security.Controllers;

import com.yard.ms_security.Entities.UserEntity;
import com.yard.ms_security.Services.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class OauthController {

    private RequestService requestService;

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> users = requestService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Bienvenido a la pagin de inicio de usuario", HttpStatus.OK);
    }
}
