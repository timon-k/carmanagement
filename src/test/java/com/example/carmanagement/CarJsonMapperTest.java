package com.example.carmanagement;

import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CarJsonMapperTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void carPropertiesShouldDeserializeFromJson() throws JsonProcessingException {
        var inputJson = """
                        {
                            "brand": "Flexa",
                            "licensePlate": "L-CS8877E",
                            "manufacturer": "Carcorp",
                            "operationCity": "Newtown",
                            "status": "available"
                        }
                        """;
        var mappedResult = mapper.readValue(inputJson,
                                            new TypeReference<CompleteUserDefinedCarPropertiesDTO>() {});
        var expectedResult = new CompleteUserDefinedCarPropertiesDTO().brand("Flexa")
                                                                      .licensePlate("L-CS8877E")
                                                                      .manufacturer("Carcorp")
                                                                      .operationCity("Newtown")
                                                                      .status(UserDefinedCarPropertiesDTO.StatusEnum.AVAILABLE);

        assertEquals(expectedResult, mappedResult);
    }

    @Test
    public void carDtoShouldSerializeToJson() throws JsonProcessingException {
        var expectedJson = """
                           {
                               "id": 12345,
                               "brand": "Flexa",
                               "licensePlate": "L-CS8877E",
                               "manufacturer": "Carcorp",
                               "operationCity": "Newtown",
                               "status": "available",
                               "createdAt": "2022-04-15T10:23:47Z",
                               "lastUpdatedAt": "2022-04-15T10:23:47Z"
                           }
                           """;
        var car = new CarDTO()
                .id(BigDecimal.valueOf(12345))
                .brand("Flexa")
                .licensePlate("L-CS8877E")
                .manufacturer("Carcorp")
                .operationCity("Newtown")
                .status(CarDTO.StatusEnum.AVAILABLE)
                .createdAt(Instant.parse("2022-04-15T10:23:47Z"))
                .lastUpdatedAt(Instant.parse("2022-04-15T10:23:47Z"));

        assertEquals(mapper.readTree(expectedJson),
                     mapper.readTree(mapper.writeValueAsString(car)));
    }
}
