package com.rml.ms_security.Services;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service // Indica que esta clase es un servicio de Spring, permitiendo su inyección en otras clases
public class EncryptionService {

    // Método que convierte una contraseña en su hash SHA-256
    public String convertSHA256(String password) {
        MessageDigest md = null; // Inicializamos el objeto MessageDigest
        try {
            // Intentamos obtener una instancia del algoritmo SHA-256
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            // Si el algoritmo no está disponible, imprimimos la traza del error y retornamos null
            e.printStackTrace();
            return null;
        }
        // Procesamos la contraseña y la convertimos en un array de bytes
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer(); // Usamos StringBuffer para construir la cadena de resultado
        for(byte b : hash) {
            // Convertimos cada byte del hash en su representación hexadecimal
            sb.append(String.format("%02x", b));
        }
        return sb.toString(); // Retornamos el hash en formato hexadecimal
    }
}
