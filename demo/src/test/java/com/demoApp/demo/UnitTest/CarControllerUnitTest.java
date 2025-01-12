package com.demoApp.demo.UnitTest;


import com.demoApp.demo.controller.CarController;
import com.demoApp.demo.model.CarManufacturer;
import com.demoApp.demo.model.CarsModel;
import com.demoApp.demo.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
//@AutoConfigureWireMock(port = 8080)
public class CarControllerUnitTest {

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @Mock
    private RestTemplate restTemplate;

    private WireMockServer wireMockServer;

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
        ResponseEntity<CarManufacturer> response = carController.createManufacturer(carManufacturer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("3",response.getBody().getId());
        assertEquals("tata",response.getBody().getName());

        verify(carService, times(1)).saveCarDetails(any(CarManufacturer.class));
    }

    @Test
    void testCreateCarModel() throws Exception {
        when(carService.saveCarModelDetails(carsModel)).thenReturn(carsModel);


        ResponseEntity<CarsModel> response = carController.createCarModel(carsModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123",response.getBody().getId());
        assertEquals("kia",response.getBody().getName());

        verify(carService, times(1)).saveCarModelDetails(any(CarsModel.class));
    }

    @Test
    void testGetManufacturerList() throws Exception {
        List<CarManufacturer> listOfManufacturer = List.of(carManufacturer);
        when(carService.getCarManufactureList()).thenReturn(listOfManufacturer);

        ResponseEntity<List<CarManufacturer>> response = carController.getManufacturerList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("3",response.getBody().get(0).getId());
        assertEquals("tata",response.getBody().get(0).getName());

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

        ResponseEntity<List<Map<String, Object>>> response = carController.getCarModelsByManuId(manufacturerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Tata", response.getBody().get(0).get("name"));
        assertEquals(1, ((List<?>) response.getBody().get(0).get("models")).size());

        verify(carService, times(1)).getCarModelByManufacture(manufacturerId);
    }

    // POC of Wiremock
    @Test
    public void testGetProductList() throws Exception{

        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();

        configureFor("localhost", 8080);

        stubFor(WireMock.get(urlEqualTo("/api/products"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[{\"id\":1,\"name\":\"Product1\"},{\"id\":2,\"name\":\"Product2\"}]")));

        when(restTemplate.exchange(
                eq("http://localhost:8080/api/products"),
                eq(HttpMethod.GET),
                isNull(),
                eq(String.class)))
                .thenReturn(ResponseEntity.ok("[{\"id\":1,\"name\":\"Product1\"},{\"id\":2,\"name\":\"Product2\"}]"));

       String response = carController.getProductList();

       assertNotNull(response);

     verify(restTemplate, times(1)).exchange(
             eq("http://localhost:8080/api/products"),
             eq(HttpMethod.GET),
             isNull(),
             eq(String.class));

    }

}
