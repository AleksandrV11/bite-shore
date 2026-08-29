package com.fishing.bite_shore.service.storage;

import com.fishing.bite_shore.entity.WeatherDataEntity;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.repository.WeatherDataRepository;
import com.fishing.bite_shore.repository.WeatherLocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WeatherStorageService {

    private final WeatherLocationRepository locationRepository;
    private final WeatherDataRepository dataRepository;

    @Transactional
    public WeatherLocationEntity upsertLocation(WeatherLocationEntity newLocation) {

        return locationRepository.findByExternalId(newLocation.getExternalId())
                .map(existing -> {

                    existing.setName(newLocation.getName());
                    existing.setReceivedAt(newLocation.getReceivedAt());
                    existing.setElevation(newLocation.getElevation());
                    existing.setLat(newLocation.getLat());
                    existing.setLon(newLocation.getLon());
                    existing.setSunrise(newLocation.getSunrise());
                    existing.setSunset(newLocation.getSunset());

                    locationRepository.save(existing);

                    // повертаємо новий список, а не старий
                    newLocation.setId(existing.getId());

                    return newLocation;
                })
                .orElseGet(() -> locationRepository.save(newLocation));
    }

    public void upsertData(Long locationId, List<WeatherDataEntity> weatherData) {

        for (WeatherDataEntity data : weatherData) {

            data.setLocation(
                    locationRepository.getReferenceById(locationId)
            );

            dataRepository.findByLocationIdAndForecastTime(
                    locationId,
                    data.getForecastTime()
            ).map(a -> {
                a.setTemperature(data.getTemperature());
                a.setPressure(data.getPressure());
                a.setHumidity(data.getHumidity());

                a.setWindSpeed(data.getWindSpeed());
                a.setWindDirection(data.getWindDirection());
                a.setWindGust(data.getWindGust());

                a.setPrecipitation(data.getPrecipitation());
                a.setPopPercent(data.getPopPercent());

                a.setWeatherCode(data.getWeatherCode());
                a.setWeatherGroup(data.getWeatherGroup());
                a.setDescription(data.getDescription());
                a.setIcon(data.getIcon());

                return dataRepository.save(a);

            }).orElseGet(() -> {
                return dataRepository.save(data);
            });
        }
    }

}
