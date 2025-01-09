package com.demoApp.demo.repository;

import com.demoApp.demo.model.carsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepo extends MongoRepository<carsModel, String> {

}
