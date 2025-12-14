package com.fishing.bite_shore.dto.dtoBase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinList {
    private int dt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private int visibility;
    private int pop;
    private Rain rain;
    private Sys sys;
    private String dt_txt;
}
