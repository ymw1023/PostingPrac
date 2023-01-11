package com.sparta.posting.exception;

import com.sparta.posting.dto.ResponseStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { Exception.class})
    public ResponseStatusDto handleApiRequestException(Exception ex, HttpServletResponse response) {
        if(response.getStatus() == 200) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return new ResponseStatusDto(ex.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }               //예외가 발생하면 Http 헤더에 상태코드를 설정하고, message 를 던짐
}
