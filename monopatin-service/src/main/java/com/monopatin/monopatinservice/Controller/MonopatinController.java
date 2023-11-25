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

    //!----------------------------------------- Acciones del monopatin ------------------------------------------------
    @GetMapping("/monopatines")//obtengo todos los monopatines
    public List<Monopatin> getAllMonopatines(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.listaMonopatines();
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    @GetMapping("/monopatin/{id}") //obtengo un monopatin
    public Optional<Monopatin> getMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable ObjectId id) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.obtenerMonopatin(id);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    @GetMapping("/monopatinesCercanos/{ubicacion}")//lista de monopatines cercanos a la ubicacion enviada por endpoint (ubicacion del usuario)
    public List<MonopatinDTO> monopatinesCercanos(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String ubicacion) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.monopatinesCercanos(ubicacion);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    @PostMapping("/crearMonopatin") //creo un nuevo monopatin
    public void crearMonopatin(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MonopatinDTO monopatin) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            monopatinService.guardarMonopatin(monopatin);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }
    @PutMapping("/actualizar/{km}/{ubicacion}/{estado}/{id}")   //actualizo un monopatin
    public void actualizarMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int km, @PathVariable String ubicacion, @PathVariable String estado, @PathVariable ObjectId id) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            monopatinService.actulizarMonopatin(km, ubicacion, estado, id);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }

    @DeleteMapping("/eliminar/{id}")//elimino un monopatin
    public String eliminarMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable ObjectId id) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.eliminarMonopatin(id);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }
    //!-------------- Relacion del microservicio de monopatin con el microservicio de Viaje ----------------------------
    @PostMapping("/iniciarViaje/{idMon}")//inicio de viaje
    public void iniciarViaje(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ViajeDTO viajeDTO, @PathVariable ObjectId idMon) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            monopatinService.iniciarViaje("/inicioViaje", viajeDTO, idMon, token);//usuarioService.verificar("/getTokenUser",user)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }
    @PutMapping("/finalizarViaje/{viajeId}")//finalizacion de viaje
    public ResponseEntity<ViajeDTO> finalizarViaje(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int viajeId, @RequestBody ViajeDTO viajeDTO) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            monopatinService.finalizarViaje("/finalizarViaje/{viajeId}", viajeDTO, viajeId, token);
            return new ResponseEntity<>(viajeDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/pausaViaje/{idViaje}") //Pausar viaje
    public ResponseEntity<ViajeDTO> pausarViaje(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int idViaje, @RequestBody PausaDTO pausaDTO) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            monopatinService.pausarViaje("/crearPausa/{idViaje}", pausaDTO, idViaje, token);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cancelarpausa/{pausaId}")//Cancelar pausa
    public ResponseEntity<ViajeDTO> cancelarPausaEnViaje(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int pausaId, @RequestBody PausaDTO pausaDTO) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            monopatinService.cancelarPausaEnViaje("/cancelarPausa/{pausaId}", pausaDTO, pausaId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/viajesPorAnio/{anio}")
    public List<ViajeDTO> consultarViajesPorAño(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int anio) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.consultarViajesPorAño("/viajesPorAnio/{anio}", anio, token);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            return null;
        }
    }
    @GetMapping("/monopatinesConMasDeXViajesEnAnio/{x}/{anio}")
    public List<Monopatin> obtenerMonopatin(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int anio, @PathVariable int x) {
        String token = authorizationHeader.replace("Bearer", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.obtenerMonopatinConMasViajesEnAnio(anio, token, x);
        } else {
            new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @GetMapping("/viajes")
    public List<ViajeDTO> obtenerTodasLosViajes(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.obtenerTodosLosViajes("/viajes", token);
        } else {
            new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @GetMapping("/viajesConPausas")
    public List<ViajeDTO> obtenerTodosLosViajeConPausas(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.obtenerTodosLosViajesConPausas("/viajesConPausas", token);
        } else {
            new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @GetMapping("/viajesSinPausas")
    public List<ViajeDTO> obtenerTodosLosViajesSinPausas(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.obtenerTodosLosViajesSinPausas("/viajeSinpausa", token);
        } else {
            new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    //!------------------------------------------------ Reportes -------------------------------------------------------
    //reporte de uso de monopatines por kilómetros recorridos
    @GetMapping("/reporteMonopatinesPorKmsRecorridos/{km}")
    public List<Monopatin> reporteMonopatinesPorKmR(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int km) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return monopatinService.reporteMonopatinesPorKmR(km);
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            return null;
        }
    }

    //reporte de la cantidad de monopatines en operación vs en mantenimiento
    @GetMapping("/reporteMonopatinesEstado")
    public ResponseEntity<String> cantidadDeMonopatinesEstados(@RequestHeader("Authorization") String authorizationHeader) {
        //extrae el token del encabezado de autorización
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            return ResponseEntity.ok(monopatinService.cantidadDeMonopatinesEstados());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }
    private String obtenerReporteMonopatines(String username) {
        // Realiza la lógica para obtener el reporte de monopatines según el usuario
        // Devuelve el reporte como un String
        return "Reporte de monopatines para el usuario: " + username;
    }
    @GetMapping("/reportePorKilometro/{kmParaMantenimiento}")
    public ResponseEntity<String> generarInformeUdoMonopatines(@RequestHeader("Authorization") String authorizationHeader,@PathVariable int kmParaMantenimiento, @RequestParam(required = false, defaultValue = "false") boolean incluirPausas) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtService.isTokenValid(token)) {
            String informe = String.valueOf(monopatinService.generarInformeUsoMonopatines("/reportePorKilometro/{kmParaMantenimiento}",kmParaMantenimiento, incluirPausas, token));
            return ResponseEntity.ok(informe);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
