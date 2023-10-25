package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Parada;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public interface ParadaService {

    Parada agregarNuevaParada(Parada parada);
    String eliminarParada(ObjectId id);
    Optional<Parada> obtengoParadaID(ObjectId id);
    Optional<Parada> actualizarParada(ObjectId id, Parada parada);
    List<Parada> listaParadas();


}
