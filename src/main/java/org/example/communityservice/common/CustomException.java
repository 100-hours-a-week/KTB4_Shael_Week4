package org.example.communityservice.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final Object data;

    public CustomException(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
        this.data = null;
    }

    public CustomException(HttpStatus httpStatus, String message, Object data){
        super(message);
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
