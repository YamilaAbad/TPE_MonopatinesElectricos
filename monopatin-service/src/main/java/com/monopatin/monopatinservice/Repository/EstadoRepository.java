package com.monopatin.monopatinservice.Repository;

import com.monopatin.monopatinservice.Model.Estado;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends MongoRepository<Estado, ObjectId> {


}
