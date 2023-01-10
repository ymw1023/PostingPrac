package com.sparta.posting.exception;

import com.sparta.posting.dto.ResponseStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { Exception.class})
    public ResponseStatusDto handleApiRequestException(Exception ex) {
        return new ResponseStatusDto("해당 값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
