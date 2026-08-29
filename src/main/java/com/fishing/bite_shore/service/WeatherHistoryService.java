package com.fishing.bite_shore.service;

import com.fishing.bite_shore.exception.InvalidDateException;
import com.fishing.bite_shore.model.WeatherSnapshot;
import com.fishing.bite_shore.repository.WeatherDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherHistoryService {
    private final WeatherDataRepository dataRepository;

    public List<WeatherSnapshot> loadSnapshots(Long locationId, LocalDateTime localDate) {
        LocalDateTime from = localDate.minusDays(4L);
        List<WeatherSnapshot> snapshotList = dataRepository.findSnapshotsByLocationId(locationId).stream()
                .filter(a -> a.getForecastTime().isAfter(from) && a.getForecastTime().isBefore(localDate))
                .toList();
        if (snapshotList.size() == 0) {
            throw new InvalidDateException("The data sheet for analysis is empty.");
        }

        return snapshotList;
    }
}



