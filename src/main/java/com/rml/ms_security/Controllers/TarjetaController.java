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
    public ResponseEntity<Map<String, Object>> procesarPago(@RequestBody Map<String, String> pagoData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 1. Validar si la factura existe y no está pagada en AdonisJS
            String adonisUrl = "http://localhost:3333/facturas/" + pagoData.get("facturaId");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> facturaResponse = restTemplate.getForEntity(adonisUrl, Map.class);
            String estadoPago = (String) facturaResponse.getBody().get("estado");
            System.out.println(estadoPago);

            if (!facturaResponse.getStatusCode().is2xxSuccessful()) {
                response.put("mensaje", "Factura no válida");
                return ResponseEntity.status(400).body(response);
            }
            if (estadoPago.equals("PAGADO")) {
                response.put("mensaje", "Factura ya está en estado 'PAGADO'");
                System.out.println("FACTURA YA PAGADA HPTA");
                return ResponseEntity.status(200).body(response);

            }

            System.out.println();

            // 2. Preparar datos del pago y enviarlos a Flask
            Map<String, Object> datosPagoFlask = new HashMap<>();
            datosPagoFlask.put("card_number", pagoData.get("numeroTarjeta"));
            datosPagoFlask.put("exp_year", pagoData.get("expYear")); // Año de expiración (YYYY)
            datosPagoFlask.put("exp_month", pagoData.get("expMonth")); // Mes de expiración (MM)
            datosPagoFlask.put("cvc", pagoData.get("cvc")); // Código de seguridad
            datosPagoFlask.put("name", pagoData.get("nombre")); // Nombre del titular
            datosPagoFlask.put("last_name", pagoData.get("apellido")); // Apellido del titular
            datosPagoFlask.put("email", pagoData.get("email")); // Correo del cliente
            datosPagoFlask.put("phone", pagoData.get("telefono")); // Teléfono del cliente
            datosPagoFlask.put("value", pagoData.get("monto")); // Monto del pago
            datosPagoFlask.put("bill", pagoData.get("facturaId")); // ID de la factura
            datosPagoFlask.put("doc_number", "123456789"); // Documento ficticio (puedes reemplazarlo)
            datosPagoFlask.put("city", "Bogotá"); // Ciudad del cliente
            datosPagoFlask.put("address", "Calle Falsa 123"); // Dirección del cliente
            datosPagoFlask.put("cell_phone", pagoData.get("telefono")); // Teléfono del cliente

            String flaskUrl = "http://localhost:5001/process-payment";
            ResponseEntity<Map> flaskResponse = restTemplate.postForEntity(flaskUrl, datosPagoFlask, Map.class);

            System.out.println(datosPagoFlask);

            // 3. Verificar si la respuesta de Flask tiene un estado de "error"
            Map<String, Object> flaskBody = flaskResponse.getBody();
            if (flaskBody == null ||
                    !Boolean.TRUE.equals(flaskBody.get("status")) ||
                    flaskBody.containsKey("data") && flaskBody.get("data") instanceof Map &&
                            !((Map<String, Object>) flaskBody.get("data")).get("estado").equals("Aceptada")) {

                // Si el estado no es "Aceptada" o hay un error
                response.put("mensaje", "Error en la pasarela de pagos");

                // Si hay un mensaje de error en la pasarela, inclúyelo en la respuesta
                if (flaskBody != null && flaskBody.containsKey("data")) {
                    Map<String, Object> data = (Map<String, Object>) flaskBody.get("data");
                    response.put("detalle", data.get("respuesta") != null ? data.get("respuesta") : "Error desconocido");
                } else {
                    response.put("detalle", "Respuesta inválida de la pasarela");
                }
                return ResponseEntity.status(400).body(response);
            }

            // 4. Si no hubo error en la pasarela de pagos, actualizar el estado de la factura a "PAGADO"
            Map<String, String> updateFacturaPayload = new HashMap<>();
            updateFacturaPayload.put("estado", "PAGADO");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(updateFacturaPayload, headers);
            ResponseEntity<Map> updateFacturaResponse = restTemplate.postForEntity(adonisUrl + "/actualizar", requestEntity, Map.class);

            if (!updateFacturaResponse.getStatusCode().is2xxSuccessful()) {
                response.put("mensaje", "Pago exitoso, pero no se pudo actualizar la factura a 'PAGADO'");
                response.put("error", updateFacturaResponse.getBody());
                return ResponseEntity.status(500).body(response);
            }

            // 5. Respuesta exitosa
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
