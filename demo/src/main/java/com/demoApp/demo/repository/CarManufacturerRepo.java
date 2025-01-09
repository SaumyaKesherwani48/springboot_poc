package com.demoApp.demo.repository;

import com.demoApp.demo.model.CarManufacturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarManufacturerRepo extends MongoRepository<CarManufacturer, String> {

}
