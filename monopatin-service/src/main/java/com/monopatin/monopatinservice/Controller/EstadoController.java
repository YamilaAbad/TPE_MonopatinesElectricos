package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Estado;
import com.monopatin.monopatinservice.Repository.EstadoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/estadoMonopatin")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;
    //obtengo todos los estados
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Estado> getAllEstado(){
        return estadoRepository.findAll();
    }
    //obtengo un estado en concreto
    @GetMapping("/{id}")
    public Estado getEstado(@PathVariable ObjectId id){
        return estadoRepository.findById(id).orElse(null);
    }

    //creo un nuevo estado (solo se usaria cuando hay un nuevo monopatin)
    @PostMapping("/crearEstado")
    @ResponseStatus(HttpStatus.OK)
    public void crearEstado(@RequestBody Estado estado){
        estadoRepository.save(estado);
    }

    //actualizo un estado
    @PutMapping("/actualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void actualizarEstado(@PathVariable ObjectId id, @RequestBody Estado estado) {

        Estado estadoActual = estadoRepository.findById(id).orElse(null);
        if (estadoActual != null) {
            estadoActual.setEstado(estado.getEstado());
            estadoRepository.save(estadoActual);
        }
    }

    //elimino un estado
    @DeleteMapping("eliminar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarEstado(@PathVariable ObjectId id){
        estadoRepository.deleteById(id);
    }

}
