package com.rml.ms_security.Services;

import com.rml.ms_security.Models.Tarjetas.Tarjeta;
import com.rml.ms_security.Models.Tarjetas.AsociarTarjetaRequest;
import com.rml.ms_security.Models.User;  // Asegúrate de que sea 'User' o 'Usuario', dependiendo de tu modelo
import com.rml.ms_security.Repositories.TarjetaRepository;
import com.rml.ms_security.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private UserRepository usuarioRepository;

    public String asociarTarjeta(AsociarTarjetaRequest request) {
        // Verificar si el usuario existe
        Optional<User> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if (!usuarioOpt.isPresent()) {
            return "Usuario no encontrado";
        }

        // Crear la tarjeta
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(request.getNumeroTarjeta());
        tarjeta.setFechaExpiracion(request.getFechaExpiracion());
        tarjeta.setNombreTitular(request.getNombreTitular());
        tarjeta.setTipoTarjeta(request.getTipoTarjeta());
        tarjeta.setUsuarioId(request.getUsuarioId());

        // Guardar la tarjeta en la base de datos
        tarjetaRepository.save(tarjeta);

        return "Tarjeta asociada correctamente";
    }

    // Método para enmascarar el número de la tarjeta
    private String enmascararNumeroTarjeta(String numeroTarjeta) {
        return numeroTarjeta.substring(0, 6) + "******" + numeroTarjeta.substring(numeroTarjeta.length() - 4);
    }
}
