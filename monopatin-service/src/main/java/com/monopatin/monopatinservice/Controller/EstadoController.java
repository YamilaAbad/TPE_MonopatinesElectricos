package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Estado;
import com.monopatin.monopatinservice.Repository.EstadoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/{_id}")
    public Estado getEstado(@PathVariable ObjectId _id){
        return estadoRepository.findById(_id).orElse(null);
    }

    //creo un nuevo estado
    @PostMapping("/crearEstado")
    @ResponseStatus(HttpStatus.OK)
    public void crearEstado(@RequestBody Estado estado){
        estadoRepository.save(estado);
    }

    //actualizo un estado
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarEstado(@PathVariable ObjectId id, @RequestBody Estado estado) {

        Estado estadoActual = estadoRepository.findById(id).orElse(null);
        if (estadoActual != null) {
            estadoActual.setEstado(estado.getEstado());
            estadoRepository.save(estadoActual);
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
