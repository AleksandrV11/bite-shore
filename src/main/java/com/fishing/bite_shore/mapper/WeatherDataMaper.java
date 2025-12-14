package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.dto.dtoBase.WinList;
import com.fishing.bite_shore.weatherData.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {
        AtmosphericPressureMapper.class, RainMapper.class, TemperatureMapper.class, WindMapper.class
})
public interface WeatherDataMaper {
    @Mapping(source = "dt_txt", target = "localDateTime", qualifiedByName = "castData")
    @Mapping(source = "main", target = "atmosphericPressure")
    @Mapping(source = "rain", target = "rain")
    @Mapping(source = "main", target = "temperature")
    @Mapping(source = "wind", target = "wind")
    WeatherData toWeatherData(WinList dto);

    @Named("castData")
    default LocalDateTime castData(String dt_txt) {
        if (dt_txt == null || dt_txt.isEmpty()) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dt_txt, dateTimeFormatter);
    }
}
