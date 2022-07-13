package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.exception.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[IllegalArgument ExceptionHandle] ex" + e.getMessage());
        return new ErrorResult("BAD", e.getMessage());
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResult illegalExHandle(RuntimeException e) {
        log.error("[Runtime ExceptionHandle] ex" + e.getMessage());
        return new ErrorResult("BAD", e.getMessage());
    }

}
