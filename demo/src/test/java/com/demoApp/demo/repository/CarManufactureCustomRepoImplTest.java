package com.demoApp.demo.repository;//package com.Demo.demoApplication.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.aggregation.AggregationResults;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//
//
//import org.bson.Document;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@ExtendWith(MockitoExtension.class)
//public class CarManufactureCustomRepoImplTest {

//    @Mock
//    private MongoTemplate mongoTemplate;
//
//    @InjectMocks
//    private CarManufactureCustomeRepoImpl customeRepo;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testFindManufacturerByModel(){
//
//        //mock data
//        String manufactureId = "1";
//        List<Map<String, Object>> mockData = new ArrayList<>();
//        Map<String, Object> manufacturer = new HashMap<>();
//        manufacturer.put("_id", "1");
//        manufacturer.put("name", "Tata");
//        manufacturer.put("models", Arrays.asList(
//                Map.of("_id","101","name", "Nexon", "manufacturerId","1")
//        ));
//        mockData.add(manufacturer);
//
//        // Mock AggregationResults
//        AggregationResults<Map> mockResults = mock(AggregationResults.class);
//        when(mockResults.getMappedResults().stream().map(map -> (Map<String,Object>) map).toList()).thenReturn(mockData);
//
//        // mock mongoTemplate behavior
//        when(mongoTemplate.aggregate(any(), eq("carManufacture"), eq(Map.class))).thenReturn(mockResults);
//
//        // call the method
//        List<Map<String,Object>> result = customeRepo.findManufacturerByModel(manufactureId);
//
//        // verify results
//        assertNotNull(result);
//        assertEquals(1,result.size());
//        assertEquals("Tata",result.get(0).get("name"));
//        assertEquals(1,((List<?>) result.get(0).get("models")).size());
//        assertEquals("Nexon", ((Map<?,?>) ((List<?>) result.get(0).get("models")).get(0)).get("name"));
//
//        //verify interaction with mongoTemplate
//        verify(mongoTemplate,times(1)).aggregate(any(),eq("carManufacture"),eq(Map.class));
//
//    }
//
//}
