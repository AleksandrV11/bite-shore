package com.fishing.bite_shore.servis.bd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.servis.analysis.AnalysisByWindDirection;
import com.fishing.bite_shore.servis.extractor.DayForecastDataExtractor;
import com.fishing.bite_shore.servis.parser.ForecastService;
import com.fishing.bite_shore.servis.parser.WeatherMapper;
import com.fishing.bite_shore.servis.sortByDayForecast.DayForecastSorterRepository;
import com.fishing.bite_shore.servis.sortByDayForecast.DayForecastSorterTime;
import com.fishing.bite_shore.weatherData.DayForecast;
import com.fishing.bite_shore.weatherData.WeatherData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ForecastPersistenceService {
    private final WeatherMapper weatherMapper;
    private final ForecastService forecastService;
    private final WeatherServiceToBd weatherServiceToBd;
    private final DayForecastSorterRepository dayForecastSortedRepository;
    private final DayForecastSorterTime dayForecastSorterTime;

    public List<DayForecastEntity> processForecast(double lat, double lon, LocalDateTime localDateTime) throws JsonProcessingException {
        // отримання-перевірка-оновлення інфи в бд
        fetchAndRefreshForecast(lat, lon);
        //отримую лист з бд (сортований по координатах та в періоді n днів)для аналізу
        List<DayForecastEntity> forecastEntities = receiveDataFiveDays(lat, lon, localDateTime);
        //отримуємо сортований по датах лист
        List<DayForecastEntity> sortForecastEntities = dayForecastSorterTime.getSortedDayForecast(forecastEntities);
        System.out.println("*****Відсортований лист по порядку (servis.bd.ForecastPersistenceService;)**************");
        for (DayForecastEntity forecast:sortForecastEntities){
            System.out.println(forecast);
        }
        System.out.println("**************************");
        return sortForecastEntities;
    }

    public void fetchAndRefreshForecast(double lat, double lon) throws JsonProcessingException {
        // List<WeatherData> dayForecastList = weatherMapper.mapWinListList(weatherMapper.getWinList(nameCity));//name
        List<WeatherData> dayForecastList = weatherMapper.mapWinListList(weatherMapper.getWinListCoord(lat, lon));//coord
        //координати + дані
        List<DayForecast> dayForecasts = forecastService.getDayForecastList(dayForecastList, lat, lon);
        for (
                DayForecast dayForecast : dayForecasts) { //подивитись
            System.out.println(dayForecast);
        }
        // лист зі  співпадіннями по координатах
        List<DayForecastEntity> forecastEntitiesSortCoordinates =
                dayForecastSortedRepository.getDayForecastEntitySortByCoordinates(lat, lon);
        //перетворюю в ентіті лист
        List<DayForecastEntity> forecastEntities = weatherServiceToBd.mapToEntity(dayForecasts);
        //якщо не було співпадінь по координатах то лист пустий
        if (forecastEntitiesSortCoordinates.isEmpty()) {
            //перетворюємо в лист ентіті і записуємо в бд перетворений лист dayForecast-ов
            weatherServiceToBd.saveListDayForecastEntity(forecastEntities);
        } else {  //якщо були співпадіння по координатах фільтруємо далі по датах
            // лист заповнений у разі співпадінь по датах з листа після співпадінь по координатах
            List<DayForecastEntity> dayForecastEntitiesSortByTime =
                    dayForecastSortedRepository.getDayForecastEntitiesSortByTime(lat, lon, forecastEntities, forecastEntitiesSortCoordinates);
            //при сортуванні по координатах були співпадіння (записуємо оновлений лист)
            weatherServiceToBd.saveListDayForecastEntity(dayForecastEntitiesSortByTime);
        }
    }

    //отримую лист з бд (сортований по координатах та в періоді n днів)для аналізу
    public List<DayForecastEntity> receiveDataFiveDays(double lat, double lon, LocalDateTime dataTime) {
        return dayForecastSortedRepository.getLastFiveDaysSorted(lat, lon, dataTime);
    }

}

