package com.demoApp.demo.controller;


import com.demoApp.demo.model.CarManufacturer;
import com.demoApp.demo.model.CarsModel;
import com.demoApp.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/createManufacturer")
    public ResponseEntity<CarManufacturer> createManufacturer(@RequestBody CarManufacturer carManufacturer) throws Exception{
        return new ResponseEntity<>(carService.saveCarDetails(carManufacturer), HttpStatus.CREATED);
    }

    // Kafka POC
    @PostMapping("/createCarModel")
    public ResponseEntity<CarsModel> createCarModel(@RequestBody CarsModel carModel) throws Exception{
        return new ResponseEntity<>(carService.saveCarModelDetails(carModel),HttpStatus.CREATED);
    }

    @GetMapping("/getManufacturerList")
    public ResponseEntity<List<CarManufacturer>> getManufacturerList() throws Exception{
        return new ResponseEntity<>(carService.getCarManufactureList(),HttpStatus.OK);
    }

    // POC to implement joins in mongodb
    @GetMapping("/getCarModelsByManuId/{manufacturerId}")
    public ResponseEntity<List<Map<String, Object>>> getCarModelsByManuId(@PathVariable String manufacturerId) throws Exception{
        return new ResponseEntity<>(carService.getCarModelByManufacture(manufacturerId),HttpStatus.OK);
    }

    // POC of Wiremock
    @RequestMapping(value = "/getCarDetails")
    public String getProductList() throws Exception{
        return restTemplate
                .exchange("http://localhost:8080/api/products",
                        HttpMethod.GET,null,String.class).getBody();
    }
}
