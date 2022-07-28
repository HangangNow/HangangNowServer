package com.hangangnow.mainserver.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class IdResponseDto {

    @NotEmpty
    private Long id;
}
