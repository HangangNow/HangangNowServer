package com.hangangnow.mainserver.picnic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class RecomCourseRequestDto {

    @NotNull
    private Double x_pos;
    @NotNull
    private Double y_pos;
    @NotNull
    private String companion;

    private List<String> places = new ArrayList<>();
    private List<String> themes = new ArrayList<>();

    public void updateThemeByCompainon() {
        this.themes = new ArrayList<>();
        switch (this.companion) {
            case "혼자": {
                this.themes.add("산책");
                this.themes.add("자전거");
                this.themes.add("예술");
                break;
            }
            case "가족": {
                this.themes.add("생태체험");
                this.themes.add("역사");
                break;
            }
            default: {
                this.themes.add("산책");
                this.themes.add("예술");
                this.themes.add("자전거");
                break;
            }
        }
    }

    public RecomCourseRequestDto(Double x_pos, Double y_pos, String companion) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.companion = companion;
    }
}
