package com.hangangnow.mainserver.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
public class MethodArgsException {
    private String code;
    private LinkedHashMap<String, String> message;
}
