package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.DTO.MonopatinDTO;
import com.monopatin.monopatinservice.DTO.PausaDTO;
import com.monopatin.monopatinservice.DTO.ViajeDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Repository.MonopatinRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonopatinServiceImp implements MonopatinService {
    @Value("${url_viaje}")
    private String url_viaje;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MonopatinRepository monopatinRepository;

    @Autowired
    ParadaService paradaService;
    private final WebClient webClient;

    public MonopatinServiceImp(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/service_viaje/").build();
    }
    //!----------------------------------------- Acciones del monopatin ------------------------------------------------
    @Override
    public void guardarMonopatin(MonopatinDTO monopatinDTO) {
        Monopatin monopatin = new Monopatin();
        monopatin.setKm_totales(monopatinDTO.getKm_totales());
        monopatin.setUbicacion(monopatinDTO.getUbicacion());
        monopatin.setKm_recorridos(monopatinDTO.getKm_recorridos());
        monopatin.setEstado(monopatinDTO.getEstado());
        monopatinRepository.insert(monopatin);
    }

    @Override
    public String eliminarMonopatin(ObjectId id) {
        if (id != null) {
            Boolean result = monopatinRepository.existsById(id);
            if (result) {
                monopatinRepository.deleteById(id);
                return "Eliminado exitosamente";
            }
            return "Id no valido";
        }
        return "No se proporcionó un id";
    }

    @Override
    public Optional<Monopatin> actulizarMonopatin(int km, String ubicacion, String estado, ObjectId id) {
        Optional<Monopatin> monopatin = monopatinRepository.findById(id);
        if (monopatin.isPresent()) {
            Monopatin monopatinActual = monopatin.get();
            monopatinActual.setKm_totales(monopatinActual.getKm_totales() + km);
            monopatinActual.setUbicacion(ubicacion);
            monopatinActual.setKm_recorridos(monopatinActual.getKm_recorridos() + km);
            monopatinActual.getEstado().setEstado(estado);
            monopatinRepository.save(monopatinActual);
        }
        return monopatin;
    }

    @Override
    public Optional<Monopatin> obtenerMonopatin(ObjectId id) {
        Optional<Monopatin> monopatin = (monopatinRepository.findById(id));
        return monopatin;
    }

    @Override
    public List<Monopatin> listaMonopatines() {
        return monopatinRepository.findAll();
    }

    //!-------------- Relacion del microservicio de monopatin con el microservicio de Viaje ----------------------------
    /**
     * Metodo para inicia el viaje
     * @param viaje
     * @param viajeDto
     * @param idMon
     * @param token
     */
    public void iniciarViaje(String viaje, ViajeDTO viajeDto, ObjectId idMon, String token) {
        // Llamada al servicio de viaje para guardar el viaje
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<ViajeDTO> requestEntity = new HttpEntity<>(viajeDto, headers);
        WebClient webClient = WebClient.create(this.url_viaje + viaje);
        try {
            webClient.post()
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(Mono.just(viajeDto), ViajeDTO.class)
                    .retrieve()
                    .toBodilessEntity()
                    .block();  // Esperar a que la llamada al servicio de viaje se complete

            incrementarCantidadViajes(idMon);// Incrementar la cantidad de viajes en el monopatín
        } catch (WebClientResponseException ex) {
            // Manejar excepciones específicas, como 403 Forbidden
            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new RuntimeException("Acceso denegado al servicio de viaje");
            } else {
                throw new RuntimeException("Error al llamar al servicio de viaje: " + ex.getRawStatusCode());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error general al llamar al servicio de viaje: " + ex.getMessage());
        }
    }

    /**
     * Metodo para finalizar el viaje
     * @param viaje
     * @param viajeDTO
     * @param idViaje
     * @param token*/
    @Override
    public void finalizarViaje(String viaje, ViajeDTO viajeDTO, int idViaje, String token) {
        // Verificar si es una parada permitida para dejar el monopatín
        String ubicacion = viajeDTO.getDestinoDelViaje();
        Parada parada = paradaService.paradaExistente(ubicacion);
        String estadoDeLaParada = paradaService.estadoDeLaParadaActual(parada, ubicacion);

        // Caso de ser una parada permitida para finalizar el viaje
        if ("permitida".equals(estadoDeLaParada)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<ViajeDTO> requestEntity = new HttpEntity<>(viajeDTO, headers);
            restTemplate.put(this.url_viaje + viaje, requestEntity, idViaje);
        } else {
            // En caso contrario, puedes manejar el error de alguna manera
            throw new RuntimeException("No se puede finalizar el viaje en una parada no permitida");
        }
    }

    /**
     * Incremento de los viajes en el monopatin
     * @param idMon*/
    private void incrementarCantidadViajes(ObjectId idMon) {
        // Verificar si el monopatín existe
        Monopatin monopatinEnViaje = monopatinRepository.findById(idMon).orElse(null);

        // Incrementar la cantidad de viajes en el monopatín
        if (monopatinEnViaje != null) {
            monopatinEnViaje.setCantViajes(monopatinEnViaje.getCantViajes() + 1);
            monopatinRepository.save(monopatinEnViaje);  // Guardar los cambios en la base de datos
        }
    }

    @Override
    public void pausarViaje(String viaje, PausaDTO pausaDTO, int idViaje, String token) {
        this.restTemplate.postForObject(this.url_viaje + viaje, pausaDTO, Void.class, idViaje);
    }

    @Override
    public void cancelarPausaEnViaje(String viaje, PausaDTO pausaDTO, int pausaId) {
        String url = this.url_viaje + "/cancelarPausa/{pausaId}";
        this.restTemplate.put(url, pausaDTO, pausaId);
    }

    @Override
    public List<ViajeDTO> consultarViajesPorAño(String endpoint, int anio, String token) {
        String url = this.url_viaje + endpoint.replace("{anio}", String.valueOf(anio));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<ViajeDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<ViajeDTO>>() {}
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            // Manejar otros códigos de estado según sea necesario
            return null;
        }
    }

    @Override
    public List<Monopatin> obtenerMonopatinConMasViajesEnAnio(int anio, String token, int x) {
        // Llamada al microservicio de viaje para obtener todos los viajes en un año específico
        List<ViajeDTO> viajes = consultarViajesPorAño("/viajesPorAnio/{anio}", anio, token);
        List<Monopatin>monopatines = listaMonopatines();
        List<Monopatin>filtrado = new ArrayList<>();

        for(Monopatin monopatin: monopatines){
            if(monopatin.getCantViajes()> x){
                for(ViajeDTO viajeDTO : viajes){
                    if(!filtrado.contains(monopatin)){
                        filtrado.add(monopatin);
                    }
                }
            }
        }
        return filtrado;
    }
    @Override
    public List<ViajeDTO>obtenerTodosLosViajes(String viaje, String token) {
        String url = this.url_viaje+viaje;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<ViajeDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ViajeDTO>>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public List<ViajeDTO> obtenerTodosLosViajesConPausas(String viaje, String token) {
        String url = this.url_viaje+viaje;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<ViajeDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ViajeDTO>>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public List<ViajeDTO> obtenerTodosLosViajesSinPausas(String viaje, String token) {
        String url = this.url_viaje+viaje;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<ViajeDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<ViajeDTO>>() {}
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
    //!------------------------------------------------ Reportes -------------------------------------------------------
    @Override
    public List<Monopatin> reporteMonopatinesPorKmR(int km) {
        List<Monopatin> monopatines = this.listaMonopatines();
        List<Monopatin> monopatineskms = new ArrayList<>();
        for (Monopatin m : monopatines) {
            if (m.getKm_recorridos() >= km) {
                monopatineskms.add(m);
            }
        }
        return monopatineskms;
    }

    @Override
    public String cantidadDeMonopatinesEstados() {
        List<Monopatin> monopatines = this.listaMonopatines();
        int cantMant = 0;
        int cantFun = 0;
        for (Monopatin m : monopatines) {
            if ("habilitado".equals(m.getEstado().getEstado())) {
                cantFun++;
            } else if ("mantenimiento".equals(m.getEstado().getEstado())) {
                cantMant++;
            }
        }
        return ("Monopatines habilitados: " + cantFun + "\nMonopatines en mantenimiento: " + cantMant);
    }

    @Override
    public List<MonopatinDTO> monopatinesCercanos(String ubicacion) {
        //ubicacion del usuario
        List<MonopatinDTO> monopatinesCercanos = new ArrayList<>();
        String ubicacionUsuario = ubicacion;
        String[] coordenadas = ubicacionUsuario.split(",");
        double latitudUsuario = Double.parseDouble(coordenadas[0]);
        double longitudUsuario = Double.parseDouble(coordenadas[1]);
        double distanciaMaxima = 0.3; // Umbral de distancia en kilómetros (300mts)
        List<Monopatin> monopatines = this.listaMonopatines();

        for (Monopatin m : monopatines) {
            String[] coordenadaMonopatin = m.getUbicacion().split(",");
            double latitudMonopatin = Double.parseDouble(coordenadaMonopatin[0]);
            double longitudMonopatin = Double.parseDouble(coordenadaMonopatin[1]);

            double distancia = calcularDistancia(latitudUsuario, longitudUsuario, latitudMonopatin, longitudMonopatin);
            if (distancia <= distanciaMaxima) {
                MonopatinDTO monopatin = new MonopatinDTO();
                monopatin.setKm_totales(m.getKm_totales());
                monopatin.setUbicacion(m.getUbicacion());
                monopatin.setKm_recorridos(m.getKm_recorridos());
                monopatin.setEstado(m.getEstado());

                monopatinesCercanos.add(monopatin);
            }
        }
        return monopatinesCercanos;
    }

    private double calcularDistancia(double latitudUsuario, double longitudUsuario, double latitudMonopatin, double longitudMonopatin) {
        int radioTierra = 6371; // Radio de la Tierra en kilómetros

        double dLat = Math.toRadians(latitudMonopatin - latitudUsuario);
        double dLon = Math.toRadians(longitudMonopatin - longitudUsuario);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitudUsuario)) * Math.cos(Math.toRadians(latitudMonopatin)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = radioTierra * c;

        return distancia; // Distancia en kilómetros
    }
    @Override
    public ResponseEntity<String> generarInformeUsoMonopatines( String viaje, int kmParaMantenimiento, boolean incluirPausas, String token) {
        StringBuilder informe = new StringBuilder();
        List<ViajeDTO> viajes = obtenerTodosLosViajes("/viajes", token);
        List<Monopatin> monopatines = listaMonopatines();
        for (Monopatin monopatin : monopatines) {
            int kmRecorridos = monopatin.getKm_recorridos();
            String estado = monopatin.getEstado().getEstado();
            // Verificar si el monopatín necesita mantenimiento
            boolean necesitaMantenimiento = kmRecorridos >= kmParaMantenimiento;
            // Lógica para determinar si se deben incluir pausas o no
            List<ViajeDTO> viajesMonopatin = incluirPausas ? obtenerTodosLosViajesSinPausas("/viajesSinPausas", token)
                    : obtenerTodosLosViajesConPausas("/viajesConPausas", token);
            informe.append("Monopatin ID: ").append(monopatin.getIdMonopatin()).append("\n");
            informe.append("Kilometros recorridos: ").append(kmRecorridos).append("\n");
            informe.append("Estado: ").append(estado).append("\n");
            // Agregar información de mantenimiento si es necesario
            if (necesitaMantenimiento) {
                informe.append("¡Este monopatín necesita mantenimiento!\n");
            }

            // Agregar información de cada viaje al informe
            for (ViajeDTO viajeDTO : viajesMonopatin) {
                informe.append("Origen: ").append(viajeDTO.getOrigenDelViaje()).append("\n");
                informe.append("Destino: ").append(viajeDTO.getDestinoDelViaje()).append("\n");
                // Agregar más detalles según sea necesario
                informe.append("\n");
            }
        }
        // Construir un ResponseEntity con el informe y el código de estado OK
        return ResponseEntity.ok(informe.toString());
    }




}