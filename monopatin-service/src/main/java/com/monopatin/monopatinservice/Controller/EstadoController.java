package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Estado;
import com.monopatin.monopatinservice.Service.EstadoService;
import com.monopatin.monopatinservice.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    JwtService jwtService;
    @GetMapping("/estados")//obtengo todos los estados
    public List<Estado> getAllEstado(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return estadoService.listaEstados();
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    //obtengo un estado en concreto
    @GetMapping("/estado/{id}")
    public Optional<Estado> getEstado(@RequestHeader("Authorization") String authorizationHeader, @PathVariable ObjectId id){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return estadoService.obtenerEstadoID(id);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //creo un nuevo estado (solo se usaria cuando hay un nuevo monopatin)
    @PostMapping("/crearEstado")
    public void crearEstado(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Estado estado){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            estadoService.crearEstado(estado);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }

    //actualizo un estado
    @PutMapping("/actualizar/{id}")
    public Optional<Estado> actualizarEstado(@RequestHeader("Authorization") String authorizationHeader, @PathVariable ObjectId id, @RequestBody Estado estado) {
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return estadoService.actualizarEstado(id,estado);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //elimino un estado
    @DeleteMapping("/eliminar/{id}")
    public String eliminarEstado(@RequestHeader("Authorization") String authorizationHeader,  @PathVariable ObjectId id){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return estadoService.elimiarEstado(id);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

}
