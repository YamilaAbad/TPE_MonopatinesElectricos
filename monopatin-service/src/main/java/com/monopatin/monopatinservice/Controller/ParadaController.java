package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Service.ParadaService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/parada")
public class ParadaController {

    @Autowired
    ParadaService paradaService;

    //obtengo todas las paradas
    @GetMapping("/paradas")
    public List<Parada>getAllParadas(){
        return paradaService.listaParadas();
    }

    //obtengo una parada
    @GetMapping("/paradaID/{id}")
    public Optional<Parada> getParada(@PathVariable ObjectId id){
        return paradaService.obtengoParadaID(id);
    }

    //creo una nueva parada
    @PostMapping("/crearParada")
    public void crearParada(@RequestBody Parada parada){
        paradaService.agregarNuevaParada(parada);
    }

    //actualizar una parada
    @PutMapping("/actualizarParada/{id}")
    public void actualizarParada(@PathVariable ObjectId id, Parada parada){
        paradaService.actualizarParada(id,parada);
    }

    //elimino una parada
    @DeleteMapping("/eliminarParada/{id}")
    public String eliminarParada(@PathVariable ObjectId id){
       return paradaService.eliminarParada(id);
    }
}
