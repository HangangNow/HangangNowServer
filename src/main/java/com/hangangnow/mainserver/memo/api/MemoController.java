package com.hangangnow.mainserver.memo.api;

import com.hangangnow.mainserver.memo.dto.MemoDto;
import com.hangangnow.mainserver.memo.service.MemoService;
import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memos")
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/{memoId}")
    @Operation(summary = "단일 메모 조회", description = "메모id를 이용해 메모를 상세 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    public MemoDto getOneMemo(@PathVariable Long memoId) {
        return memoService.findOne(memoId);
    }

    @GetMapping("")
    @Operation(summary = "멤버의 모든 메모 조회", description = "멤버의 모든 메모 리스트 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    public GenericResponseDto getMemos() {
        return new GenericResponseDto(memoService.findAllMemberMemo());
    }

    @GetMapping("/years/{year}/months/{month}")
    @Operation(summary = "멤버 달력 메모 조회", description = "년도, 월 별로 멤버의 달력 메모 리스트를 조회할 수 있습니다.  " +
            "\nyear range: 2000~2099  " +
            "\nmonth range: 1~12")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public GenericResponseDto getMemosByYearAndMonth(@PathVariable("year") @Min(2000) @Max(2099) int year,
                                                     @PathVariable("month") @Min(1) @Max(12) int month) {
        return new GenericResponseDto(memoService.findAllCalendarMemo(year, month));
    }

    @PostMapping("")
    @Operation(summary = "메모 추가", description = "메모를 추가할 수 있습니다.  " +
            "\n content, memoDate 값은 필수 값 입니다.  " +
            "\n color default: #ffffff(white)  " +
            "\n color Valid condition: ^#([0-9]|[a-f]|[A-F]){6}$")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    public MemoDto addMemo(@RequestBody @Valid MemoDto request) {
        return memoService.addMemo(request);
    }

    @PutMapping("/{memoid}")
    @Operation(summary = "메모 수정", description = "메모를 수정할 수 있습니다.  "
            + "\n모든 키 값은 필수 값 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    public Boolean modifyMemo(@PathVariable Long memoId, @RequestBody MemoDto request) {
        return memoService.modifyMemo(memoId, request);
    }

    @DeleteMapping("/{memoId}")
    @Operation(summary = "메모 삭제", description = "메모를 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    public Boolean deleteMemo(@PathVariable Long memoId) {
        return memoService.deleteMemo(memoId);
    }

}
