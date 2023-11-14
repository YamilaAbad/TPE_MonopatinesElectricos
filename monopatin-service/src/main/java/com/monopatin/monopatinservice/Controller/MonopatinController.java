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

   @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
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

    //reporte de uso de monopatines por kilómetros recorridos
    @GetMapping("/reporteMonopatinesPorKmsRecorridos/{km}")
    public List<Monopatin> reporteMonopatinesPorKmR(@PathVariable int km){
        return monopatinService.reporteMonopatinesPorKmR(km);
    }
    //reporte de la cantidad de monopatines en operación vs en mantenimiento
    @GetMapping("/reporteMonopatinesEstado")
    public ResponseEntity<String> cantidadDeMonopatinesEstados(@RequestHeader("Authorization") String authorizationHeader){
        // Extrae el token del encabezado de autorización
        System.out.println("llega");
       // jwtAuthenticationFilter.doFilter(authorizationHeader,"Bearer",token);
        String token = authorizationHeader.replace("Bearer ", "");
        if(jwtService.isTokenValid(token)) {
            String username = jwtService.getUsernameFromToken(token);
            return ResponseEntity.ok(monopatinService.cantidadDeMonopatinesEstados());
        }else {
            // El token no es válido, manejar el error según sea necesario
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
    public List<MonopatinDTO> monopatinesCercanos(@PathVariable String ubicacion){
        return monopatinService.monopatinesCercanos(ubicacion);
    }

    //creo un nuevo monopatin
    @PostMapping("/crearMonopatin")
    public void crearMonopatin(@RequestBody MonopatinDTO monopatin){
        monopatinService.guardarMonopatin(monopatin);
    }

    //inicio un viaje desde el monopatin dado
    @PostMapping("/iniciarViaje/{idMon}")
    public void iniciarViaje(@RequestBody ViajeDTO viajeDTO, @PathVariable ObjectId idMon){
        //usuarioService.verificar("/getTokenUser",user)
        monopatinService.iniciarViaje("/inicioViaje", viajeDTO, idMon);
    }
    //actualizo un monopatin
    @PutMapping("/actualizar/{km}/{ubicacion}/{estado}/{id}")
    public void actualizarMonopatin( @PathVariable int km,@PathVariable String ubicacion,@PathVariable String estado,@PathVariable ObjectId id) {
       monopatinService.actulizarMonopatin(km, ubicacion, estado,id);
    }

    //elimino un monopatin
    @DeleteMapping("/eliminar/{id}")
    public String eliminarMonopatin(@PathVariable ObjectId id){
       return monopatinService.eliminarMonopatin(id);
    }

    @PutMapping("/finalizarViaje/{viajeId}")
    public ResponseEntity<ViajeDTO> finalizarViaje(@PathVariable int viajeId, @RequestBody ViajeDTO viajeDTO) {
        try {
            monopatinService.finalizarViaje("/finalizarViaje/{viajeId}", viajeDTO, viajeId);
            return new ResponseEntity<>(viajeDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("pausaViaje/{idViaje}")
    public ResponseEntity<ViajeDTO> pausarViaje(@PathVariable int idViaje, @RequestBody PausaDTO pausaDTO) {
        try {
            monopatinService.pausarViaje("/crearPausa/{idViaje}", pausaDTO, idViaje);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    ///
    @PutMapping("cancelarpausa/{pausaId}")
    public ResponseEntity<ViajeDTO> cancelarPausaEnViaje(@PathVariable int pausaId, @RequestBody PausaDTO pausaDTO) {
        try {
            monopatinService.cancelarPausaEnViaje("/cancelarPausa/{pausaId}", pausaDTO, pausaId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
