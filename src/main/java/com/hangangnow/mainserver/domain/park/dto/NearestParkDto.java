package com.hangangnow.mainserver.domain.park.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearestParkDto {
    public Long id;
    public String name;
    public Double x_pos;
    public Double y_pos;
}
