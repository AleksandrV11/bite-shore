package com.fishing.bite_shore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.model.daily.*;
import com.fishing.bite_shore.model.WeatherProcessResult;
import com.fishing.bite_shore.model.WeatherSnapshot;
import com.fishing.bite_shore.repository.WeatherLocationRepository;
import com.fishing.bite_shore.service.FishingAnalysisService;
import com.fishing.bite_shore.service.aggregator.PrecipitationAggregator;
import com.fishing.bite_shore.service.aggregator.PressureAggregator;
import com.fishing.bite_shore.service.aggregator.TemperatureAggregator;
import com.fishing.bite_shore.service.WeatherHistoryService;
import com.fishing.bite_shore.service.aggregator.WindAggregator;
import com.fishing.bite_shore.service.analyzer.*;
import com.fishing.bite_shore.service.builder.DailyWeatherDataBuilder;
import com.fishing.bite_shore.service.orhestrator.WeatherOrchestratorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class BiteShoreApplication {
    ;

    public static void main(String[] args) throws JsonProcessingException {
//        double lat = 10.7656; // проізвольно
//        double lon = 4.4864;
//        double lat = 48.7656; // андреевка
//        double lon = 35.4864;
//        double lat = 48.1280973;//антоновка
//        double lon = 34.0656450;
//        double lat = 48.384756; // городківка
//        double lon = 28.691357
        //ЗМІНА КІЛЬКОСТІ ДНІВ ДЛЯ АНАЛІЗУ У WeatherHistoryService   А САМЕ LocalDateTime from = localDate.minusDays(4L);
// якщо подивитись базові данні та це в   service.parser.WeatherService.getParsDTOCity або getParsDTOCoord
        ConfigurableApplicationContext context = SpringApplication.run(BiteShoreApplication.class, args);
        String nameCity = "ГОРОДКІВКА";

        String date = "2026-09-01";
        System.out.println(" Дата риболовлі : " + date);
        LocalDate fishingDate = LocalDate.of(2026, 9, 1);

        FishingAnalysisService fishingAnalysisService =
                context.getBean(FishingAnalysisService.class);

        fishingAnalysisService.analyze(48.1280973, 34.0656450, nameCity, fishingDate);


    }


}
