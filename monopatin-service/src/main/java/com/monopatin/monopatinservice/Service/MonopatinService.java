package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Monopatin;
import org.bson.types.ObjectId;

import java.util.Optional;
import java.util.List;
public interface MonopatinService {
    Monopatin guardarMonopatin(Monopatin monopatin);
    String eliminarMonopatin(ObjectId id);
    //actualiza todos los datos del monopatin
    Optional<Monopatin>actulizarMonopatin(int km,String ubicacion, String estado, ObjectId id);
    Optional<Monopatin> obtenerMonopatin(ObjectId id);
    List<Monopatin> listaMonopatines();
    List<Monopatin> reporteMonopatinesPorKmR(int km);
    String cantidadDeMonopatinesEstados();
}
