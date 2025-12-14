package com.fishing.bite_shore.servis.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.servis.bd.ForecastPersistenceService;
import com.fishing.bite_shore.servis.extractor.DayForecastDataExtractor;
import com.fishing.bite_shore.weatherData.weather.Wind;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ForecastFacadeService {
    //поверне лист (черги )
    private final ForecastPersistenceService forecastPersistenceService;
    private final DayForecastDataExtractor dayForecastDataExtractor;

    public void windVisualization(double lat, double lon, LocalDateTime localDateTime) throws JsonProcessingException {
        // лист фільтрований/сортований
        List<DayForecastEntity> forecastEntities = forecastPersistenceService.processForecast(lat, lon, localDateTime);
        List<Map<LocalDateTime, Wind>> mapList = dayForecastDataExtractor.getWindByDateTime(forecastEntities);
        System.out.println("/////поглазеть servis.facade.windVisualization////////////");
        for (Map<LocalDateTime, Wind> listMap : mapList) {
            for (Map.Entry<LocalDateTime, Wind> localDateTimeWindEntry : listMap.entrySet()) {
                System.out.println(localDateTimeWindEntry.getKey() + " "
                        + localDateTimeWindEntry.getValue().getClass().getSimpleName() + " = "
                        + localDateTimeWindEntry.getValue().getWindDirection() + " / "
                        + localDateTimeWindEntry.getValue().getWindPower() + " / "
                        + localDateTimeWindEntry.getValue().getImpulseForce());
            }
        }
        System.out.println("/////////////////////////////////");
    }
}


