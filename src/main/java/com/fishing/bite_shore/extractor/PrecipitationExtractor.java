package com.fishing.bite_shore.extractor;

import com.fishing.bite_shore.model.point.PrecipitationPoint;
import com.fishing.bite_shore.model.WeatherSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecipitationExtractor {
    public List<PrecipitationPoint> extractPrecipitationPoints(List<WeatherSnapshot> snapshotList) {
        return snapshotList.stream()
                .map(snapshot -> new PrecipitationPoint(
                        snapshot.getForecastTime(),
                        snapshot.getPrecipitation(),
                        snapshot.getPopPercent(),
                        snapshot.getHumidity()
                )).toList();
    }
}
