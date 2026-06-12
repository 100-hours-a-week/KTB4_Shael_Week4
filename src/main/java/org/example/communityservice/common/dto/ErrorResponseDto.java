package org.example.communityservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private List<ErrorInfoDto> errors;
}
