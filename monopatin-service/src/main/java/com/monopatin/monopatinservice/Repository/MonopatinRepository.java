package com.monopatin.monopatinservice.Repository;

import com.monopatin.monopatinservice.Model.Monopatin;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonopatinRepository extends MongoRepository<Monopatin, ObjectId> {
}
