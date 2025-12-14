package com.fishing.bite_shore.daysForAnalysis;

import com.fishing.bite_shore.weatherData.DayForecast;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class SecondDayAnalysis {
    private DayForecast dayForecast;

//    public SecondDayAnalysis(DayForecast dayForecast) {
//        this.dayForecast = dayForecast;
//    }
}
