package com.fishing.bite_shore.extractor;

import com.fishing.bite_shore.model.WeatherSnapshot;
import com.fishing.bite_shore.model.point.WindPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WindExtractor {

    public List<WindPoint> extractWindPoints(List<WeatherSnapshot> snapshotList) {

        return snapshotList.stream()
                .map(snapshot -> new WindPoint(
                        snapshot.getForecastTime(),
                        snapshot.getWindSpeed(),
                        snapshot.getWindDirection(),
                        snapshot.getWindGust()
                )).toList();
    }
}
