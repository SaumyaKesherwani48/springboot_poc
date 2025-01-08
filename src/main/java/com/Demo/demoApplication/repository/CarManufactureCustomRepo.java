package com.Demo.demoApplication.repository;

import com.Demo.demoApplication.model.CarManufacturer;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CarManufactureCustomRepo extends MongoRepository<CarManufacturer, String> {

    @Aggregation(pipeline = {
            "{$lookup :{from: 'carModel', localField: '_id', foreignField: 'manufacturerId', as: 'models' }}}",
            "{$match : {'_id' : ?0}}",
            "{$project : {_id:1,name:1,models:1}}"
    })
    List<Map<String, Object>> findManufacturerByModel(String manufactureId);
}
