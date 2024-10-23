package com.yard.ms_security.Services;


import com.yard.ms_security.Entities.UserEntity;
import com.yard.ms_security.Models.EmailSent;
import com.yard.ms_security.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class RequestService {
    @Value("${url-ms-notificaciones}")
    private String urlMsN;
    @Autowired
    private RestTemplate restTemplate;

    public List<UserEntity> getUsers() {
        String endPointName = "/get-users";
        String url = "http://127.0.0.1:5000/get-users";
        ResponseEntity<UserEntity[]> response = restTemplate.getForEntity(url, UserEntity[].class);
        UserEntity[] users = response.getBody();
        return Arrays.asList(users);
    }

    public void sendEmail(EmailSent emailSent) {
        String endPointName = "/send-email";
        String urlNotificaciones = urlMsN + endPointName;
        ResponseEntity<User> response = restTemplate.postForEntity(urlNotificaciones, emailSent, User.class);
        User users = response.getBody();
    }

    public int codGenerator() {
        final Random random = new Random();
        int code = 100000;

        code += random.nextInt(900000);
        return code;
    }
}
