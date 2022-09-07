package com.example.carmanagement;

import com.example.carmanagement.gen.model.CarDTO;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDTO;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarDtoMapper {

    CarDtoMapper INSTANCE = Mappers.getMapper(CarDtoMapper.class);

    CarDTO carToCarDto(Car car);

    Car propertiesToNewCar(CompleteUserDefinedCarPropertiesDTO carProps);

    void applyNewProperties(@MappingTarget Car existingCar,
                            UserDefinedCarPropertiesDTO newProperties);
}
