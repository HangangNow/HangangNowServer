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

    public RecomPlaceResponseDto getOnePlace(Long id) {
        List<RecomPlace> scrapPlaces = scrapRepository.findRecomPlaceByMemberId(SecurityUtil.getCurrentMemberId());

        RecomPlace recomPlace = picnicRepository.findPlaceById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found Place"));

        RecomPlaceResponseDto recomPlaceResponseDto = new RecomPlaceResponseDto(recomPlace);
        checkRecomPlaceScrap(scrapPlaces, recomPlace, recomPlaceResponseDto);

        return recomPlaceResponseDto;
    }

    public List<RecomPlaceResponseDto> getRecomPlaces(Double x_pos, Double y_pos, Long limitSize) {
        List<RecomPlace> scrapPlaces = scrapRepository.findRecomPlaceByMemberId(SecurityUtil.getCurrentMemberId());

        List<RecomPlace> recomPlaces = picnicRepository.findAllPlace()
                .stream()
                .sorted(Comparator.comparingDouble(p -> p.getDistance(x_pos, y_pos)))
                .limit(limitSize)
                .collect(Collectors.toList());

        List<RecomPlaceResponseDto> results = new ArrayList<>();

        for (RecomPlace recomPlace : recomPlaces) {
            RecomPlaceResponseDto recomPlaceResponseDto = new RecomPlaceResponseDto(recomPlace);
            checkRecomPlaceScrap(scrapPlaces, recomPlace, recomPlaceResponseDto);
            results.add(recomPlaceResponseDto);
        }

        return results;
    }

    private void checkRecomPlaceScrap(List<RecomPlace> scrapPlaces, RecomPlace recomPlace, RecomPlaceResponseDto recomPlaceResponseDto) {
        if (scrapPlaces.contains(recomPlace)) {
            recomPlaceResponseDto.setIsScrap(true);
        } else {
            recomPlaceResponseDto.setIsScrap(false);
        }
    }

    public RecomCourseDto getOneCourse(Long id) {
        List<RecomCourse> scrapCourses = scrapRepository.findRecomCourseByMemberId(SecurityUtil.getCurrentMemberId());

        RecomCourse findRecomCourse = picnicRepository.findCourseById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found Course"));

        RecomCourseDto recomCourseDto = new RecomCourseDto(findRecomCourse);
        checkRecomCourseScrap(scrapCourses, findRecomCourse, recomCourseDto);

        return recomCourseDto;
    }

    public List<RecomCourseDto> getRecomCoursesWithoutPlace(RecomCourseRequestDto recomCourseRequestDto, List<RecomCourse> scrapCourses) {
        List<RecomCourse> courses;
        if (recomCourseRequestDto.getThemes().isEmpty()) recomCourseRequestDto.updateThemeByCompainon();

        if (recomCourseRequestDto.getPlaces().isEmpty()) {
            courses = picnicRepository.findAllCourseByTheme(recomCourseRequestDto.getThemes())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        } else {
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
            checkRecomCourseScrap(scrapCourses, optimizeCourse, recomCourseDto);

            results.add(recomCourseDto);
        }

        return results;
    }

    public RecomCourseResponseDto getRecomCourses(RecomCourseRequestDto recomCourseRequestDto) {
        List<RecomCourse> scrapCourses = scrapRepository.findRecomCourseByMemberId(SecurityUtil.getCurrentMemberId());

        List<RecomPlaceResponseDto> recomPlaces = getRecomPlaces(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos(), 2L);

        List<RecomCourseDto> recomCourses = getRecomCoursesWithoutPlace(recomCourseRequestDto, scrapCourses);
        if (recomCourses.isEmpty()) {
            List<RecomCourse> recomCourseList = picnicRepository.findAllCourseByTheme(recomCourseRequestDto.getThemes())
                    .stream()
                    .distinct()
                    .sorted(Comparator.comparingDouble(c -> c.getDistance(recomCourseRequestDto.getX_pos(), recomCourseRequestDto.getY_pos())))
                    .collect(Collectors.toList());

            List<RecomCourseDto> results = new ArrayList<>();
            for (RecomCourse recomCourse : recomCourseList) {
                RecomCourseDto recomCourseDto = new RecomCourseDto(recomCourse);
                checkRecomCourseScrap(scrapCourses, recomCourse, recomCourseDto);
                results.add(recomCourseDto);
            }
            recomCourses = results;
            if (recomCourses.isEmpty()) throw new NullPointerException("Failed: Not Found Course");
        }
        return new RecomCourseResponseDto(true, recomCourses, recomPlaces);
    }

    private void checkRecomCourseScrap(List<RecomCourse> scrapCourses, RecomCourse findRecomCourse, RecomCourseDto recomCourseDto) {
        if (scrapCourses.contains(findRecomCourse)) {
            recomCourseDto.setIsScrap(true);
        } else {
            recomCourseDto.setIsScrap(false);
        }
    }
}
