package com.demoApp.demo.controller;


import com.demoApp.demo.model.CarManufacturer;
import com.demoApp.demo.model.carsModel;
import com.demoApp.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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
    public CarManufacturer createManufacturer(@RequestBody CarManufacturer carManufacturer){
        return carService.saveCarDetails(carManufacturer);
    }

    @PostMapping("/createCarModel")
    public carsModel createCarModel(@RequestBody carsModel carModel){
        return carService.saveCarModelDetails(carModel);
    }

    @GetMapping("/getManufacturerList")
    public List<CarManufacturer> getManufacturerList(){
        return carService.getCarManufactureList();
    }

    @GetMapping("/getCarModelsByManuId/{manufacturerId}")
    public List<Map<String, Object>> getCarModelsByManuId(@PathVariable String manufacturerId){
        return carService.getCarModelByManufacture(manufacturerId);
    }

//    @GetMapping("/getCarModel")
//    public carsModel getCarModel(){
//        return carService.getCarModelDetails();
//    }


    @RequestMapping(value = "/ignek/products")
    public String getProductList() {
        return restTemplate
                .exchange("http://localhost:8080/api/products",
                        HttpMethod.GET,null,String.class).getBody();
    }
}
