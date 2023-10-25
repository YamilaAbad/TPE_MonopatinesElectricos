package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Service.MonopatinService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/monopatin")
public class MonopatinController {
    @Autowired
    MonopatinService monopatinService;

    //obtengo todos los monopatines
    @GetMapping("/monopatines")
    public List<Monopatin> getAllMonopatines(){
        return monopatinService.listaMonopatines();
    }

    //obtengo un monopatin
    @GetMapping("/monopatin/{id}")

    public Optional<Monopatin> getMonopatin(@PathVariable ObjectId id){
        return monopatinService.obtenerMonopatin(id);
    }
    //creo un nuevo monopatin
    @PostMapping("/crearMonopatin")
    public void crearMonopatin(@RequestBody Monopatin monopatin){
        monopatinService.guardarMonopatin(monopatin);
    }
    //actualizo un monopatin
    @PutMapping("/actualizar/{id}")
    public void actualizarMonopatin( @RequestBody Monopatin monopatin,@PathVariable ObjectId id) {
       monopatinService.actulizarMonopatin(monopatin,id);
    }

    //elimino un monopatin
    @DeleteMapping("/eliminar/{id}")
    public String eliminarMonopatin(@PathVariable ObjectId id){
       return monopatinService.eliminarMonopatin(id);
    }

}
