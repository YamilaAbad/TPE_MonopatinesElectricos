package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Model.Estado;
import com.monopatin.monopatinservice.Repository.EstadoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EstadoServiceImp implements EstadoService{
    @Autowired
    EstadoRepository estadoRepository;
    //@Autowired
    //MongoTemplate mongoTemplate;
    @Override
    public List<Estado> listaEstados() {
        List<Estado> estados = estadoRepository.findAll();
        return estados;
    }

    @Override
    public Optional<Estado> obtenerEstadoID(ObjectId id) {
        Optional<Estado> estado=(estadoRepository.findById(id));
        return estado;
    }

    @Override
    public Estado crearEstado(Estado estado) {
        return estadoRepository.insert(estado);
    }

    @Override
    public Optional<Estado> actualizarEstado(ObjectId id, Estado estado) {
        Optional<Estado> oEstado = estadoRepository.findById(id);
        if (oEstado.isPresent()) {
            Estado estadoActual = oEstado.get();
            estadoActual.setEstado(estado.getEstado());
            estadoRepository.save(estadoActual);
        }
        return oEstado;
    }

    @Override
    public String elimiarEstado(ObjectId id) {
        if(id != null){
            Boolean result = estadoRepository.existsById(id);
            if (result){
                estadoRepository.deleteById(id);
                return "Eliminada exitosamente";
            }
            return "id no valido";
        }
        return "no se proporciono un id";
    }
}
