package com.fishing.bite_shore.service.facade;

import com.fishing.bite_shore.dto.dtoBase.weather.DTOBase;
import com.fishing.bite_shore.entity.WeatherDataEntity;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.mapper.WeatherDataMapper;
import com.fishing.bite_shore.mapper.WeatherLocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherFacadeService {
    private final WeatherLocationMapper weatherLocationMapper;
    private final WeatherDataMapper weatherDataMapper;

    public WeatherLocationEntity buildWeather(DTOBase dtoBase) {
        WeatherLocationEntity weatherLocation = weatherLocationMapper.toWeatherLocation(dtoBase.getCity());
        weatherLocation.setReceivedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        List<WeatherDataEntity> weatherDataEntities =
                dtoBase.getList().stream().map(weatherDataMapper::toWeatherData).collect(Collectors.toList());
        weatherDataEntities.forEach(w -> w.setLocation(weatherLocation));
        weatherLocation.setWeatherData(weatherDataEntities);

        return weatherLocation;
    }
}
