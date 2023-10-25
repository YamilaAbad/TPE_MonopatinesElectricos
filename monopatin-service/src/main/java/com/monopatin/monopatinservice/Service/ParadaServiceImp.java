package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Repository.ParadaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ParadaServiceImp implements ParadaService{
    @Autowired
    ParadaRepository paradaRepository;
    //@Autowired
    //MongoTemplate mongoTemplate;
    @Override
    public Parada agregarNuevaParada(Parada parada) {
        return paradaRepository.insert(parada);
    }

    @Override
    public String eliminarParada(ObjectId id) {
        if(id != null){
            Boolean result = paradaRepository.existsById(id);
            if (result){
                paradaRepository.deleteById(id);
                return "Eliminada exitosamente";
            }
            return "id no valido";
        }
        return "no se proporciono un id";
    }

    @Override
    public Optional<Parada> obtengoParadaID(ObjectId id) {
        Optional<Parada> parada=(paradaRepository.findById(id));
        return parada;
    }

    @Override
    public Optional<Parada> actualizarParada(ObjectId id, Parada parada) {
        Optional<Parada> oParada = paradaRepository.findById(id);
        if (oParada.isPresent()){
            Parada paradaActual = oParada.get();
            paradaActual.setNombre(parada.getNombre());
            paradaActual.setUbicacion(parada.getUbicacion()); //en el caso de que se deseara ampliar la parada, su ubicacion seria distinta y tendria que ser actualizada.
            paradaActual.setEstado(parada.getEstado());
            paradaActual.setMonopatin(parada.getMonopatin());
        }
        return oParada;
    }

    @Override
    public List<Parada> listaParadas() {
        List<Parada> paradas = paradaRepository.findAll();
        return paradas;
    }
}
