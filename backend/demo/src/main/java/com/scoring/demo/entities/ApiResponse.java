package com.scoring.demo.entities;

import lombok.Data;

@Data
public class ApiResponse {
    private String message;
    private Object data;
}
