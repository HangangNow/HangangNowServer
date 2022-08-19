package com.hangangnow.mainserver.domain.picnic;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
public class RecomPlaceRequestDto {
    @NotEmpty
    private Double x_pos;
    @NotEmpty
    private Double y_pos;
}
