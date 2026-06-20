package org.example.communityservice.common.Exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException{
    public ForbiddenException(){
        super(HttpStatus.FORBIDDEN, "permission_denied");
    }
}
