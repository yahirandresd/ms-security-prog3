package com.yard.ms_security.Controllers;

import com.yard.ms_security.Models.User;
import com.yard.ms_security.Repositories.UserRepository;
import com.yard.ms_security.Services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private EncryptionService theEncryptionService;

    @GetMapping("")
    public List<User> find(){
        return this.theUserRepository.findAll();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable String id){
        User theUser=this.theUserRepository.findById(id).orElse(null);
        return theUser;
    }
    @PostMapping
    public User create(@RequestBody User newUser){
        newUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getPassword()));
        return this.theUserRepository.save(newUser);
    }
    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User newUser){
        User actualUser=this.theUserRepository.findById(id).orElse(null);
        if(actualUser!=null){
            actualUser.setName(newUser.getName());
            actualUser.setEmail(newUser.getEmail());
            actualUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getPassword()));
            this.theUserRepository.save(actualUser);
            return actualUser;
        }else{
            return null;
        }

    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        User theUser=this.theUserRepository.findById(id).orElse(null);
        if (theUser!=null){
            this.theUserRepository.delete(theUser);
        }
    }

    @GetMapping("/token")
    public String getToken(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            String token = oauth2Token.getPrincipal().getAttributes().get("access_token").toString();
            return "Access Token: " + token;
        }
        return "No access token available";
    }
}
