package com.hangangnow.mainserver.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class GenericResponseDto<T> {
    @NotEmpty
    private T data;
}
