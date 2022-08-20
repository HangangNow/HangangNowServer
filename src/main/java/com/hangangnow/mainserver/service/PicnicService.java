package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.picnic.*;
import com.hangangnow.mainserver.domain.picnic.dto.RecomCourseDto;
import com.hangangnow.mainserver.domain.picnic.dto.RecomCourseRequestDto;
import com.hangangnow.mainserver.domain.picnic.dto.RecomCourseResponseDto;
import com.hangangnow.mainserver.repository.PicnicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PicnicService {
    private final PicnicRepository picnicRepository;

    public RecomPlace placeOne(Long id){
        return picnicRepository.findPlaceById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found Place"));
    }

    public List<RecomPlace> recomPlaces (Double x_pos, Double y_pos, Long limitSize){
        return picnicRepository.findAllPlace()
                .stream()
                .sorted(Comparator.comparingDouble(p -> p.getDistance(x_pos, y_pos)))
                .limit(limitSize)
                .collect(Collectors.toList());
    }

    public RecomCourseDto courseOne(Long id){
        RecomCourse findRecomCourse = picnicRepository.findCourseById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found Course"));
        return new RecomCourseDto(findRecomCourse);
    }

    public List<RecomCourseDto> recomCoursesWithoutPlace(RecomCourseRequestDto recomCourseRequestDto){
        List<RecomCourse> courses;
        if(recomCourseRequestDto.getThemes().isEmpty()) recomCourseRequestDto.updateThemeByCompainon();

        if(recomCourseRequestDto.getPlaces().isEmpty()){
            courses = picnicRepository.findAllCourseByTheme(recomCourseRequestDto.getThemes())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        }else{
            courses = picnicRepository.findAllCourseByPlaceAndTheme(recomCourseRequestDto.getPlaces(), recomCourseRequestDto.getThemes())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<RecomCourse> optimizeCourses = courses.stream()
                .sorted(Comparator.comparingDouble(c -> c.getDistance(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos())))
                .collect(Collectors.toList());

        return optimizeCourses.stream().map(RecomCourseDto::new).collect(Collectors.toList());
    }

    public RecomCourseResponseDto recomCourses(RecomCourseRequestDto recomCourseRequestDto){
        List<RecomPlace> recomPlaces = recomPlaces(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos(), 2L);
        List<RecomCourseDto> recomCourses = recomCoursesWithoutPlace(recomCourseRequestDto);
        if(recomCourses.isEmpty()){
            recomCourses = picnicRepository.findAllCourseByTheme(recomCourseRequestDto.getThemes())
                    .stream()
                    .distinct()
                    .sorted(Comparator.comparingDouble(c -> c.getDistance(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos())))
                    .map(RecomCourseDto::new)
                    .collect(Collectors.toList());
            if(recomCourses.isEmpty()) throw new NullPointerException("Failed: Not Found Course");
        }
        return new RecomCourseResponseDto(true, recomCourses, recomPlaces);
    }
}
