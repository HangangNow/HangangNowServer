package com.hangangnow.mainserver.scrap.api;

import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @Operation(summary = "멤버 이벤트 스크랩 조회", description = "멤버 이벤트 스크랩 조회 URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @GetMapping("/api/v1/scraps/events")
    public ResponseEntity<GenericResponseDto> getEventScrapInfo() {
        return new ResponseEntity<>(scrapService.getEventScraps(), HttpStatus.OK);
    }


    @Operation(summary = "이벤트 스크랩 설정 or 해제", description = "이벤트를 스크랩 설정하거나 해제할 수 있습니다.  " +
            "\n스크랩 하지 않은 이벤트를 스크랩 하는 경우 -> 스크랩 설정  " +
            "\n스크랩 된 이벤트를 한번 더 요청하는 경우 -> 스크랩 해제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @PostMapping("/api/v1/scraps/events/{eventId}")
    public ResponseEntity<ResponseDto> createScarpEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(scrapService.updateEventScrap(eventId), HttpStatus.OK);
    }


    @Operation(summary = "멤버 전단지 스크랩 조회", description = "멤버 전단지 스크랩 조회 URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @GetMapping("/api/v1/scraps/flyers")
    public ResponseEntity<GenericResponseDto> getFlyerScrapInfo() {
        return new ResponseEntity<>(scrapService.getFlyerScraps(), HttpStatus.OK);
    }


    @Operation(summary = "단일 전단지 스크랩", description = "단일 전단지를 스크랩할 수 있습니다.  " +
            "\n스크랩 하지 않은 이벤트를 스크랩 하는 경우 -> 스크랩 설정  " +
            "\n스크랩 된 이벤트를 한번 더 요청하는 경우 -> 스크랩 해제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @PostMapping("/api/v1/scraps/flyers/{flyerId}")
    public ResponseEntity<ResponseDto> scrapFlyer(@PathVariable Long flyerId) {
        return new ResponseEntity<>(scrapService.updateFlyerScrap(flyerId), HttpStatus.OK);
    }


    @Operation(summary = "멤버 추천코스 스크랩 조회", description = "멤버 추천코스 스크랩 조회 URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @GetMapping("/api/v1/scraps/recomCourses")
    public ResponseEntity<GenericResponseDto> getRecomCourseScrapInfo() {
        return new ResponseEntity<>(scrapService.getRecomCourseScraps(), HttpStatus.OK);
    }


    @Operation(summary = "단일 추천코스 스크랩", description = "단일 추천코스를 스크랩할 수 있습니다.  " +
            "\n스크랩 하지 않은 추천코스를 스크랩 하는 경우 -> 스크랩 설정  " +
            "\n스크랩 된 추천코스를 한번 더 요청하는 경우 -> 스크랩 해제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @PostMapping("/api/v1/scraps/recomCourses/{recomCourseId}")
    public ResponseEntity<ResponseDto> scrapRecomCourse(@PathVariable Long recomCourseId) {
        return new ResponseEntity<>(scrapService.updateRecomCourseScrap(recomCourseId), HttpStatus.OK);
    }


    @Operation(summary = "멤버 추천장소 스크랩 조회", description = "멤버 추천장소 스크랩 조회 URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @GetMapping("/api/v1/scraps/recomPlaces")
    public ResponseEntity<GenericResponseDto> getRecomPlaceScrapInfo() {
        return new ResponseEntity<>(scrapService.getRecomPlaceScraps(), HttpStatus.OK);
    }


    @Operation(summary = "단일 추천장소 스크랩", description = "단일 추천장소를 스크랩할 수 있습니다.  " +
            "\n스크랩 하지 않은 추천장소를 스크랩 하는 경우 -> 스크랩 설정  " +
            "\n스크랩 된 추천장소를 한번 더 요청하는 경우 -> 스크랩 해제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @PostMapping("/api/v1/scraps/recomPlaces/{recomPlaceId}")
    public ResponseEntity<ResponseDto> scrapRecomPlace(@PathVariable Long recomPlaceId) {
        return new ResponseEntity<>(scrapService.updateRecomPlaceScrap(recomPlaceId), HttpStatus.OK);
    }
}
