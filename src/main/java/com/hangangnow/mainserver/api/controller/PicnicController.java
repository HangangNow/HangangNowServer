package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.picnic.*;
import com.hangangnow.mainserver.domain.picnic.dto.RecomCourseDto;
import com.hangangnow.mainserver.domain.picnic.dto.RecomCourseRequestDto;
import com.hangangnow.mainserver.domain.picnic.dto.RecomCourseResponseDto;
import com.hangangnow.mainserver.service.PicnicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/picnic")
public class PicnicController {
    private final PicnicService picnicService;

    @GetMapping("/course/{courseid}")
    @Operation(summary = "단일 추천 코스 조회", description = "코스 id를 이용해 코스를 상세 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public RecomCourseDto CourseOne(@PathVariable("courseid") Long courseid){
        return picnicService.courseOne(courseid);
    }

    @GetMapping("/place/{placeid}")
    @Operation(summary = "단일 추천 장소 조회", description = "장소 id를 이용해 장소를 상세 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public RecomPlace PlaceOne(@PathVariable("placeid") Long placeid){
        return picnicService.placeOne(placeid);
    }

    @GetMapping("/recom/place")
    @Operation(summary = "위치 기반 추천 장소 조회", description = "위치 좌표값을 이용해 가까운 거리의 추천 장소를 10개 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public List<RecomPlace> RecomCourse(@RequestParam("x_pos") @Min(-180) @Max(180) Double x_pos,
                                        @RequestParam("y_pos") @Min(-90) @Max(90) Double y_pos){
        return picnicService.recomPlaces(x_pos, y_pos, 10L);
    }

    @PostMapping("/recom/course")
    @Operation(summary = "사용자 맞춤 추천 코스 조회", description = "추천 코스를 조회할 수 있습니다.  \n" +
            "누구와: 가족, 친구, 연인, 혼자  \n" +
            "무엇을: 산책, 자전거, 예술, 생태 체험, 역사  \n" +
            "어디서: 강서, 마포, 영등포, 동작, 용산, 서초, 성동구, 송파, 강동  \n" +
            "누구와, x_pos, y_pos 필드는 필수 값 입니다.  \n" +
            "무엇을, 어디서 필드가 선택되지 않은 상황(상관없음)일 시 해당 필드 없이 요청하면 됩니다.  \n" +
            "length 단위는 km 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public RecomCourseResponseDto RecomCourse(@Valid @RequestBody RecomCourseRequestDto recomCourseRequestDto){
        return picnicService.recomCourses(recomCourseRequestDto);
    }
}
