package com.Demo.demoApplication.controller;

import com.Demo.demoApplication.model.CarManufacturer;
import com.Demo.demoApplication.model.carsModel;
import com.Demo.demoApplication.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    CarManufacturer RECORD_1 = new CarManufacturer("1","Tata");
    CarManufacturer RECORD_2 = new CarManufacturer("2", "Kia");

    @Test
    void testCreateCarManufacturer() throws Exception {

        CarManufacturer carManufacturer = new CarManufacturer("1", "Tata");
        when(carService.saveCarDetails(carManufacturer)).thenReturn(carManufacturer);

        mockMvc.perform(post("/car/createManufacturer")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(carManufacturer)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tata"));

        verify(carService, times(1)).saveCarDetails(any(CarManufacturer.class));
    }

    @Test
    void testCreateCarModel() throws Exception {
        carsModel carModel = new carsModel("101", "Nexon", "1");
        when(carService.saveCarModelDetails(any(carsModel.class))).thenReturn(carModel);

        mockMvc.perform(post("/car/createCarModel")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(carModel)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("101"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Nexon"));

        verify(carService, times(1)).saveCarModelDetails(any(carsModel.class));
    }



    @Test
    public void testGetCarManufacturerList() throws Exception {
        List<CarManufacturer> manufacturers = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2));

        Mockito.when(carService.getCarManufactureList()).thenReturn(manufacturers);

        mockMvc.perform(MockMvcRequestBuilders.get("/car/getManufacturerList")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tata"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Kia"));

        verify(carService, times(1)).getCarManufactureList();
    }

    @Test
    void testGetCarModelsByManufacturer() throws Exception {
        String manufacturerId = "1";
        List<Map<String, Object>> mockResult = new ArrayList<>();
        Map<String, Object> manufacturer = new HashMap<>();
        manufacturer.put("id", "1");
        manufacturer.put("name", "Tata");
        manufacturer.put("models", List.of(
                Map.of("id", "101", "name", "Nexon", "manufacturerId", "1")
        ));
        mockResult.add(manufacturer);

        when(carService.getCarModelByManufacture(manufacturerId)).thenReturn(mockResult);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/car/getCarModelsByManuId/{manufacturerId}", manufacturerId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tata"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].models[0].name").value("Nexon"));

        verify(carService, times(1)).getCarModelByManufacture(manufacturerId);
    }


}
