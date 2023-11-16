package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.DTO.MonopatinDTO;
import com.monopatin.monopatinservice.DTO.ParadaDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Service.JwtService;
import com.monopatin.monopatinservice.Service.ParadaService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    JwtService jwtService;

    //obtengo todas las paradas
    @GetMapping("/paradas")
    public List<Parada>getAllParadas(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return paradaService.listaParadas();
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //obtengo una parada
    @GetMapping("/paradaID/{id}")
    public Optional<Parada> getParada(@RequestHeader("Authorization") String authorizationHeader,@PathVariable ObjectId id){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return paradaService.obtengoParadaID(id);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //obtengo el estado de la parada actual, la ubicacion sería enviada mediante GPS la
    @GetMapping("/estadoDeLaParada/{ubicacion}")
    public String estadoDeLaParada(@RequestHeader("Authorization") String authorizationHeader,@RequestBody Parada parada,@PathVariable String ubicacion){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return paradaService.estadoDeLaParadaActual(parada, ubicacion);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //si existe la parada en la ubicación dada, la devuelve, si no devuelve null
    @GetMapping("/paradaEnUbicacion/{ubicacion}")
    public ParadaDTO paradaExistente(@RequestHeader("Authorization") String authorizationHeader,@PathVariable String ubicacion){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            Parada p = paradaService.paradaExistente(ubicacion);
            return paradaService.castParadaDTO(p);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //obtengo los monopatines que se encuentran en una parada pasada por parametro
    @GetMapping("/monopatinesEnParada/{ubicacion}")
    public List<MonopatinDTO> monopatinesEnParada(@RequestHeader("Authorization") String authorizationHeader,@PathVariable String ubicacion){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return paradaService.monopatinesEnParada(ubicacion);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    //creo una nueva parada
    @PostMapping("/crearParada")
    public void crearParada(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ParadaDTO parada){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            paradaService.agregarNuevaParada(parada);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }

    //actualizar una parada
    @PutMapping("/actualizarParada/{id}")
    public void actualizarParada(@RequestHeader("Authorization") String authorizationHeader,@PathVariable ObjectId id, @RequestBody ParadaDTO parada){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            paradaService.actualizarParada(id,parada);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }
    @PutMapping("/agregarMonopatinAParada/{ubicacion}")
    public void agregarMonopatinAParada(@RequestHeader("Authorization") String authorizationHeader,@PathVariable String ubicacion, @RequestBody MonopatinDTO monopatin){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            paradaService.agregarMonopatinAParada(ubicacion,monopatin);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }

    //elimino una parada
    @DeleteMapping("/eliminarParada/{id}")
    public String eliminarParada(@RequestHeader("Authorization") String authorizationHeader,@PathVariable ObjectId id){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return paradaService.eliminarParada(id);
        }else {
          ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
          return null;
        }
    }
}
