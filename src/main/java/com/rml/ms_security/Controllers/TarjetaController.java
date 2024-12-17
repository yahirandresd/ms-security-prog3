package com.rml.ms_security.Controllers;

import com.rml.ms_security.Models.Tarjetas.AsociarTarjetaRequest;
import com.rml.ms_security.Models.Tarjetas.Tarjeta;
import com.rml.ms_security.Models.User;
import com.rml.ms_security.Repositories.UserRepository;
import com.rml.ms_security.Services.TarjetaService;
import com.rml.ms_security.Repositories.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Map<String, Object>> procesarPagoSimplificado(@RequestBody Map<String, String> pagoData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 1. Validar que el usuario existe
            String usuarioId = pagoData.get("usuarioId");
            String tarjetaId = pagoData.get("tarjetaId");
            Optional<User> usuarioOpt = usuarioRepository.findById(usuarioId);

            if (!usuarioOpt.isPresent()) {
                response.put("mensaje", "Usuario no encontrado con ID: " + usuarioId);
                return ResponseEntity.status(404).body(response);
            }

            // 2. Validar que la tarjeta existe y pertenece al usuario
            Optional<Tarjeta> tarjetaOpt = tarjetaRepository.findById(tarjetaId);

            if (!tarjetaOpt.isPresent() || !tarjetaOpt.get().getUsuarioId().equals(usuarioId)) {
                response.put("mensaje", "Tarjeta no encontrada o no pertenece al usuario");
                return ResponseEntity.status(404).body(response);
            }

            // 3. Recuperar los datos de la tarjeta
            Tarjeta tarjeta = tarjetaOpt.get();
            String numeroTarjeta = tarjeta.getNumeroTarjeta();
            String[] fechaExpiracion = tarjeta.getFechaExpiracion().split("/");
            String expMonth = fechaExpiracion[0];
            String expYear = fechaExpiracion[1];

            // 4. Validar la factura en AdonisJS
            String facturaId = pagoData.get("facturaId");
            String adonisUrl = "http://localhost:3333/facturas/" + facturaId;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> facturaResponse = restTemplate.getForEntity(adonisUrl, Map.class);

            Integer montoFactura = (Integer) facturaResponse.getBody().get("monto");
            System.out.println(montoFactura);

            if (!facturaResponse.getStatusCode().is2xxSuccessful() ||
                    "PAGADO".equals(facturaResponse.getBody().get("estado"))) {
                response.put("mensaje", "Factura no válida o ya está pagada");
                return ResponseEntity.status(400).body(response);
            }

            // 5. Preparar datos del pago para enviar a Flask
            Map<String, Object> datosPagoFlask = new HashMap<>();
            datosPagoFlask.put("card_number", numeroTarjeta);
            datosPagoFlask.put("exp_year", expYear);
            datosPagoFlask.put("exp_month", expMonth);
            datosPagoFlask.put("cvc", "123"); // Aquí puedes usar el valor real si lo tienes almacenado
            datosPagoFlask.put("name", pagoData.get("nombre"));
            datosPagoFlask.put("last_name", pagoData.get("apellido"));
            datosPagoFlask.put("email", pagoData.get("email"));
            datosPagoFlask.put("phone", pagoData.get("telefono"));
            datosPagoFlask.put("value", montoFactura);
            datosPagoFlask.put("bill", facturaId);
            datosPagoFlask.put("doc_number", pagoData.get("cedula"));
            datosPagoFlask.put("city", "Bogotá");
            datosPagoFlask.put("address", "Calle Falsa 123");
            datosPagoFlask.put("cell_phone", pagoData.get("telefono"));
            System.out.println(datosPagoFlask);

            String flaskUrl = "http://localhost:5001/process-payment";
            ResponseEntity<Map> flaskResponse = restTemplate.postForEntity(flaskUrl, datosPagoFlask, Map.class);

            // 6. Verificar respuesta de Flask
            // 6. Verificar respuesta de Flask
            Map<String, Object> flaskBody = flaskResponse.getBody();
            if (flaskBody == null || !Boolean.TRUE.equals(flaskBody.get("status"))) {
                response.put("mensaje", "Error en la pasarela de pagos");
                response.put("detalle", flaskBody != null ? flaskBody.get("detalle") : "Respuesta inválida");
                return ResponseEntity.status(400).body(response);
            }

// Validar si el estado del pago es 'Aceptada'
            Map<String, Object> flaskData = (Map<String, Object>) flaskBody.get("data");
            if (flaskData == null || !flaskData.get("estado").equals("Aceptada")) {
                response.put("mensaje", "Pago rechazado por la pasarela");
                response.put("detalle", flaskData != null ? flaskData.get("respuesta") : "Error desconocido");
                response.put("razon", flaskData != null && flaskData.get("cc_network_response") != null ?
                        ((Map<String, Object>) flaskData.get("cc_network_response")).get("message") :
                        "No se proporcionaron detalles");
                return ResponseEntity.status(400).body(response);
            }


            // 7. Actualizar el estado de la factura en AdonisJS
            Map<String, String> updateFacturaPayload = new HashMap<>();
            updateFacturaPayload.put("estado", "PAGADO");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(updateFacturaPayload, headers);
            ResponseEntity<Map> updateFacturaResponse = restTemplate.postForEntity(adonisUrl + "/actualizar", requestEntity, Map.class);

            if (!updateFacturaResponse.getStatusCode().is2xxSuccessful()) {
                response.put("mensaje", "Pago procesado, pero no se pudo actualizar la factura");
                return ResponseEntity.status(500).body(response);
            }

            // 8. Respuesta exitosa
            response.put("mensaje", "Pago procesado exitosamente y factura actualizada");
            response.put("data", flaskBody);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al procesar el pago");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }










}
