package com.hangangnow.mainserver.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
public class MethodArgsException {
    private String code;
    private LinkedHashMap<String, String> message;
}
