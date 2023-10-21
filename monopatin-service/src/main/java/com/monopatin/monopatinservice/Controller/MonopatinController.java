package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Repository.MonopatinRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monopatin")
public class MonopatinController {
    @Autowired
    private MonopatinRepository monopatinRepository;

    //obtengo todos los monopatines
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Monopatin> getAllMonopatines(){
        return monopatinRepository.findAll();
    }
    //obtengo un monopatin
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Monopatin getMonopatin(@PathVariable ObjectId id){
        return monopatinRepository.findById(id).orElse(null);
    }
    //creo un nuevo monopatin
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void crearMonopatin(@RequestBody Monopatin monopatin){
        monopatinRepository.save(monopatin);
    }
    //actualizo un monopatin
    @PutMapping("/actualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void actualizarMonopatin(@PathVariable ObjectId id, @RequestBody Monopatin monopatin) {
        Monopatin monopatinActual = monopatinRepository.findById(id).orElse(null);
        if (monopatinActual != null) {
            monopatinActual.setKm_totales(monopatin.getKm_totales());
            monopatinActual.setUbicacion(monopatin.getUbicacion());
            monopatinActual.setKm_recorridos(monopatin.getKm_recorridos());
            monopatinActual.setEstado(monopatin.getEstado());
            monopatinRepository.save(monopatinActual);

        }
    }

    //elimino un monopatin
    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarMonopatin(@PathVariable ObjectId id){
        monopatinRepository.deleteById(id);
    }

}
