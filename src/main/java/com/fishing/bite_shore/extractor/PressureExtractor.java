package com.fishing.bite_shore.extractor;

import com.fishing.bite_shore.model.point.PressurePoint;
import com.fishing.bite_shore.model.WeatherSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PressureExtractor {
    public List<PressurePoint> extractPressurePoints(List<WeatherSnapshot> snapshotList) {
        return snapshotList.stream()
                .map(snapshot -> new PressurePoint(
                        snapshot.getForecastTime(),
                        snapshot.getPressure()
                ))
                .toList();
    }
}
