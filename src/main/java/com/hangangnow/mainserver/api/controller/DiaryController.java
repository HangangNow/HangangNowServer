package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDateRequestDto;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import com.hangangnow.mainserver.service.myPageService.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("")
    public GenericResponseDto addDiary(@Valid @RequestPart(value = "data") DiaryDto request,
                                       @Valid @RequestPart(value = "image", required = false) MultipartFile imageRequest) throws Exception {
        return new GenericResponseDto(diaryService.addDiary(request, imageRequest));
    }

    @GetMapping("/{diaryid}")
    public DiaryDto DiaryOne(@PathVariable("diaryid") Long diaryid){
        return diaryService.findOne(diaryid);
    }

    @GetMapping("")
    public GenericResponseDto Diaries(){
        return new GenericResponseDto(diaryService.findAllMemberDiary());
    }

    @GetMapping("/years/{year}/months/{month}")
    public GenericResponseDto CalendarDiaries(@PathVariable("year") int year, @PathVariable("month") int month) {
        return new GenericResponseDto(diaryService.findAllCalendarDiary(year, month));
    }

    @PostMapping("/date")
    public GenericResponseDto DateDiaries(@Valid @RequestBody DiaryDateRequestDto diaryDateRequestDto){
        return new GenericResponseDto(diaryService.findAllDateDiary(diaryDateRequestDto));
    }

//    @PutMapping("/diaryid")
//    public GenericResponseDto ModifyDiary( @PathVariable("diaryid") Long diaryid,
//                                        @Valid @RequestPart(value = "data") DiaryDto request,
//                                        @Valid @RequestPart(value = "image", required = false) MultipartFile imageRequest) throws Exception {
//        return new GenericResponseDto(diaryService.modifyDiary(diaryid, request, imageRequest));
//    }

    @DeleteMapping("/{diaryid}")
    public Boolean DeleteDiary(@PathVariable("diaryid") Long diaryid) {
        return diaryService.deleteDiary(diaryid);
    }
}
