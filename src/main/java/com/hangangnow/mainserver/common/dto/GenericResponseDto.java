package com.hangangnow.mainserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class GenericResponseDto<T> {

    @NotEmpty
    private T data;
}
