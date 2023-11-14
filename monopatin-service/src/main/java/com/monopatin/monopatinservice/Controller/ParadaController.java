package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.DTO.ParadaDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Service.ParadaService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
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

    //obtengo el estado de la parada actual, la ubicacion sería enviada mediante GPS la
    @GetMapping("/estadoDeLaParada/{ubicacion}")
    public String estadoDeLaParada(@RequestBody Parada parada,@PathVariable String ubicacion){
        return paradaService.estadoDeLaParadaActual(parada, ubicacion);
    }

    //si existe la parada en la ubicación dada, la devuelve, si no devuelve null
    @GetMapping("/paradaEnUbicacion/{ubicacion}")
    public Parada paradaExistente(@PathVariable String ubicacion){
        return paradaService.paradaExistente(ubicacion);
    }

    //obtengo los monopatines que se encuentran en una parada pasada por parametro
    @GetMapping("/monopatinesEnParada/{ubicacion}")
    public List<Monopatin> monopatinesEnParada(@PathVariable String ubicacion){
        return paradaService.monopatinesEnParada(ubicacion);
    }

    //creo una nueva parada
    @PostMapping("/crearParada")
    public void crearParada(@RequestBody ParadaDTO parada){
        paradaService.agregarNuevaParada(parada);
    }

    //actualizar una parada
    @PutMapping("/actualizarParada/{id}")
    public void actualizarParada(@PathVariable ObjectId id, @RequestBody Parada parada){
        paradaService.actualizarParada(id,parada);
    }
    @PutMapping("/agregarMonopatinAParada/{ubicacion}")
    public void agregarMonopatinAParada(@PathVariable String ubicacion, @RequestBody Monopatin monopatin){
        paradaService.agregarMonopatinAParada(ubicacion,monopatin);
    }

    //elimino una parada
    @DeleteMapping("/eliminarParada/{id}")
    public String eliminarParada(@PathVariable ObjectId id){
       return paradaService.eliminarParada(id);
    }
}
