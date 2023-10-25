package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Repository.MonopatinRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MonopatinServiceImp implements MonopatinService {
    @Autowired
    MonopatinRepository monopatinRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public Monopatin guardarMonopatin(Monopatin monopatin) {
       return monopatinRepository.insert(monopatin);
    }


    @Override
    public String eliminarMonopatin(ObjectId id) {
        if(id != null){
            Boolean result = monopatinRepository.existsById(id);
            if (result){
                monopatinRepository.deleteById(id);
                return "Eliminado exitosamente";
            }
            return "id no valido";
        }
        return "no se proporciono un id";
    }


    @Override
    public Optional<Monopatin> actulizarMonopatin(Monopatin monopatin, ObjectId id) {
        Optional<Monopatin> oMonopatin = monopatinRepository.findById(id);
        if (oMonopatin.isPresent()){
            Monopatin monopatinActual = oMonopatin.get();
            monopatinActual.setKm_totales(monopatin.getKm_totales());
            monopatinActual.setUbicacion(monopatin.getUbicacion());
            monopatinActual.setKm_recorridos(monopatin.getKm_recorridos());
            monopatinActual.setEstado(monopatin.getEstado());
            monopatinRepository.save(monopatinActual);
        }
        return oMonopatin;
    }

    @Override
    public Optional<Monopatin> obtenerMonopatin(ObjectId id) {
        Optional<Monopatin> monopatin=(monopatinRepository.findById(id));
        return monopatin;
    }

    @Override
    public List<Monopatin>listaMonopatines(){
        List<Monopatin> monopatines = monopatinRepository.findAll();
        return monopatines;
    }
}
