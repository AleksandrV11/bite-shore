package com.fishing.bite_shore.extractor;

import com.fishing.bite_shore.model.point.TemperaturePoint;
import com.fishing.bite_shore.model.WeatherSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemperatureExtractor {
    public List<TemperaturePoint> extractTemperaturePoints(List<WeatherSnapshot> snapshots) {
        return snapshots.stream()
                .map(snapshot -> new TemperaturePoint(
                        snapshot.getForecastTime(),
                        snapshot.getTemperature()
                )).toList();
    }
}
