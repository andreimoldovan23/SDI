package mappers;

import domain.SensorMeasurement;
import dtos.SensorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorMapper extends DateMapper {

    @Mapping(source = "time", target = "time", qualifiedByName = "dateToString")
    SensorDTO entityToDTO(SensorMeasurement sensor);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "measurement", target = "measurement", ignore = true)
    @Mapping(source = "time", target = "time", ignore = true)
    SensorDTO entityToNameDto(SensorMeasurement sensor);

}
