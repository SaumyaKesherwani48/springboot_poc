package com.demoApp.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "carModel")
public class CarsModel {

    @Id
    private String id;
    private String name;
    private String manufacturerId;

    public CarsModel(String id, String name, String manufacturerId) {
        this.id = id;
        this.name = name;
        this.manufacturerId = manufacturerId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    @Override
    public String toString() {
        return "carsModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", manufacturerId='" + manufacturerId + '\'' +
                '}';
    }
}
