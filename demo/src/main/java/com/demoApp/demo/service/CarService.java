package com.demoApp.demo.service;


import com.demoApp.demo.constants.CarConstants;
import com.demoApp.demo.model.CarManufacturer;

import com.demoApp.demo.model.CarsModel;
import com.demoApp.demo.repository.CarManufactureCustomRepo;
import com.demoApp.demo.repository.CarManufacturerRepo;
import com.demoApp.demo.repository.CarModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class CarService {

    // construtor dependency injection needs to lean more on this
    private final CarManufacturerRepo carManufacturerRepo;
    private final CarModelRepo carModelRepo;
    private final CarManufactureCustomRepo customeRepo;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final RestTemplate restTemplate;

    public CarService(CarManufacturerRepo carManufacturerRepo, CarModelRepo carModelRepo, CarManufactureCustomRepo customeRepo, KafkaTemplate<String, String> kafkaTemplate, RestTemplate restTemplate) {
        this.carManufacturerRepo = carManufacturerRepo;
        this.carModelRepo = carModelRepo;
        this.customeRepo = customeRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }

    public CarManufacturer saveCarDetails(CarManufacturer carManufacturer){
        return carManufacturerRepo.save(carManufacturer);
    }

    public CarsModel saveCarModelDetails(CarsModel carModel){
        CarsModel response = carModelRepo.save(carModel);
        if(response.getId().isEmpty()){
            kafkaTemplate.send(CarConstants.LOCATION_TOPIC,CarConstants.PRODUCER_FAILUER_MSG);
        }else{
            kafkaTemplate.send(CarConstants.LOCATION_TOPIC,CarConstants.PRODUCER_SUCCESS_MSG);
        }
        return response;
    }

    public List<CarManufacturer> getCarManufactureList (){
        return  carManufacturerRepo.findAll();
    }

    public List<Map<String, Object>> getCarModelByManufacture (String manufacturerId){
        return customeRepo.findManufacturerByModel(manufacturerId);
    }

}
