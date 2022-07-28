package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.IdResponseDto;
import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDateRequestDto;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import com.hangangnow.mainserver.service.myPageService.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/{diaryid}")
    @Operation(summary = "단일 일기 조회", description = "일기id를 이용해 일기를 상세 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public DiaryDto DiaryOne(@PathVariable("diaryid") Long diaryid){
        return diaryService.findOne(diaryid);
    }

    @GetMapping("")
    @Operation(summary = "멤버 전체 일기 조회", description = "멤버의 모든 일기 리스트 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public GenericResponseDto Diaries(){
        return new GenericResponseDto(diaryService.findAllMemberDiary());
    }

    @GetMapping("/years/{year}/months/{month}")
    @Operation(summary = "멤버 달력 일기 조회", description = "년도, 월 별로 멤버의 달력 일기 리스트를 조회할 수 있습니다.  " +
            "\nyear range: 2000~2099  " +
            "\nmonth range: 1~12")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public GenericResponseDto CalendarDiaries(@PathVariable("year") @Min(2000) @Max(2099) int year,
                                              @PathVariable("month") @Min(1) @Max(12) int month) {
        return new GenericResponseDto(diaryService.findAllCalendarDiary(year, month));
    }

    @Operation(summary = "멤버 일자 일기 조회(해당 일자 일기 모아보기)", description = "일자로 멤버의 달력 일기 리스트를 조회할 수 있습니다.  " +
            "\ndate string form: 20xx-xx-xx  " +
            "정규식: ^(20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/date")
    public GenericResponseDto DateDiaries(@Valid @RequestBody DiaryDateRequestDto diaryDateRequestDto){
        return new GenericResponseDto(diaryService.findAllDateDiary(diaryDateRequestDto));
    }

    @PostMapping("")
    @Operation(summary = "일기 추가", description = "일기를 추가할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청, 추가된 diary id 응답  " +
            "\nkey: jsonData(필수), multipartData(선택)  " +
            "\njsonData의 title, content, diaryDate 필드는 필수 값, emotion, diaryWeather는 선택 값 입니다.  " +
            "\n주의 - jsonData content-type application/json으로 설정  \n  " +
            "\njsonData example: { \"title\":\"거짓말\", \"content\":\"즐거운 개발\", \"diaryDate\": \"2022-07-25\", \"emotion\": \"ANGRY\"}  " +
            "\nemotion type: EXCITED, LOVELY, BIG_SMILE, FUNNY, PEACEFUL, SMILE, SAD, ANGRY, FROWNING, DIZZY  " +
            "\ndiaryWeather type: SUN, SUN_CLOUD, CLOUD, CLOUD_RAIN, CLOUD_SNOW, SNOWMAN, UMBRELLA, WIND")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public IdResponseDto addDiary(@Valid @RequestPart(value = "jsonData") DiaryDto request,
                                  @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws Exception {
        return new IdResponseDto(diaryService.addDiary(request, imageRequest));
    }

    @PutMapping("/{diaryid}")
    @Operation(summary = "일기 수정", description = "일기를 수정할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청, 수정 성공 여부를 Boolean으로 응답  " +
            "\nkey: jsonData(필수), multipartData(선택)  " +
            "\njsonData의 title, content, diaryDate 필드는 필수 값, emotion, diaryWeather는 선택 값 입니다.  \n  " +
            "\n사진이 있는 일기에 사진을 지우고 싶은 경우, url을 비우고 jsonData만 요청  " +
            "\n사진을 바꾸고 싶은 경우 & 사진이 없는 일기에 사진을 추가하고 싶은 경우, multipartData에 파일데이터를 포함하여 요청  " +
            "\n이외 다른 일기의 데이터를 수정하고 싶은 경우는 jsonData필드에 원하는 값을 수정  \n  " +
            "\n주의 - jsonData content-type application/json으로 설정  ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public Boolean ModifyDiary( @PathVariable("diaryid") Long diaryid,
                                @Valid @RequestPart(value = "jsonData") DiaryDto request,
                                @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws Exception {
        return diaryService.modifyDiary(diaryid, request, imageRequest);
    }


    @DeleteMapping("/{diaryid}")
    @Operation(summary = "일기 삭제", description = "일기를 삭제할 수 있습니다.  " +
            "\n삭제 성공 여부를 Boolean으로 응답")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public Boolean DeleteDiary(@PathVariable("diaryid") Long diaryid) {
        return diaryService.deleteDiary(diaryid);
    }

}

