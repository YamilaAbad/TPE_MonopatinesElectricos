package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.DTO.MonopatinDTO;
import com.monopatin.monopatinservice.DTO.PausaDTO;
import com.monopatin.monopatinservice.DTO.ViajeDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import org.bson.types.ObjectId;

import java.util.Optional;
import java.util.List;
public interface MonopatinService {
    void guardarMonopatin(MonopatinDTO monopatin);

    String eliminarMonopatin(ObjectId id);

    //actualiza todos los datos del monopatin
    Optional<Monopatin> actulizarMonopatin(int km, String ubicacion, String estado, ObjectId id);

    Optional<Monopatin> obtenerMonopatin(ObjectId id);

    List<Monopatin> listaMonopatines();

    List<Monopatin> reporteMonopatinesPorKmR(int km);

    String cantidadDeMonopatinesEstados();

    List<MonopatinDTO> monopatinesCercanos(String ubicacion);

    void iniciarViaje(String viaje, ViajeDTO viajeDTO, ObjectId idMon);

    void finalizarViaje(String viaje, ViajeDTO viajeDTO, int idViaje);

    void pausarViaje(String s, PausaDTO pausaDTO, int viajeId);

    void cancelarPausaEnViaje(String viaje, PausaDTO pausaDTO, int idPausa);




}


