package com.rml.ms_security.Repositories;

import com.rml.ms_security.Models.Tarjetas.Tarjeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TarjetaRepository extends MongoRepository<Tarjeta, String> {
    // Método para buscar tarjetas por usuarioId
    List<Tarjeta> findByUsuarioId(String usuarioId);
}
