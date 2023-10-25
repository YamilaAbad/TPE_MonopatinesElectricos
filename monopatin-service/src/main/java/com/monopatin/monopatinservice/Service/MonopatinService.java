package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import org.bson.types.ObjectId;

import java.util.Optional;
import java.util.List;
public interface MonopatinService {
    Monopatin guardarMonopatin(Monopatin monopatin);
    String eliminarMonopatin(ObjectId id);
    Optional<Monopatin>actulizarMonopatin(Monopatin monopatin, ObjectId id);
    Optional<Monopatin> obtenerMonopatin(ObjectId id);
    List<Monopatin> listaMonopatines();

}
