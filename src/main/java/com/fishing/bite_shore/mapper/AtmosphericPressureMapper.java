package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.dto.dtoBase.Main;
import com.fishing.bite_shore.weatherData.weather.AtmosphericPressure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AtmosphericPressureMapper {
    @Mapping(source = "sea_level", target = "pressure", qualifiedByName = "pressureCast")
    AtmosphericPressure toAtmosphericPressure(Main main);

    @Named("pressureCast")
    default int pressureCast(int pressure) {
        double temp = pressure * 0.75006375541921;
        return (int) Math.round(temp);
    }
}
