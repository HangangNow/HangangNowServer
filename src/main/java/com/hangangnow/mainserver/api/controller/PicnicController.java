package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.picnic.*;
import com.hangangnow.mainserver.service.PicnicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/picnic")
public class PicnicController {
    private final PicnicService picnicService;

    @GetMapping("/course/{courseid}")
    public RecomCourseDto CourseOne(@PathVariable("courseid") Long courseid){
        return picnicService.courseOne(courseid);
    }

    @GetMapping("/course/{placeid}")
    public RecomPlace PlaceOne(@PathVariable("placeid") Long placeid){
        return picnicService.placeOne(placeid);
    }

    @PostMapping("/recom/course")
    public RecomCourseResponseDto RecomCourse(@RequestBody RecomCourseRequestDto recomCourseRequestDto){
        return picnicService.recomCourses(recomCourseRequestDto);
    }

    @PostMapping("/recom/place")
    public List<RecomPlace> RecomCourse(@RequestBody RecomPlaceRequestDto recomPlaceRequestDto){
        return picnicService.recomPlaces(recomPlaceRequestDto);
    }
}
