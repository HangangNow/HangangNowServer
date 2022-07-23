package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.mypage.dto.MemoDto;
import com.hangangnow.mainserver.service.myPageService.MemoService;
import com.hangangnow.mainserver.domain.common.genericResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage/memos")
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/{memoid}")
    @Operation(summary = "단일 메모 조회", description = "메모id를 이용해 메모를 상세 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public MemoDto memoOne(@PathVariable Long memoid){
       return memoService.findOne(memoid);
    }

    @GetMapping("")
    @Operation(summary = "멤버의 모든 메모 조회", description = "멤버의 모든 메모 리스트 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public genericResponseDto Memos(){
        return new genericResponseDto(memoService.findAllMemberMemo());
    }

    @GetMapping("/years/{year}/months/{month}")
    @Operation(summary = "멤버의 모든 메모 조회", description = "멤버의 모든 메모 리스트 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public genericResponseDto CalendarMemos(@PathVariable int year, @PathVariable int month){
        return new genericResponseDto(memoService.findAllCalendarMemo(year, month));
    }

    @PostMapping("")
    @Operation(summary = "메모 추가", description = "메모를 추가할 수 있습니다.  "
            +"\n모든 키 값은 필수 값 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public genericResponseDto AddMemo(@RequestBody @Valid MemoDto request) {
        return new genericResponseDto(memoService.addMemo(request));
    }

    @PutMapping("/{memoid}")
    @Operation(summary = "메모 수정", description = "메모를 수정할 수 있습니다.  "
            +"\n모든 키 값은 필수 값 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public genericResponseDto ModifyMemo(@PathVariable Long memoid, @RequestBody MemoDto request) {
        return new genericResponseDto(memoService.modifyMemo(memoid, request));
    }

    @DeleteMapping("/{memoid}")
    @Operation(summary = "메모 삭제", description = "메모를 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public genericResponseDto DeleteMemo(@PathVariable Long memoid) {
        return new genericResponseDto(memoService.deleteMemo(memoid));
    }

}
