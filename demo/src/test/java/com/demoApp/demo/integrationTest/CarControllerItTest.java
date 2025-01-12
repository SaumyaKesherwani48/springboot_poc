package com.demoApp.demo.integrationTest;

import com.demoApp.demo.controller.CarController;
import com.demoApp.demo.model.CarManufacturer;
import com.demoApp.demo.model.CarsModel;
import com.demoApp.demo.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8080)
public class CarControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    private CarsModel carsModel;

    private CarManufacturer carManufacturer;

    @BeforeEach
    public void setup() {
        carsModel = new CarsModel("123","kia","3");
        carManufacturer = new CarManufacturer("3","tata");
    }

    @Test
    void testCreateManufacturer() throws Exception {

        when(carService.saveCarDetails(carManufacturer)).thenReturn(carManufacturer);
        mockMvc.perform(post("/car/createManufacturer")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(carManufacturer)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tata"));

        verify(carService, times(1)).saveCarDetails(any(CarManufacturer.class));
    }

    @Test
    void testCreateCarModel() throws Exception {
        when(carService.saveCarModelDetails(carsModel)).thenReturn(carsModel);

        mockMvc.perform(post("/car/createCarModel")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(carsModel)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("kia"));

        verify(carService, times(1)).saveCarModelDetails(any(CarsModel.class));
    }

    @Test
    void testGetManufacturerList() throws Exception {
        List<CarManufacturer> listOfManufacturer = List.of(carManufacturer);
        when(carService.getCarManufactureList()).thenReturn(listOfManufacturer);

        mockMvc.perform(MockMvcRequestBuilders.get("/car/getManufacturerList")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("tata"));

        verify(carService, times(1)).getCarManufactureList();
    }

    @Test
    public void testGetCarModelsByManuId() throws Exception{
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
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tata"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].models[0].name").value("Nexon"));

        verify(carService, times(1)).getCarModelByManufacture(manufacturerId);
    }

    // POC of Wiremock

    @Test
    public void testGetProductList() throws Exception{
        stubFor(WireMock.get(urlEqualTo("/api/products"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[{\"id\":1,\"name\":\"Product1\"},{\"id\":2,\"name\":\"Product2\"}]")));

                mockMvc.perform(get("/car/getCarDetails"))
                .andExpect(status().isOk());
        WireMock.verify(getRequestedFor(urlEqualTo("/api/products")));

    }


}
