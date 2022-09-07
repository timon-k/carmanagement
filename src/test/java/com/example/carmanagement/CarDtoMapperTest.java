package com.example.carmanagement;

import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CarDtoMapperTest {

    private final CarDtoMapper mapper = CarDtoMapper.INSTANCE;

    @Test
    public void carPropertiesShouldMapToNewCar() {
        var props =
                (CompleteUserDefinedCarPropertiesDTO) new CompleteUserDefinedCarPropertiesDTO().brand("Foo")
                                                                                               .manufacturer("Bar")
                                                                                               .licensePlate("B-TE-123")
                                                                                               .operationCity("Berlin")
                                                                                               .status(UserDefinedCarPropertiesDTO.StatusEnum.OUT_OF_SERVICE);
        var result = mapper.propertiesToNewCar(props);
        assertNull(result.getId());
        assertEquals(props.getBrand(), result.getBrand());
    }

    private Car createCar() {
        return Car.builder()
                  .id(12345L)
                  .brand("Flexa")
                  .licensePlate("L-CS8877E")
                  .manufacturer("Carcorp")
                  .operationCity("Newtown")
                  .status(Car.Status.AVAILABLE)
                  .createdAt(Instant.parse("2022-04-15T10:23:47Z"))
                  .lastUpdatedAt(Instant.parse("2022-04-15T10:23:47Z"))
                  .build();
    }

    @Test
    public void carShouldMapToDto() {
        var car = createCar();
        var result = mapper.carToCarDto(car);
        assertEquals(car.getLicensePlate(), result.getLicensePlate());
        assertEquals(CarDTO.StatusEnum.AVAILABLE, result.getStatus());
        assertEquals(car.getLastUpdatedAt(), result.getLastUpdatedAt());
    }

    @Test
    public void propertiesShouldUpdateForPartialDtos() {
        var car = createCar();
        var changes = new UserDefinedCarPropertiesDTO().licensePlate("A-B-5");
        mapper.applyNewProperties(car, changes);
        assertEquals(changes.getLicensePlate(), car.getLicensePlate());
    }
}
