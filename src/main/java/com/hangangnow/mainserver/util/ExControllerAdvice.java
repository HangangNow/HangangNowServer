package com.hangangnow.mainserver.util;

import com.hangangnow.mainserver.common.exception.GeneralException;
import com.hangangnow.mainserver.common.exception.MethodArgsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public GeneralException illegalExHandle(IllegalArgumentException e) {
        log.error("[IllegalArgument ExceptionHandle] ex: " + e.getMessage());
        e.printStackTrace();
        return new GeneralException("BAD", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MethodArgsException methodValidExceptionHandle(MethodArgumentNotValidException e) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        for (ObjectError error : allErrors) {
            FieldError field = (FieldError) error;
            errors.put(field.getField(), error.getDefaultMessage());
            log.error("[IllegalArgument ExceptionHandle] ex: " + error.getDefaultMessage());
        }

        e.printStackTrace();
        return new MethodArgsException("BAD", errors);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(RuntimeException.class)
    public GeneralException illegalExHandle(RuntimeException e) {
        log.error("[Runtime ExceptionHandle] ex: " + e.getMessage());
        e.printStackTrace();
        return new GeneralException("FORBIDDEN", e.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public GeneralException handleNotFoundError(NoHandlerFoundException e) {
        log.error("[NoHandlerFound ExceptionHandle] ex: " + e.getMessage());
        e.printStackTrace();
        return new GeneralException("NOT FOUND", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public GeneralException NullPointerExHandle(NullPointerException e) {
        log.error("[NullPointer ExceptionHandle] ex: " + e.getMessage());
        e.printStackTrace();
        return new GeneralException("BAD", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public GeneralException MultipartExHandle(MultipartException e) {
        log.error("[MultipartException Handle]: " + e.getMessage());
        e.printStackTrace();
        return new GeneralException("BAD", e.getMessage());
    }
}
