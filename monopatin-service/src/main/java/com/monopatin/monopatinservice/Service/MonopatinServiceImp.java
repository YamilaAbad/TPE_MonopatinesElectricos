package com.monopatin.monopatinservice.Service;

import com.monopatin.monopatinservice.Controller.ParadaController;
import com.monopatin.monopatinservice.DTO.MonopatinDTO;
import com.monopatin.monopatinservice.DTO.PausaDTO;
import com.monopatin.monopatinservice.DTO.ViajeDTO;
import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Model.Parada;
import com.monopatin.monopatinservice.Repository.MonopatinRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class MonopatinServiceImp implements MonopatinService {

    @Value("${url_viaje}")
    private String url_viaje;
    @Value("${url_pausa}")
    private String url_pausa;


    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MonopatinRepository monopatinRepository;
    @Autowired
    ParadaController paradaController;
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
        if(id != null){
            Boolean result = monopatinRepository.existsById(id);
            if (result){
                monopatinRepository.deleteById(id);
                return "Eliminado exitosamente";
            }
            return "Id no valido";
        }
        return "No se proporcionó un id";
    }


    @Override
    public Optional<Monopatin> actulizarMonopatin(int km,String ubicacion, String estado, ObjectId id) {
        Optional<Monopatin> monopatin = monopatinRepository.findById(id);
        if (monopatin.isPresent()){
            Monopatin monopatinActual = monopatin.get();
            monopatinActual.setKm_totales(monopatinActual.getKm_totales()+km);
            monopatinActual.setUbicacion(ubicacion);
            monopatinActual.setKm_recorridos(monopatinActual.getKm_recorridos()+km);
            monopatinActual.getEstado().setEstado(estado);
            monopatinRepository.save(monopatinActual);
        }
        return monopatin;
    }

    @Override
    public Optional<Monopatin> obtenerMonopatin(ObjectId id) {
        Optional<Monopatin> monopatin=(monopatinRepository.findById(id));
        return monopatin;
    }

    @Override
    public List<Monopatin> listaMonopatines(){
        return monopatinRepository.findAll();
    }

    @Override
    public List<Monopatin> reporteMonopatinesPorKmR(int km) {
        List<Monopatin> monopatines = this.listaMonopatines();
        List<Monopatin> monopatineskms = new ArrayList<>();
        for (Monopatin m: monopatines){
            if (m.getKm_recorridos()>=km){
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
        for(Monopatin m:monopatines){
            if ("habilitado".equals(m.getEstado().getEstado())){
                cantFun++;
            }else if ("mantenimiento".equals(m.getEstado().getEstado())){
                cantMant++;
            }
        }
        return ("Monopatines habilitados: "+cantFun+"\nMonopatines en mantenimiento: "+cantMant);
    }

    @Override
    public List<MonopatinDTO> monopatinesCercanos(String ubicacion){
        //ubicacion del usuario
        List<MonopatinDTO> monopatinesCercanos = new ArrayList<>();
        String ubicacionUsuario = ubicacion;
        String[] coordenadas = ubicacionUsuario.split(",");
        double latitudUsuario = Double.parseDouble(coordenadas[0]);
        double longitudUsuario = Double.parseDouble(coordenadas[1]);
        double distanciaMaxima = 0.3; // Umbral de distancia en kilómetros (300mts)
        List<Monopatin> monopatines = this.listaMonopatines();

        for(Monopatin m: monopatines){
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

    /**
     * Metodo para iniciar el viaje desde el monopatin
     * @param viaje
     * @param viajeDto
     * @param idMon*/
    public void iniciarViaje(String viaje, ViajeDTO viajeDto, ObjectId idMon){
        Optional<Monopatin> monopatin = monopatinRepository.findById(idMon);
        Monopatin monopatinEnViaje = monopatin.orElse(null);
        if (monopatinEnViaje!=null){
            //Agrego viaje al monopatin
            int cant = monopatinEnViaje.getCantViajes();
            monopatinEnViaje.setCantViajes(cant+1);
            monopatinRepository.save(monopatinEnViaje);
            this.restTemplate.postForObject(this.url_viaje+viaje, viajeDto, ViajeDTO.class);
        }
    }

    /**
     * Metodo para finalizar el viaje desde el monopatim
     * @param viaje
     * @param viajeDTO
     * @param idViaje*/
    @Override
    public void finalizarViaje(String viaje, ViajeDTO viajeDTO, int idViaje) {
        //verifico si es una parada permitida para dejar el monopatin
        String ubicacion =viajeDTO.getDestinoDelViaje();
        Parada parada = paradaController.paradaExistente(ubicacion);
        String estadoDeLaParada= paradaController.estadoDeLaParada(parada, ubicacion);
        //caso de ser una parada permita dejara finalizar el viaje
        if("permitida".equals(estadoDeLaParada)){
            this.restTemplate.put(this.url_viaje +viaje, viajeDTO, idViaje);
        }
        //En caso contrario el metodo arrojará un error
    }




}
