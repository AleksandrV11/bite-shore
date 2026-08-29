package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.dto.dtoBase.weather.WeatherDTO;
import com.fishing.bite_shore.dto.dtoBase.weather.WinListDTO;
import com.fishing.bite_shore.entity.WeatherDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherDataMapper {
    //WinListDto
    @Mapping(source = "dt_txt", target = "forecastTime", qualifiedByName = "castData")
    //MainDto
    @Mapping(source = "main.temp", target = "temperature", qualifiedByName = "tempToTemperature")
    @Mapping(source = "main.pressure", target = "pressure", qualifiedByName = "pressureCast")
    @Mapping(source = "main.humidity", target = "humidity")

    //WindDto
    @Mapping(source = "wind.deg", target = "windDirection")
    @Mapping(source = "wind.speed", target = "windSpeed")
    @Mapping(source = "wind.gust", target = "windGust")

    //RainDTO
    @Mapping(source = "rain.threeH", target = "precipitation", qualifiedByName = "precipitationCast")

    //WinListDTO вірогідність опадів
    @Mapping(source = "pop", target = "popPercent")
    //List<WeatherDto>weather
    @Mapping(source = "weather", target = "weatherCode", qualifiedByName = "weatherCode")
    @Mapping(source = "weather", target = "weatherGroup", qualifiedByName = "weatherGroup")
    @Mapping(source = "weather", target = "description", qualifiedByName = "description")
    @Mapping(source = "weather", target = "icon", qualifiedByName = "icon")

    WeatherDataEntity toWeatherData(WinListDTO winListDTO);

    @Named("castData")
    default LocalDateTime castData(String dt_txt) {
        if (dt_txt == null || dt_txt.isEmpty()) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dt_txt, dateTimeFormatter);
    }

    @Named("tempToTemperature")
    default int tempToTemperature(double temp) {
        return (int) Math.round(temp);
    }

    @Named("pressureCast")
    default int pressureCast(int pressure) {
        double temp = pressure * 0.75006375541921;
        return (int) Math.round(temp);
    }

    @Named("precipitationCast")
    default int precipitationCast(double threeH) {
        return (int) Math.round(threeH);
    }

    @Named("weatherCode")
    default int weatherCode(List<WeatherDTO> weather) {
        return (weather == null || weather.isEmpty())
                ? 0
                : weather.get(0).getId();
    }
    @Named("weatherGroup")
    default String weatherGroup(List<WeatherDTO> weather) {
        return (weather == null || weather.isEmpty())
                ? null
                : weather.get(0).getMain();
    }
    @Named("description")
    default String description(List<WeatherDTO> weather) {
        return (weather == null || weather.isEmpty())
                ? null
                : weather.get(0).getDescription();
    }
    @Named("icon")
    default String icon(List<WeatherDTO> weather) {
        return (weather == null || weather.isEmpty())
                ? null
                : weather.get(0).getIcon();
    }
}
