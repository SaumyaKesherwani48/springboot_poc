package com.demoApp.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CarManufactureCustomeRepoImpl{

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map<String,Object>> findManufacturerByModel(String manufactureId) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(manufactureId));
        LookupOperation lookupOperation = Aggregation.lookup(
                "carModel",
                "_id",
                "manufacturerId",
                "models"
        );
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupOperation);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "carManufacture", Map.class);

        return results.getMappedResults().stream()
                .map(map -> (Map<String,Object>) map).toList();
    }
    
}
