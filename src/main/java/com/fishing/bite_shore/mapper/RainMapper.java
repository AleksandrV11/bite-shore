package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.weatherData.weather.Rain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RainMapper {
    @Mapping(source = "threeH", target = "precipitation", qualifiedByName = "precipitationCast")
    Rain toRain(com.fishing.bite_shore.dto.dtoBase.Rain rain);

    @Named("precipitationCast")
    default int precipitationCast(double threeH) {
        return (int) Math.round(threeH);
    }
}
