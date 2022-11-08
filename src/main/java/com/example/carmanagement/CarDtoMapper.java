package com.example.carmanagement;

import com.example.carmanagement.gen.model.CarDto;
import com.example.carmanagement.gen.model.CompleteUserDefinedCarPropertiesDto;
import com.example.carmanagement.gen.model.UserDefinedCarPropertiesDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarDtoMapper {

    CarDtoMapper INSTANCE = Mappers.getMapper(CarDtoMapper.class);

    CarDto carToCarDto(Car car);

    Car propertiesToNewCar(CompleteUserDefinedCarPropertiesDto carProps);

    void applyNewProperties(@MappingTarget Car existingCar,
                            UserDefinedCarPropertiesDto newProperties);
}
