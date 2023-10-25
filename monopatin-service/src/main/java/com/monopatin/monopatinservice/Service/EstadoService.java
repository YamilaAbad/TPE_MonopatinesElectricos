package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Estado;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface EstadoService {

    List<Estado> listaEstados();
    Optional<Estado> obtenerEstadoID(ObjectId id);
    Estado crearEstado(Estado estado);
    Optional<Estado> actualizarEstado(ObjectId id, Estado estado);
    String elimiarEstado(ObjectId id);

}
