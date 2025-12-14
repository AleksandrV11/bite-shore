package com.fishing.bite_shore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.servis.analysis.AnalysisByWindDirection;
import com.fishing.bite_shore.servis.bd.ForecastPersistenceService;
import com.fishing.bite_shore.servis.facade.ForecastFacadeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class BiteShoreApplication {

    public static void main(String[] args) throws JsonProcessingException {

        ConfigurableApplicationContext context = SpringApplication.run(BiteShoreApplication.class, args);
        String nameCity = "ДНІПРО";
        double lat = 10.7656; // проізвольно
        double lon = 4.4864;
//        double lat = 48.7656; // андреевка
//        double lon = 35.4864;
//        double lat = 48.1280973;//антоновка
//        double lon = 34.0656450;
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(9);
        ForecastFacadeService forecastFacadeService = context.getBean(ForecastFacadeService.class);
        //поглазеть що маємо
        forecastFacadeService.windVisualization(lat, lon, localDateTime);

    }

}
