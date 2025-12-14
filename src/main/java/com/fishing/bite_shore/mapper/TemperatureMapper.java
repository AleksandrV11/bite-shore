package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.dto.dtoBase.Main;
import com.fishing.bite_shore.weatherData.weather.Temperature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TemperatureMapper {

   @Mapping(source = "temp",target = "temperature",qualifiedByName = "tempToTemperature")
    Temperature toTemperature(Main main);

    @Named("tempToTemperature")
    default int tempToTemperature(double temp) {
        return (int) Math.round(temp);
    }
}
