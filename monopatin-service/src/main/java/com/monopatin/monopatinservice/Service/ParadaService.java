package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.DTO.ParadaDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface ParadaService {

    Parada agregarNuevaParada(ParadaDTO parada);
    String eliminarParada(ObjectId id);
    Optional<Parada> obtengoParadaID(ObjectId id);
    Optional<Parada> actualizarParada(ObjectId id, Parada parada);
    List<Parada> listaParadas();
    String estadoDeLaParadaActual(Parada parada, String ubicacion);

    void agregarMonopatinAParada(String ubicacion, Monopatin monopatin);

    Parada paradaExistente(String ubicacion);

    List<Monopatin> monopatinesEnParada(String ubicacion);
}
