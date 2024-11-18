package com.rml.ms_security.Services;

import com.rml.ms_security.Entities.UserEntity;
import com.rml.ms_security.Models.EmailSent;
import com.rml.ms_security.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service // Indica que esta clase es un servicio de Spring, permitiendo su inyección en otras clases
public class RequestService {

    @Value("${url-ms-notificaciones}")
    private String urlMsN; // URL del microservicio de notificaciones

    @Autowired
    private RestTemplate restTemplate; // RestTemplate para realizar llamadas HTTP

    // Método para obtener la lista de usuarios desde un microservicio
    public List<UserEntity> getUsers() {
        String endPointName = "/get-users"; // Nombre del endpoint para obtener usuarios
        String url = "http://127.0.0.1:5000/get-users"; // URL del microservicio que devuelve usuarios
        // Realiza una llamada GET y obtiene la respuesta
        ResponseEntity<UserEntity[]> response = restTemplate.getForEntity(url, UserEntity[].class);
        UserEntity[] users = response.getBody(); // Obtiene el cuerpo de la respuesta como arreglo de UserEntity
        return Arrays.asList(users); // Convierte el arreglo a una lista y la retorna
    }

    // Método para enviar un correo electrónico
    public void sendEmail(EmailSent emailSent) {
        String endPointName = "/send-email"; // Nombre del endpoint para enviar correos
        String urlNotificaciones = urlMsN + endPointName; // Construye la URL del microservicio de notificaciones
        // Realiza una llamada POST para enviar el correo
        ResponseEntity<User> response = restTemplate.postForEntity(urlNotificaciones, emailSent, User.class);
        User users = response.getBody(); // Obtiene el cuerpo de la respuesta, aunque no se usa en este caso
    }

    // Método para generar un código aleatorio de 6 dígitos
    public int codGenerator() {
        final Random random = new Random(); // Crea un objeto Random para generar números aleatorios
        int code = 100000; // Código inicial de 6 dígitos

        code += random.nextInt(900000); // Genera un número aleatorio entre 0 y 899999 y lo suma
        return code; // Retorna el código generado
    }

    // Método para generar pagos

}
