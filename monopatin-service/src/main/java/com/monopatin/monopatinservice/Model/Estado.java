package com.monopatin.monopatinservice.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Esta clase esta dedicada a la creación de la entidad estado
 *
 * hace referencia al estado actual que tendra el monopatin
 *
 * los estados podran ser:
 * - apagado
 * - mantenimiento
 * - uso
 * - pausa
 * - deshabilitado
 */

@Document(value = "estadoMonopatin")
@Getter
@Setter
@NoArgsConstructor
public class Estado {
    @Id
    private ObjectId idEstadoMonopatin;
    private String estado;
}
