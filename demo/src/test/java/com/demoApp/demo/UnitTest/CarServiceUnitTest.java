package com.demoApp.demo.UnitTest;

import com.demoApp.demo.constants.CarConstants;
import com.demoApp.demo.model.CarManufacturer;
import com.demoApp.demo.model.CarsModel;
import com.demoApp.demo.repository.CarManufactureCustomRepo;
import com.demoApp.demo.repository.CarManufacturerRepo;
import com.demoApp.demo.repository.CarModelRepo;
import com.demoApp.demo.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CarServiceUnitTest {
    @Mock
    private CarManufacturerRepo carManufacturerRepo;

    @Mock
    private CarModelRepo carModelRepo;

    @Mock
    private CarManufactureCustomRepo customRepo;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CarService carService;

    @Test
    void testSaveCarDetails() {

        CarManufacturer carManufacturer = new CarManufacturer("1", "Tata");
        when(carManufacturerRepo.save(any(CarManufacturer.class))).thenReturn(carManufacturer);

        CarManufacturer result = carService.saveCarDetails(carManufacturer);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Tata", result.getName());
        verify(carManufacturerRepo, times(1)).save(carManufacturer);
    }

    @Test
    void testSaveCarModelDetails() {
        CarsModel carModel = new CarsModel("101", "Nexon", "1");
        when(carModelRepo.save(carModel)).thenReturn(carModel);

        CarsModel result = carService.saveCarModelDetails(carModel);

        assertNotNull(result);
        assertEquals("101", result.getId());
        assertEquals("Nexon", result.getName());
        verify(carModelRepo,times(1)).save(carModel);
        verify(kafkaTemplate, times(1)).send(CarConstants.LOCATION_TOPIC,CarConstants.PRODUCER_SUCCESS_MSG);
    }

    @Test
    void testGetCarManufacturerList() {
        List<CarManufacturer> manufacturers = Arrays.asList(
                new CarManufacturer("1", "Tata"),
                new CarManufacturer("2", "Kia")
        );
        when(carManufacturerRepo.findAll()).thenReturn(manufacturers);

        List<CarManufacturer> result = carService.getCarManufactureList();

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

        List<Map<String, Object>> result = carService.getCarModelByManufacture(manufacturerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tata", result.get(0).get("name"));
        assertEquals(1, ((List<?>) result.get(0).get("models")).size());
        verify(customRepo, times(1)).findManufacturerByModel(manufacturerId);
    }
}
