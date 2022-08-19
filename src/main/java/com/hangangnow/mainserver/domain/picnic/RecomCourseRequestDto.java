package com.hangangnow.mainserver.domain.picnic;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class RecomCourseRequestDto {

    @NotEmpty
    private Double x_pos;
    @NotEmpty
    private Double y_pos;

    private List<String> places = new ArrayList<>();
    private List<String> themes = new ArrayList<>();
    private String companion;

    public void updateThemeByCompainon(){
        this.themes = new ArrayList<>();
        switch(this.companion){
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
}
