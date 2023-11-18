package com.monopatin.monopatinservice.DTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class ViajeDTO {
    private int idViaje;
    private ObjectId idMonopatin;
    private String origenDelViaje;
    private String destinoDelViaje;
    private Time horaInicioViaje;
    private Time horaFinViaje;
    private Timestamp fechaDelViaje;
    private boolean isFinalizado;
    private double cobroViaje;
}
