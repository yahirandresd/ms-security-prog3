package com.rml.ms_security.Services;

import com.rml.ms_security.Models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service // Indica que esta clase es un servicio de Spring, permitiendo su inyección en otras clases
public class JwtService {

    @Value("${jwt.secret}")
    private String secret; // Esta es la clave secreta que se utiliza para firmar el token. Debe mantenerse segura.

    @Value("${jwt.expiration}")
    private Long expiration; // Tiempo de expiración del token en milisegundos.

    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Genera la clave secreta para firmar el token

    // Método para generar un token JWT a partir de un objeto User
    public String generateToken(User theUser) {
        Date now = new Date(); // Obtiene la fecha y hora actual
        Date expiryDate = new Date(now.getTime() + expiration); // Calcula la fecha de expiración del token
        Map<String, Object> claims = new HashMap<>(); // Mapa para almacenar las reclamaciones (claims)
        claims.put("_id", theUser.get_id()); // Agrega el ID del usuario
        claims.put("name", theUser.getName()); // Agrega el nombre del usuario
        claims.put("email", theUser.getEmail()); // Agrega el email del usuario

        return Jwts.builder() // Construye el token JWT
                .setClaims(claims) // Establece las reclamaciones
                .setSubject(theUser.getName()) // Establece el sujeto del token
                .setIssuedAt(now) // Establece la fecha de emisión
                .setExpiration(expiryDate) // Establece la fecha de expiración
                .signWith(secretKey) // Firma el token con la clave secreta
                .compact(); // Compacta y devuelve el token
    }

    // Método para validar un token JWT
    public boolean validateToken(String token) {
        try {
            // Intenta parsear y validar el token
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Establece la clave secreta para la validación
                    .build()
                    .parseClaimsJws(token); // Parsea el token y devuelve las reclamaciones

            // Verifica la expiración del token
            Date now = new Date();
            if (claimsJws.getBody().getExpiration().before(now)) {
                return false; // Retorna falso si el token ha expirado
            }

            return true; // Retorna verdadero si el token es válido
        } catch (SignatureException ex) {
            // La firma del token es inválida
            return false;
        } catch (Exception e) {
            // Otra excepción
            return false;
        }
    }

    // Método para extraer un usuario del token JWT
    public User getUserFromToken(String token) {
        try {
            // Intenta parsear el token y validar la firma
            Jws<Claims> claimsJws = Jwts.parserBuilder() // Convierte el token que le acabe de mandar y le verifica la firma
                    .setSigningKey(secretKey) // Establece la clave secreta para la validación
                    .build()
                    .parseClaimsJws(token); // Descodifica el token

            Claims claims = claimsJws.getBody(); // Obtiene las reclamaciones del token

            User user = new User(); // Crea un nuevo objeto User
            // Llena el objeto User con los datos del token
            user.set_id((String) claims.get("_id")); // Codifica cada cosa una por una
            user.setName((String) claims.get("name"));
            user.setEmail((String) claims.get("email"));
            return user; // Devuelve el usuario
        } catch (Exception e) {
            // En caso de que el token sea inválido o haya expirado
            return null;
        }
    }
}
