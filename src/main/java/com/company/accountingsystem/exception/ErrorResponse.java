package com.company.accountingsystem.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Hidden
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"statusCode", "message", "path", "timestamp", "link"})
public class ErrorResponse {
    @JsonProperty("status")
    private Integer statusCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private final String link = "http://localhost:8080/swagger-ui/index.html";
}
