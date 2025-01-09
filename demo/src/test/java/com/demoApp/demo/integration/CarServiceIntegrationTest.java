package com.demoApp.demo.integration;

import com.demoApp.demo.model.CarManufacturer;
import com.demoApp.demo.model.carsModel;
import com.demoApp.demo.repository.CarManufacturerRepo;
import com.demoApp.demo.repository.CarModelRepo;
import com.demoApp.demo.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CarServiceIntegrationTest {
    @Autowired
    private CarService carService;

    @Autowired
    private CarManufacturerRepo carManufacturerRepo;

    @Autowired
    private CarModelRepo carModelRepo;

    @BeforeEach
    void setup() {
        carManufacturerRepo.deleteAll();
        carModelRepo.deleteAll();
    }

    @Test
    void testSaveAndFetchManufacturer() {
        // Save a manufacturer
        CarManufacturer manufacturer = new CarManufacturer("1", "Toyota");
        carService.saveCarDetails(manufacturer);

        // Fetch data
        List<CarManufacturer> manufacturers = carService.getCarManufactureList();
        assertEquals(1, manufacturers.size());
        assertEquals("Toyota", manufacturers.get(0).getName());
    }

    @Test
    void testGetCarModelsByManufacturer() {
        // Save test data
        CarManufacturer manufacturer = new CarManufacturer("1", "Toyota");
        carManufacturerRepo.save(manufacturer);

        carsModel carModel = new carsModel("101", "Nexon", "1");
        carModelRepo.save(carModel);

        // Fetch data
        List<Map<String, Object>> models = carService.getCarModelByManufacture("1");
        assertEquals(1, models.size());
        assertEquals("Toyota", models.get(0).get("name"));
    }
}
