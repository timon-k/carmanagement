package com.example.carmanagement;

import com.example.carmanagement.gen.model.CarArrayDto;
import com.example.carmanagement.gen.model.CarDto;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDto;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarManagementControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final CompleteUserDefinedCarPropertiesDto carToCreate =
            (CompleteUserDefinedCarPropertiesDto) new CompleteUserDefinedCarPropertiesDto().brand("Flexa")
                                                                                           .manufacturer("Carcorp")
                                                                                           .licensePlate("L-CS8877E")
                                                                                           .operationCity("Newtown")
                                                                                           .status(UserDefinedCarPropertiesDto.StatusEnum.AVAILABLE);

    private CarDto createdCar = null; // Test relies on order, no Optional here

    private final String idForWhichNoCarExists = "43";

    @Test
    @Order(1)
    public void shouldHaveNoCarsInitially() throws Exception {
        var result = this.mockMvc.perform(get("/cars"))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var carList = mapper.readValue(result, new TypeReference<CarArrayDto>() {});
        assertEquals(List.of(), carList.getItems());
    }

    @Test
    @Order(2)
    public void queryingNonExistingCarShouldYield404() throws Exception {
        this.mockMvc.perform(get("/cars/" + idForWhichNoCarExists))
                    .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void updatingNonExistingCarShouldYield404() throws Exception {
        this.mockMvc.perform(put("/cars/" + idForWhichNoCarExists).contentType(MediaType.APPLICATION_JSON)
                                                                  .content(mapper.writeValueAsString(new UserDefinedCarPropertiesDto())))
                    .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    public void creatingACarShouldWork() throws Exception {
        var postNewCar = post("/cars").contentType(MediaType.APPLICATION_JSON)
                                      .content(mapper.writeValueAsString(carToCreate))
                                      .characterEncoding("utf-8");
        var result = this.mockMvc.perform(postNewCar)
                                 .andExpect(status().isCreated())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        createdCar = mapper.readValue(result, new TypeReference<CarDto>() {});
        assertNotNull(createdCar.getId());
        assertEquals(carToCreate.getLicensePlate(), createdCar.getLicensePlate());
    }

    @Test
    @Order(5)
    public void createdCarShouldShowUpInCarList() throws Exception {
        var result = this.mockMvc.perform(get("/cars"))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var carList = mapper.readValue(result, new TypeReference<CarArrayDto>() {});
        assertEquals(1, carList.getItems()
                               .size());
        assertEquals(createdCar.getId(), carList.getItems()
                                                .get(0)
                                                .getId());
    }

    @Test
    @Order(6)
    public void queryingExistingCarShouldWork() throws Exception {
        var result = this.mockMvc.perform(get("/cars/" + createdCar.getId()))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        assertEquals(createdCar.getId(), mapper.readValue(result, new TypeReference<CarDto>() {})
                                               .getId());
    }

    @Test
    @Order(7)
    public void updatingExistingCarShouldWork() throws Exception {
        var update = new UserDefinedCarPropertiesDto().operationCity("Berlin");
        var updateRequest =
                put("/cars/" + createdCar.getId()).contentType(MediaType.APPLICATION_JSON)
                                                  .content(mapper.writeValueAsString(update));
        var result = this.mockMvc.perform(updateRequest)
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        var parsedResult = mapper.readValue(result, new TypeReference<CarDto>() {});
        assertEquals(update.getOperationCity(), parsedResult.getOperationCity());
        assertNotNull(parsedResult.getLastUpdatedAt());
    }
}
