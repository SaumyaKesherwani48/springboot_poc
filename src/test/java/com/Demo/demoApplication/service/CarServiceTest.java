package com.Demo.demoApplication.service;

import com.Demo.demoApplication.model.CarManufacturer;
import com.Demo.demoApplication.model.carsModel;
import com.Demo.demoApplication.repository.CarManufactureCustomRepo;
import com.Demo.demoApplication.repository.CarManufacturerRepo;
import com.Demo.demoApplication.repository.CarModelRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarManufacturerRepo carManufacturerRepo;

    @Mock
    private CarModelRepo carModelRepo;

    @Mock
    private CarManufactureCustomRepo customRepo;

    @InjectMocks
    private CarService carService;

    @Test
    void testSaveCarDetails() {
        // Mock input and output
        CarManufacturer carManufacturer = new CarManufacturer("1", "Tata");
        when(carManufacturerRepo.save(carManufacturer)).thenReturn(carManufacturer);

        // Call the method
        CarManufacturer result = carService.saveCarDetails(carManufacturer);

        // Verify and assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Tata", result.getName());
        verify(carManufacturerRepo, times(1)).save(carManufacturer);
    }

    @Test
    void testSaveCarModelDetails() {
        // Mock input and output
        carsModel carModel = new carsModel("101", "Nexon", "1");
        when(carModelRepo.save(carModel)).thenReturn(carModel);

        // Call the method
        com.Demo.demoApplication.model.carsModel result = carService.saveCarModelDetails(carModel);

        // Verify and assert
        assertNotNull(result);
        assertEquals("101", result.getId());
        assertEquals("Nexon", result.getName());
        verify(carModelRepo, times(1)).save(carModel);
    }

    @Test
    void testGetCarManufacturerList() {
        // Mock input and output
        List<CarManufacturer> manufacturers = Arrays.asList(
                new CarManufacturer("1", "Tata"),
                new CarManufacturer("2", "Kia")
        );
        when(carManufacturerRepo.findAll()).thenReturn(manufacturers);

        // Call the method
        List<CarManufacturer> result = carService.getCarManufactureList();

        // Verify and assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tata", result.get(0).getName());
        verify(carManufacturerRepo, times(1)).findAll();
    }

    @Test
    void testGetCarModelByManufacturer() {
        // Mock input and output
        String manufacturerId = "1";
        List<Map<String, Object>> mockResult = new ArrayList<>();
        Map<String, Object> manufacturer = new HashMap<>();
        manufacturer.put("id", "1");
        manufacturer.put("name", "Tata");
        manufacturer.put("models", List.of(
                Map.of("id", "101", "name", "Nexon", "manufacturerId", "1")
        ));
        mockResult.add(manufacturer);

        when(customRepo.findManufacturerByModel(manufacturerId)).thenReturn(mockResult);

        // Call the method
        List<Map<String, Object>> result = carService.getCarModelByManufacture(manufacturerId);

        // Verify and assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tata", result.get(0).get("name"));
        assertEquals(1, ((List<?>) result.get(0).get("models")).size());
        verify(customRepo, times(1)).findManufacturerByModel(manufacturerId);
    }
}