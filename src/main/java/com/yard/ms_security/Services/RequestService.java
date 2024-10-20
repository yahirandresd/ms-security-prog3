package com.yard.ms_security.Services;


import com.yard.ms_security.Entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RestTemplate restTemplate;

    public List<UserEntity> getUsers() {
        String url = "http://127.0.0.1:5000/get-users";
        ResponseEntity<UserEntity[]> response = restTemplate.getForEntity(url, UserEntity[].class);
        UserEntity[] users = response.getBody();
        return Arrays.asList(users);
    }

    public void sendEmail() {
        String url = "http://127.0.0.1:5000/send-email";
        ResponseEntity<UserEntity[]> response = restTemplate.getForEntity(url, UserEntity[].class);
        UserEntity[] users = response.getBody();
    }
}
