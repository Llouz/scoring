package com.scoring.demo.DTOs;

import lombok.Data;

@Data
public class CoachmarkDto {
    private UserDto user;
    private GuideDto guide;
    private Integer score;
}
