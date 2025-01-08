package com.Demo.demoApplication.repository;

import com.Demo.demoApplication.model.carsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepo extends MongoRepository<carsModel, String> {
}
