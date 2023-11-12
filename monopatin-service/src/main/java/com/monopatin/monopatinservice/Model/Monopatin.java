package com.monopatin.monopatinservice.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Esta clase esta dedicada a la creación de la entidad monopatin
 */

@Document(value= "monopatin")
@Getter
@Setter
@NoArgsConstructor
public class Monopatin {
    @Id
    private ObjectId idMonopatin;
    private int km_totales;
    private String ubicacion;
    private int km_recorridos;
    private Estado estado;
    private int cantViajes;

}
