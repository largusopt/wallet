package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private final String message;
    private final String status;
    private final LocalDateTime timestamp;
}
