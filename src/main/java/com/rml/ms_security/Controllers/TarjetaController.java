package com.rml.ms_security.Controllers;

import com.rml.ms_security.Models.Tarjetas.AsociarTarjetaRequest;
import com.rml.ms_security.Models.Tarjetas.Tarjeta;
import com.rml.ms_security.Models.User;
import com.rml.ms_security.Repositories.UserRepository;
import com.rml.ms_security.Services.TarjetaService;
import com.rml.ms_security.Repositories.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
    @RequestMapping("/api/public/tarjetas")  // Ruta base para las tarjetas (con prefijo "public")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private UserRepository usuarioRepository;

    // Endpoint de prueba para comprobar que la ruta funciona
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> prueba() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hola, si ves esto, estás accediendo a la ruta correctamente!");
        return ResponseEntity.ok(response);  // Devuelve una respuesta con un mensaje en formato JSON
    }

    // Endpoint para asociar una tarjeta a un usuario
    @PostMapping("/asociar")
    public ResponseEntity<Map<String, String>> asociarTarjeta(@RequestBody AsociarTarjetaRequest request) {
        String resultado = tarjetaService.asociarTarjeta(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", resultado);
        System.out.println(resultado);

        if (resultado.equals("Tarjeta asociada correctamente")) {
            return ResponseEntity.status(201).body(response);  // Código 201: Creado
        } else if (resultado.equals("Usuario no encontrado")) {
            return ResponseEntity.status(404).body(response);  // Código 404: Not Found
        } else {
            return ResponseEntity.status(400).body(response);  // Código 400: Bad Request para otros errores
        }
    }

    // Endpoint para obtener las tarjetas asociadas a un usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Object> obtenerTarjetas(@PathVariable String usuarioId) {

        // Verificar si el usuario existe
        Optional<User> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con ID: " + usuarioId + " ¿Quien Monda es ese, no joda marque bien o llamo a la fiscalia");
            response.put("estado", "404");

            return ResponseEntity.status(404).body(response);  // Código 404: Usuario no encontrado
        }

        // Si el usuario existe, buscar sus tarjetas
        List<Tarjeta> tarjetas = tarjetaRepository.findByUsuarioId(usuarioId);

        if (tarjetas.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontraron tarjetas para el usuario con ID: " + usuarioId);
            response.put("estado", "202 para USER y 404 para TARJETAS");
            response.put("tarjetas", tarjetas);  // Lista vacía

            return ResponseEntity.status(202).body(response);  // Código 404 con mensaje personalizado
        }

        // Si el usuario existe y tiene tarjetas, devolver las tarjetas
        return ResponseEntity.ok(tarjetas);  // Código 200: OK
    }

    // Endpoint para procesar el pago
    @PostMapping("/procesar-pago")
    public ResponseEntity<Map<String, Object>> procesarPago(@RequestBody Map<String, String> pagoData) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(pagoData);
        try {
            // Validaciones de usuario y tarjeta
            User usuario = usuarioRepository.findById(pagoData.get("usuarioId"))
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + pagoData.get("usuarioId")));

            Tarjeta tarjeta = tarjetaRepository.findById(pagoData.get("tarjetaId"))
                    .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada para el usuario con ID: " + pagoData.get("usuarioId")));

            // Aquí se construye el JSON
            Map<String, Object> datosPagoFlask = new HashMap<>();
            datosPagoFlask.put("card_number", tarjeta.getNumeroTarjeta());
            System.out.println(tarjeta.getNumeroTarjeta());
            datosPagoFlask.put("exp_year", tarjeta.getFechaExpiracion().substring(3, 7)); // Formato YY
            datosPagoFlask.put("exp_month", tarjeta.getFechaExpiracion().substring(0, 2)); // Formato MM

            datosPagoFlask.put("cvc", pagoData.get("cvc"));
            datosPagoFlask.put("name", pagoData.get("nombre"));
            datosPagoFlask.put("last_name", pagoData.get("apellido"));
            datosPagoFlask.put("email", pagoData.get("email"));
            datosPagoFlask.put("phone", pagoData.get("telefono"));
            datosPagoFlask.put("value", pagoData.get("monto"));
            datosPagoFlask.put("bill", pagoData.get("facturaId"));
            datosPagoFlask.put("doc_number", "123456789"); // Reemplazar según tu lógica
            datosPagoFlask.put("city", "Bogotá");
            datosPagoFlask.put("address", "Calle Falsa 123");
            datosPagoFlask.put("cell_phone", pagoData.get("telefono"));
            System.out.println(datosPagoFlask);

            // Llamada al servicio Flask
            String flaskUrl = "http://localhost:5001/process-payment";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> flaskResponse = restTemplate.postForEntity(flaskUrl, datosPagoFlask, Map.class);

            if (flaskResponse.getStatusCode().is2xxSuccessful()) {
                response.put("mensaje", "Pago procesado exitosamente");
                response.put("data", flaskResponse.getBody());
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar el pago");
                response.put("error", flaskResponse.getBody());
                return ResponseEntity.status(500).body(response);
            }

        } catch (RuntimeException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }





}
