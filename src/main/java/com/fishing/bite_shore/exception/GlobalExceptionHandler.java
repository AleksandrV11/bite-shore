package com.fishing.bite_shore.exception;

import com.fishing.bite_shore.dto.dtoBase.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse response = new ErrorResponse(400, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getField() + ": " +
                exception.getBindingResult()
                        .getFieldErrors()
                        .get(0)
                        .getDefaultMessage();

        ErrorResponse response = new ErrorResponse(400, message);
        return ResponseEntity.badRequest().body(response);
    }

    //    HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        ErrorResponse response = new ErrorResponse(400,
                "Невірний формат дати. Використовуйте формат yyyy-MM-dd");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ErrorResponse> handleWebClientRequestException(WebClientRequestException exception) {
        ErrorResponse response = new ErrorResponse(503, "Сервіс погоди тимчасово недоступний");
        return ResponseEntity.status(503).body(response);
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFound(WebClientResponseException.NotFound exception) {
        ErrorResponse response = new ErrorResponse(404, "Місто не знайдено");
        return ResponseEntity.status(404).body(response);
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
//        ErrorResponse response = new ErrorResponse(500, "Внутрішня помилка сервера");
//        return ResponseEntity.status(500).body(response);
//    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {

        exception.printStackTrace();

        ErrorResponse response = new ErrorResponse(
                500,
                "Внутрішня помилка сервера"
        );

        return ResponseEntity.status(500).body(response);
    }
}
