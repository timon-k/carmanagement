package com.example.carmanagement;

import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarManagementControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final CompleteUserDefinedCarPropertiesDTO carToCreate =
            (CompleteUserDefinedCarPropertiesDTO) new CompleteUserDefinedCarPropertiesDTO().brand("Flexa")
                                                                                           .manufacturer("Carcorp")
                                                                                           .licensePlate("L-CS8877E")
                                                                                           .operationCity("Newtown")
                                                                                           .status(UserDefinedCarPropertiesDTO.StatusEnum.AVAILABLE);

    private CarDTO createdCar = null; // Test relies on order, no Optional here

    @Test
    @Order(1)
    public void shouldHaveNoCarsInitially() throws Exception {
        var result = this.mockMvc.perform(get("/cars"))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var carList = mapper.readValue(result, new TypeReference<List<CarDTO>>() {});
        assertEquals(List.of(), carList);
    }

    @Test
    @Order(1)
    public void queryingNonExistingCarShouldYield404() throws Exception {
        this.mockMvc.perform(get("/cars/foo"))
                    .andExpect(status().isNotFound());
    }

    @Test
    @Order(1)
    public void updatingNonExistingCarShouldYield404() throws Exception {
        this.mockMvc.perform(put("/cars/bar"))
                    .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    public void creatingACarShouldWork() throws Exception {
        var postNewCar = post("/cars").contentType(MediaType.APPLICATION_JSON)
                                      .content(mapper.writeValueAsString(carToCreate))
                                      .characterEncoding("utf-8");
        var result = this.mockMvc.perform(postNewCar)
                                 .andExpect(status().isCreated())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var parsedResponse = mapper.readValue(result, new TypeReference<CarDTO>() {});
        assertNotNull(parsedResponse.getId());
        assertEquals(carToCreate.getLicensePlate(), parsedResponse.getLicensePlate());
    }

    @Test
    @Order(3)
    public void createdCarShouldShowUpInCarList() throws Exception {
        var result = this.mockMvc.perform(get("/cars"))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var carList = mapper.readValue(result, new TypeReference<List<CarDTO>>() {});
        assertEquals(1, carList.size());
        createdCar = carList.get(0);
        assertEquals(carToCreate.getLicensePlate(), createdCar.getLicensePlate());
    }

    @Test
    @Order(3)
    public void queryingExistingCarShouldWork() throws Exception {
        assertNotNull(createdCar);
        var result = this.mockMvc.perform(get("/cars/" + createdCar.getId()))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        assertEquals(createdCar, mapper.readValue(result, new TypeReference<CarDTO>() {}));
    }

    @Test
    @Order(4)
    public void updatingExistingCarShouldWork() throws Exception {
        assertNotNull(createdCar);
        var update = new UserDefinedCarPropertiesDTO().operationCity("Berlin");
        var updateRequest =
                put("/cars/" + createdCar.getId()).content(mapper.writeValueAsString(update));
        var result = this.mockMvc.perform(updateRequest)
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var parsedResult = mapper.readValue(result, new TypeReference<CarDTO>() {});
        assertEquals(update.getOperationCity(), parsedResult.getOperationCity());
    }
}
