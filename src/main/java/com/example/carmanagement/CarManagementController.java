package com.example.carmanagement;

import com.example.carmanagement.gen.api.CarsApi;
import com.example.carmanagement.gen.model.CarArrayDto;
import com.example.carmanagement.gen.model.CarDto;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDto;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDto;
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
    public ResponseEntity<CarDto> getCarById(Integer id) {
        return repo.findById(Long.valueOf(id))
                   .map((car) -> dtoMapper.carToCarDto(car))
                   .map((dto) -> new ResponseEntity<>(dto, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<CarDto> createCar(CompleteUserDefinedCarPropertiesDto completeUserDefinedCarPropertiesDto) {
        var newCar = dtoMapper.propertiesToNewCar(completeUserDefinedCarPropertiesDto);
        repo.save(newCar);
        return new ResponseEntity<>(dtoMapper.carToCarDto(newCar), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CarArrayDto> getCars() {
        List<CarDto> cars = StreamSupport.stream(repo.findAll()
                                                     .spliterator(), false)
                                         .map((car) -> dtoMapper.carToCarDto(car))
                                         .toList();
        return new ResponseEntity<>(new CarArrayDto().items(cars), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CarDto> updateCar(Integer id,
                                            UserDefinedCarPropertiesDto userDefinedCarPropertiesDto) {
        return repo.findById(Long.valueOf(id))
                   .map((existingCar) -> {
                       dtoMapper.applyNewProperties(existingCar, userDefinedCarPropertiesDto);
                       repo.save(existingCar);
                       return existingCar;
                   })
                   .map((car) -> dtoMapper.carToCarDto(car))
                   .map((dto) -> new ResponseEntity<>(dto, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
