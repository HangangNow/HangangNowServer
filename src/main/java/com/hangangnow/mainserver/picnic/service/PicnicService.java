package com.hangangnow.mainserver.picnic.service;

import com.hangangnow.mainserver.picnic.entity.RecomCourse;
import com.hangangnow.mainserver.picnic.entity.RecomPlace;
import com.hangangnow.mainserver.util.SecurityUtil;
import com.hangangnow.mainserver.picnic.dto.RecomCourseDto;
import com.hangangnow.mainserver.picnic.dto.RecomCourseRequestDto;
import com.hangangnow.mainserver.picnic.dto.RecomCourseResponseDto;
import com.hangangnow.mainserver.picnic.dto.RecomPlaceResponseDto;
import com.hangangnow.mainserver.picnic.repository.PicnicRepository;
import com.hangangnow.mainserver.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PicnicService {

    private final PicnicRepository picnicRepository;
    private final ScrapRepository scrapRepository;

    public RecomPlaceResponseDto placeOne(Long id){
        List<RecomPlace> scrapPlaces = scrapRepository.findRecomPlaceByMemberId(SecurityUtil.getCurrentMemberId());

        RecomPlace recomPlace = picnicRepository.findPlaceById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found Place"));

        RecomPlaceResponseDto recomPlaceResponseDto = new RecomPlaceResponseDto(recomPlace);
        if(scrapPlaces.contains(recomPlace)){
            recomPlaceResponseDto.setIsScrap(true);
        }
        else{
            recomPlaceResponseDto.setIsScrap(false);
        }

        return recomPlaceResponseDto;
    }

    public List<RecomPlaceResponseDto> recomPlaces (Double x_pos, Double y_pos, Long limitSize){
        List<RecomPlace> scrapPlaces = scrapRepository.findRecomPlaceByMemberId(SecurityUtil.getCurrentMemberId());

        List<RecomPlace> recomPlaces = picnicRepository.findAllPlace()
                .stream()
                .sorted(Comparator.comparingDouble(p -> p.getDistance(x_pos, y_pos)))
                .limit(limitSize)
                .collect(Collectors.toList());

        List<RecomPlaceResponseDto> results = new ArrayList<>();


        for (RecomPlace recomPlace : recomPlaces) {
            RecomPlaceResponseDto recomPlaceResponseDto = new RecomPlaceResponseDto(recomPlace);
            if (scrapPlaces.contains(recomPlace)){
                recomPlaceResponseDto.setIsScrap(true);
            }
            else{
                recomPlaceResponseDto.setIsScrap(false);
            }
            results.add(recomPlaceResponseDto);
        }

        return results;
    }

    public RecomCourseDto courseOne(Long id){
        List<RecomCourse> scrapCourses = scrapRepository.findRecomCourseByMemberId(SecurityUtil.getCurrentMemberId());

        RecomCourse findRecomCourse = picnicRepository.findCourseById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found Course"));

        RecomCourseDto recomCourseDto = new RecomCourseDto(findRecomCourse);

        if(scrapCourses.contains(findRecomCourse)){
            recomCourseDto.setIsScrap(true);
        }
        else{
            recomCourseDto.setIsScrap(false);
        }

        return recomCourseDto;
    }

    public List<RecomCourseDto> recomCoursesWithoutPlace(RecomCourseRequestDto recomCourseRequestDto, List<RecomCourse> scrapCourses){
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

        List<RecomCourseDto> results = new ArrayList<>();

        for (RecomCourse optimizeCourse : optimizeCourses) {
            RecomCourseDto recomCourseDto = new RecomCourseDto(optimizeCourse);
            if(scrapCourses.contains(optimizeCourse)){
                recomCourseDto.setIsScrap(true);
            }
            else{
                recomCourseDto.setIsScrap(false);
            }

            results.add(recomCourseDto);
        }

        return results;
    }

    public RecomCourseResponseDto recomCourses(RecomCourseRequestDto recomCourseRequestDto){
        List<RecomCourse> scrapCourses = scrapRepository.findRecomCourseByMemberId(SecurityUtil.getCurrentMemberId());

        List<RecomPlaceResponseDto> recomPlaces = recomPlaces(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos(), 2L);

        List<RecomCourseDto> recomCourses = recomCoursesWithoutPlace(recomCourseRequestDto, scrapCourses);
        if(recomCourses.isEmpty()){
            List<RecomCourse> recomCourseList = picnicRepository.findAllCourseByTheme(recomCourseRequestDto.getThemes())
                    .stream()
                    .distinct()
                    .sorted(Comparator.comparingDouble(c -> c.getDistance(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos())))
                    .collect(Collectors.toList());

            List<RecomCourseDto> results = new ArrayList<>();
            for (RecomCourse recomCourse : recomCourseList) {
                RecomCourseDto recomCourseDto = new RecomCourseDto(recomCourse);
                if(scrapCourses.contains(recomCourse)){
                    recomCourseDto.setIsScrap(true);
                }
                else {
                    recomCourseDto.setIsScrap(false);
                }
                results.add(recomCourseDto);
            }
            recomCourses = results;
            if(recomCourses.isEmpty()) throw new NullPointerException("Failed: Not Found Course");
        }
        return new RecomCourseResponseDto(true, recomCourses, recomPlaces);
    }
}
