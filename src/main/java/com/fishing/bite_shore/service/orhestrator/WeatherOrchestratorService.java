package com.fishing.bite_shore.service.orhestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.weather.DTOBase;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.model.WeatherProcessResult;
import com.fishing.bite_shore.repository.WeatherLocationRepository;
import com.fishing.bite_shore.service.facade.WeatherFacadeService;
import com.fishing.bite_shore.service.gettingJSONData.ElevationApiClient;
import com.fishing.bite_shore.service.parser.WeatherService;
import com.fishing.bite_shore.service.storage.WeatherStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherOrchestratorService {
    private final WeatherStorageService storageService;
    private final WeatherService weatherService;
    private final WeatherFacadeService facadeService;
    private final WeatherLocationRepository locationRepository;
    private final ElevationApiClient elevationApiClient;

    @Transactional

    public WeatherProcessResult getAndSaveByCity(String city) throws JsonProcessingException {

        DTOBase dto = weatherService.getParsDTOCity(city);
     //   System.out.println("CITY DTO = " + dto);
        WeatherLocationEntity location = facadeService.buildWeather(dto);
        //додаємо висоту
        fillElevation(location);
        // INSERT або UPDATE weather_location
        location = storageService.upsertLocation(location);   // знаходимо висоту
        // INSERT або UPDATE weather_data
        storageService.upsertData(
                location.getId(),
                location.getWeatherData()
        );

        return new WeatherProcessResult(dto, location.getId());
    }

    @Transactional
    public WeatherProcessResult getAndSaveByCoord(double lat, double lon) throws JsonProcessingException {

        DTOBase dto = weatherService.getParsDTOCoord(lat, lon);
        WeatherLocationEntity location = facadeService.buildWeather(dto);
        //знаходжу висоту
        fillElevation(location);
        // INSERT або UPDATE weather_location
        location = storageService.upsertLocation(location);
        // INSERT або UPDATE weather_data
        storageService.upsertData(
                location.getId(),
                location.getWeatherData()
        );

        return new WeatherProcessResult(dto, location.getId());
    }

    public void fillElevation(WeatherLocationEntity location) {
        if (location.getElevation() == null) {
            Double elevation = elevationApiClient
                    .getElevation(location.getLat(), location.getLon())
                    .getElevationValue();
            location.setElevation(elevation);
        }
    }

}
