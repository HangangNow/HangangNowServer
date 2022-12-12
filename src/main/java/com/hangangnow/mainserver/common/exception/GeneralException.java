package com.hangangnow.mainserver.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneralException {
    private String code;
    private String message;
}
