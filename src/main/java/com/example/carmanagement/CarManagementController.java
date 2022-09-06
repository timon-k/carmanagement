package com.example.carmanagement;

import com.example.carmanagement.gen.api.CarsApi;
import com.example.carmanagement.gen.model.CarArrayDTO;
import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarManagementController implements CarsApi {

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
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<CarDTO> updateCar(Integer id, UserDefinedCarPropertiesDTO userDefinedCarPropertiesDTO) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
