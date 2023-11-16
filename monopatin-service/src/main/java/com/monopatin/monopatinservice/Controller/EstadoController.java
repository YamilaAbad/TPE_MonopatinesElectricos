package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Estado;
import com.monopatin.monopatinservice.Service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/estadoMonopatin")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class EstadoController {

    @Autowired
    EstadoService estadoService;

    //obtengo todos los estados
    @GetMapping("/estados")
    public List<Estado> getAllEstado(){
        return estadoService.listaEstados();
    }
    //obtengo un estado en concreto
    @GetMapping("/estado/{id}")
    public Optional<Estado> getEstado(@PathVariable ObjectId id){
        return estadoService.obtenerEstadoID(id);
    }

    //creo un nuevo estado (solo se usaria cuando hay un nuevo monopatin)
    @PostMapping("/crearEstado")
    public void crearEstado(@RequestBody Estado estado){
        estadoService.crearEstado(estado);
    }

    //actualizo un estado
    @PutMapping("/actualizar/{id}")
    public Optional<Estado> actualizarEstado(@PathVariable ObjectId id, @RequestBody Estado estado) {
       return estadoService.actualizarEstado(id,estado);
    }

    //elimino un estado
    @DeleteMapping("/eliminar/{id}")
    public String eliminarEstado(@PathVariable ObjectId id){
       return estadoService.elimiarEstado(id);
    }

}
