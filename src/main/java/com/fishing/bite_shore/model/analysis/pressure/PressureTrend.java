package com.fishing.bite_shore.model.analysis.pressure;

public enum PressureTrend {
    STABILIZED_LOW,       // тиск стабільний на низькому рівні
    STABILIZED_NORMAL,    // тиск стабільний на нормальному рівні
    STABILIZED_HIGH,      // тиск стабільний на високому рівні

    RISING,               // тиск зростає
    FALLING,              // тиск падає
    NOT_DATA              // немає даних
}
