package com.monopatin.monopatinservice.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "parada")
@Getter
@Setter
@NoArgsConstructor
public class Parada {

    @Id
    private ObjectId idParada;
    private String nombre;
    private String ubicacion;
    private Estado estado;
    private Monopatin monopatin;
}
