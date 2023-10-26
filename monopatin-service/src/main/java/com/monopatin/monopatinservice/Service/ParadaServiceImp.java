package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.DTO.ParadaDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Repository.ParadaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class ParadaServiceImp implements ParadaService{
    @Autowired
    ParadaRepository paradaRepository;

    @Override
    public Parada agregarNuevaParada(ParadaDTO paradaDTO) {
        Parada parada = new Parada();
        parada.setNombre(paradaDTO.getNombre());
        parada.setUbicacion(paradaDTO.getUbicacion());
        parada.setEstado(paradaDTO.getEstado());
        parada.setMonopatin(paradaDTO.getMonopatin());
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
            paradaRepository.save(paradaActual);
        }
        return oParada;
    }

    @Override
    public List<Parada> listaParadas() {
        List<Parada> paradas = paradaRepository.findAll();
        return paradas;
    }

    @Override
    public String estadoDeLaParadaActual(Parada parada, String ubicacion) {
        if (ubicacion.equals(parada.getUbicacion()) && "permitida".equals(parada.getEstado())){
            return "permitida";
        }else
            return "no permitida";
    }

    @Override
    public void agregarMonopatinAParada(String ubicacion, Monopatin monopatin) {
        Parada paradaActual = this.paradaExistente(ubicacion);
        if(paradaActual != null){
            paradaActual.addMonopatin(monopatin);
            paradaRepository.save(paradaActual);
        }
    }

    @Override
    public Parada paradaExistente(String ubicacion) {
        List<Parada> paradas = this.listaParadas();
        boolean encontrada = false;
        int i=0;
        while (!encontrada){
            if(ubicacion.equals(paradas.get(i).getUbicacion())){
                encontrada=true;
            }
            i++;
        }
        if (encontrada){
            return paradas.get(i-1);
        }
        return null;
    }

    @Override
    public List<Monopatin> monopatinesEnParada(String ubicacion) {
        Parada parada = this.paradaExistente(ubicacion);
        return parada.getMonopatin();
    }
    //para punto 3-G
    //0.003236 coordenada = a 100 mts
    // el valor donde estoy + (0.003236*3) y ahi veo si esta o no cerca de alguna de las coordenadas de los monopatines
    // *3 por ejemplo para que se tome como "cerca" 300 mts a mi alrededor
}
