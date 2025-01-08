package com.Demo.demoApplication.repository;

import com.Demo.demoApplication.model.CarManufacturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarManufacturerRepo extends MongoRepository<CarManufacturer, String> {
}
