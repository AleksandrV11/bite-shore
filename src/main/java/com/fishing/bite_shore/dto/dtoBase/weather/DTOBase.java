package com.fishing.bite_shore.dto.dtoBase.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTOBase {
    private String cod;
    private int message;
    private int cnt;
    private List<WinListDTO> list;
    private CityDto city;
}












