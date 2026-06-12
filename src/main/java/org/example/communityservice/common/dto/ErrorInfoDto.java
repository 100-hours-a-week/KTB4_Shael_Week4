package org.example.communityservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorInfoDto {
    private String field;
    private String code;
}
