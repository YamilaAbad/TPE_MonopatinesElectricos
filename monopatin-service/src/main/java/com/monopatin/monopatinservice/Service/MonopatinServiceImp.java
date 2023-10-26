package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Repository.MonopatinRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            return "Id no valido";
        }
        return "No se proporcionó un id";
    }


    @Override
    public Optional<Monopatin> actulizarMonopatin(int km,String ubicacion, String estado, ObjectId id) {
        Optional<Monopatin> monopatin = monopatinRepository.findById(id);
        if (monopatin.isPresent()){
            Monopatin monopatinActual = monopatin.get();
            monopatinActual.setKm_totales(monopatinActual.getKm_totales()+km);
            monopatinActual.setUbicacion(ubicacion);
            monopatinActual.setKm_recorridos(monopatinActual.getKm_recorridos()+km);
            monopatinActual.getEstado().setEstado(estado);
            monopatinRepository.save(monopatinActual);
        }
        return monopatin;
    }

    @Override
    public Optional<Monopatin> obtenerMonopatin(ObjectId id) {
        Optional<Monopatin> monopatin=(monopatinRepository.findById(id));
        return monopatin;
    }

    @Override
    public List<Monopatin> listaMonopatines(){
        return monopatinRepository.findAll();
    }

    @Override
    public List<Monopatin> reporteMonopatinesPorKmR(int km) {
        List<Monopatin> monopatines = this.listaMonopatines();
        List<Monopatin> monopatineskms = new ArrayList<>();
        for (Monopatin m: monopatines){
            if (m.getKm_recorridos()>=km){
                monopatineskms.add(m);
            }
        }
        return monopatineskms;
    }

    @Override
    public String cantidadDeMonopatinesEstados() {
        List<Monopatin> monopatines = this.listaMonopatines();
        int cantMant = 0;
        int cantFun = 0;
        for(Monopatin m:monopatines){
            if ("habilitado".equals(m.getEstado().getEstado())){
                cantFun++;
            }else if ("mantenimiento".equals(m.getEstado().getEstado())){
                cantMant++;
            }
        }
        return ("Monopatines habilitados: "+cantFun+"\nMonopatines en mantenimiento: "+cantMant);
    }
}
