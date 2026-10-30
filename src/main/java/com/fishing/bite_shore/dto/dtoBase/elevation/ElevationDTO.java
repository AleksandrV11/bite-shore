package com.fishing.bite_shore.dto.dtoBase.elevation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElevationDTO {

    private List<Result> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Double elevation;
    }

    public Double getElevationValue() {
        return results != null && !results.isEmpty()
                ? results.get(0).getElevation()
                : null;
    }
}
