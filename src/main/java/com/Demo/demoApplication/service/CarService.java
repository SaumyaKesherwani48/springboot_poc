package com.Demo.demoApplication.service;


import com.Demo.demoApplication.model.CarManufacturer;

import com.Demo.demoApplication.model.carsModel;
import com.Demo.demoApplication.repository.CarManufactureCustomRepo;
import com.Demo.demoApplication.repository.CarManufacturerRepo;
import com.Demo.demoApplication.repository.CarModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Autowired
    private CarManufacturerRepo carManufacturerRepo;

    @Autowired
    private CarModelRepo carModelRepo;

    @Autowired
    private CarManufactureCustomRepo customeRepo;

    private final RestTemplate restTemplate;

    public CarService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CarManufacturer saveCarDetails(CarManufacturer carManufacturer){
        return carManufacturerRepo.save(carManufacturer);
    }

    public carsModel saveCarModelDetails(carsModel carModel){
        return carModelRepo.save(carModel);
    }

    public List<CarManufacturer> getCarManufactureList (){
        return  carManufacturerRepo.findAll();
    }

    public List<Map<String, Object>> getCarModelByManufacture (String manufacturerId){
        return customeRepo.findManufacturerByModel(manufacturerId);
    }

    public carsModel getCarModelDetails(){
        String url = "http://localhost:8080/car/getCarModel";

        ResponseEntity<carsModel> response = restTemplate.getForEntity(url, carsModel.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

}
