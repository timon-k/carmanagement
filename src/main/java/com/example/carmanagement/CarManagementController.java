package com.example.carmanagement;

import com.example.carmanagement.gen.api.CarsApi;
import com.example.carmanagement.gen.model.CarArrayDTO;
import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
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

    private final CarDtoMapper dtoMapper = CarDtoMapper.INSTANCE;

    @Override
    public ResponseEntity<CarDTO> getCarById(Integer id) {
        return repo.findById(Long.valueOf(id))
                   .map((car) -> dtoMapper.carToCarDto(car))
                   .map((dto) -> new ResponseEntity<>(dto, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<CarDTO> createCar(CompleteUserDefinedCarPropertiesDTO completeUserDefinedCarPropertiesDTO) {
        var newCar = dtoMapper.propertiesToNewCar(completeUserDefinedCarPropertiesDTO);
        repo.save(newCar);
        return new ResponseEntity<>(dtoMapper.carToCarDto(newCar), HttpStatus.CREATED);
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
        return repo.findById(Long.valueOf(id))
                   .map((existingCar) -> {
                        dtoMapper.applyNewProperties(existingCar, userDefinedCarPropertiesDTO);
                        repo.save(existingCar);
                        return existingCar;
                   })
                   .map((car) -> dtoMapper.carToCarDto(car))
                   .map((dto) -> new ResponseEntity<>(dto, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
