package com.monopatin.monopatinservice.Repository;

import com.monopatin.monopatinservice.Model.Parada;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadaRepository extends MongoRepository<Parada, ObjectId> {
}
