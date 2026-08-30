package com.fishing.bite_shore.dto.dtoBase.error;

public record ErrorResponse(
        int status,
        String message
) {
}
