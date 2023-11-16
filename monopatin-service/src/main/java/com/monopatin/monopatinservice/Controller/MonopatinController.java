package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.DTO.MonopatinDTO;
import com.monopatin.monopatinservice.DTO.PausaDTO;
import com.monopatin.monopatinservice.DTO.ViajeDTO;
import com.monopatin.monopatinservice.JWT.JwtAuthenticationFilter;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Service.JwtService;
import com.monopatin.monopatinservice.Service.MonopatinService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/monopatin")
public class MonopatinController {
    @Autowired
    MonopatinService monopatinService;
   @Autowired
   JwtService jwtService;

    //obtengo todos los monopatines
    @GetMapping("/monopatines")
    public List<Monopatin> getAllMonopatines(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return monopatinService.listaMonopatines();
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }

    }

    //obtengo un monopatin
    @GetMapping("/monopatin/{id}")
    public Optional<Monopatin> getMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable ObjectId id){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return monopatinService.obtenerMonopatin(id);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //reporte de uso de monopatines por kilómetros recorridos
    @GetMapping("/reporteMonopatinesPorKmsRecorridos/{km}")
    public List<Monopatin> reporteMonopatinesPorKmR(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int km){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return monopatinService.reporteMonopatinesPorKmR(km);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    //reporte de la cantidad de monopatines en operación vs en mantenimiento
    @GetMapping("/reporteMonopatinesEstado")
    public ResponseEntity<String> cantidadDeMonopatinesEstados(@RequestHeader("Authorization") String authorizationHeader){
        //extrae el token del encabezado de autorización
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return ResponseEntity.ok(monopatinService.cantidadDeMonopatinesEstados());
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }

    private String obtenerReporteMonopatines(String username) {
        // Realiza la lógica para obtener el reporte de monopatines según el usuario
        // Devuelve el reporte como un String
        return "Reporte de monopatines para el usuario: " + username;
    }

    //lista de monopatines cercanos a la ubicacion enviada por endpoint (ubicacion del usuario)
    @GetMapping("/monopatinesCercanos/{ubicacion}")
    public List<MonopatinDTO> monopatinesCercanos(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String ubicacion){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return monopatinService.monopatinesCercanos(ubicacion);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }

    }

    //creo un nuevo monopatin
    @PostMapping("/crearMonopatin")
    public void crearMonopatin(@RequestHeader("Authorization") String authorizationHeader,@RequestBody MonopatinDTO monopatin){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            monopatinService.guardarMonopatin(monopatin);
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }
    //inicio un viaje desde el monopatin dado
    @PostMapping("/iniciarViaje/{idMon}")
    public void iniciarViaje(@RequestHeader("Authorization") String authorizationHeader,@RequestBody ViajeDTO viajeDTO, @PathVariable ObjectId idMon){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            monopatinService.iniciarViaje("/inicioViaje", viajeDTO, idMon);//usuarioService.verificar("/getTokenUser",user)
        }else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }
    //actualizo un monopatin
    @PutMapping("/actualizar/{km}/{ubicacion}/{estado}/{id}")
    public void actualizarMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int km,@PathVariable String ubicacion,@PathVariable String estado,@PathVariable ObjectId id) {
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            monopatinService.actulizarMonopatin(km, ubicacion, estado, id);
        }else{
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");

        }

    }

    //elimino un monopatin
    @DeleteMapping("/eliminar/{id}")
    public String eliminarMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable ObjectId id){
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            return monopatinService.eliminarMonopatin(id);
        }else{
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    @PutMapping("/finalizarViaje/{viajeId}")
    public ResponseEntity<ViajeDTO> finalizarViaje(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int viajeId, @RequestBody ViajeDTO viajeDTO) {
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            monopatinService.finalizarViaje("/finalizarViaje/{viajeId}", viajeDTO, viajeId);
            return new ResponseEntity<>(viajeDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("pausaViaje/{idViaje}")
    public ResponseEntity<ViajeDTO> pausarViaje(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int idViaje, @RequestBody PausaDTO pausaDTO) {
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            monopatinService.pausarViaje("/crearPausa/{idViaje}", pausaDTO, idViaje);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("cancelarpausa/{pausaId}")
    public ResponseEntity<ViajeDTO> cancelarPausaEnViaje(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int pausaId, @RequestBody PausaDTO pausaDTO) {
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            monopatinService.cancelarPausaEnViaje("/cancelarPausa/{pausaId}", pausaDTO, pausaId);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
