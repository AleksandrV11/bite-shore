package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.dto.dtoBase.weather.CityDto;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface WeatherLocationMapper {
    // CityDto
    @Mapping(target = "id", ignore = true)

    @Mapping(source = "id", target = "externalId")
    @Mapping(source = "name", target = "name")

    @Mapping(source = "sunrise", target = "sunrise", qualifiedByName = "toDateTime")
    @Mapping(source = "sunset", target = "sunset", qualifiedByName = "toDateTime")

    // Class CoordDTO
    @Mapping(source = "coord.lat", target = "lat")
    @Mapping(source = "coord.lon", target = "lon")

    WeatherLocationEntity toWeatherLocation(CityDto cityDto);

    @Named("toDateTime")
    default LocalDateTime toDateTime(long unixSeconds) {
        return Instant.ofEpochSecond(unixSeconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
