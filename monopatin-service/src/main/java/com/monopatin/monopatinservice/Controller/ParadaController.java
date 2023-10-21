package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Repository.ParadaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parada")
public class ParadaController {

    @Autowired
    private ParadaRepository paradaRepository;

    //obtengo todas las paradas
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Parada>getAllParadas(){
        return paradaRepository.findAll();
    }

    //obtengo una parada
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Parada getParada(@PathVariable ObjectId id){
        return paradaRepository.findById(id).orElse(null);
    }

    //creo una nueva parada
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void crearParada(@RequestBody Parada parada){
        paradaRepository.save(parada);
    }

    //actualizar una parada
    @PutMapping("/actualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void actualizarParada(@PathVariable ObjectId id, Parada parada){
        Parada paradaActual = paradaRepository.findById(id).orElse(null);
        if (paradaActual != null){
            paradaActual.setNombre(parada.getNombre());
            paradaActual.setUbicacion(parada.getUbicacion()); //en el caso de que se deseara ampliar la parada, su ubicacion seria distinta y tendria que ser actualizada.
            paradaActual.setEstado(parada.getEstado());
            paradaActual.setMonopatin(parada.getMonopatin());
        }
    }

    //elimino una parada
    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarParada(@PathVariable ObjectId id){
        paradaRepository.deleteById(id);
    }
}
