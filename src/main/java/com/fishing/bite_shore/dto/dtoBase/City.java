package com.fishing.bite_shore.dto.dtoBase;

import lombok.Data;

@Data
public class City {
    private int id;
    private String name;
    private Coord coord;
    private String country;
    private int population;
    private int timezone;
    private int sunrise;
    private int sunset;
}
