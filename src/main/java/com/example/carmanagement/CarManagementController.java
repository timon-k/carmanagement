package com.example.carmanagement;

import com.example.carmanagement.gen.api.CarsApi;
import com.example.carmanagement.gen.model.CarArrayDTO;
import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
public class CarManagementController implements CarsApi {

    @Autowired
    private CarRepository repo;

    private final CarMapper dtoMapper = CarMapper.INSTANCE;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public ResponseEntity<CarDTO> getCarById(Integer id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<CarDTO> createCar(CompleteUserDefinedCarPropertiesDTO completeUserDefinedCarPropertiesDTO) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<CarArrayDTO> getCars() {
        List<CarDTO> cars = StreamSupport.stream(repo.findAll()
                                                     .spliterator(), false)
                                         .map((car) -> dtoMapper.carToCarDto(car))
                                         .toList();
        return new ResponseEntity<>(new CarArrayDTO().items(cars), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CarDTO> updateCar(Integer id,
                                            UserDefinedCarPropertiesDTO userDefinedCarPropertiesDTO) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
