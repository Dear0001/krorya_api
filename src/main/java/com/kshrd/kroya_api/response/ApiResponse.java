package com.kshrd.kroya_api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {
    private T payload;
    private String message;
    private Integer code;
    private boolean error;
    private LocalDateTime date;
}
