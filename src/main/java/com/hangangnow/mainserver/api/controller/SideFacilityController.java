package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.sidefacility.dto.FacilityRequestDto;
import com.hangangnow.mainserver.domain.sidefacility.dto.FacilityResponseDto;
import com.hangangnow.mainserver.service.SideFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SideFacilityController {

    private final SideFacilityService sideFacilityService;


    @Operation(summary = "주변시설 위도, 경도에 해당하는 주변시설 내용 조회", description = "위도 경도를 이용한 단일 주변시설 내용 조회.  " +
            "\n**RequestParam: y(위도), x(경도).  " +
            "\n 편의점, 식당, 카페, 주차장은 카카오 API를 직접 호출하기 때문에 제공하지 않습니다.  " +
            "\n 데이터베이스에 존재하는 주변시설 데이터와 일치하지 않는 위도 경도가 요청된 경우, 예외가 발생합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/facilities")
    public ResponseEntity<FacilityResponseDto> getFacilityWithLocation(
            @RequestParam Double x, @RequestParam Double y){
        return new ResponseEntity<>(sideFacilityService.getSideFacilityByXposAndYpos(x, y), HttpStatus.OK);
    }

    @Operation(summary = "현재 위치 기준 편의점, 주차장, 식당, 카페 주변시설 조회", description = "카카오 로컬 API 이용한 주변시설 조회.  " +
            "\n**RequestParam: y(위도), x(경도), category(카테고리)**  " +
            "\n카테고리 코드는 **편의점: CS2, 주차장: PK6, 식당: FD6, 카페:CE7** 입니다. 이외 코드 -> 예외발생  " +
            "\n**요청 예시) /api/v1/facilities?x=127.0781632&y=37.5276908&category=CS2**  " +
            "\n현재 사용자의 위도, 경도 기준으로 1km 이내, 최대 15개 결과를 가까운 순으로 보여줍니다.  " +
            "\n카카오 API를 이용해 조회한 편의점, 주차장, 식당, 카페는 전화번호, 카카오맵 정보 url, 거리까지 응답으로 제공합니다.  "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/facilities/categories")
    public ResponseEntity<GenericResponseDto> getFacilityWithLocation(
            @RequestParam String x, @RequestParam String y, @RequestParam String category){
        return new ResponseEntity<>(sideFacilityService.getFacilities(x, y, category), HttpStatus.OK);
    }

    @Operation(summary = "공원 별 주변시설 조회", description = "해당 공원 카테고리 별 모든 주변시설 조회.  " +
            "\nRequestParam: category(카테고리)  " +
            "\n카테고리 코드는 **TOILET(화장실), SUN_SHADOW(그늘막), BICYCLE(자전거 대여소), VIEW(전망쉼터), DELIVERY_ZONE(배달존) CAFE(카페), RESTAURANT(맛집), " +
            "\n  PARKING(주차장), STORE(편의점), LOAD_FOOD(포장마차), BASKETBALL(농구장), SOCCER(축구장), BASEBALL(야구장), TENNIS(테니스장), SWIM(수영장)**. 없는 코드 -> 예외발생  " +
            "\n**요청 예시) /api/v1/facilities/1?category=TOILET**  " +
            "\n 1: 광나루한강공원  " +
            "\n 2: 잠실한강공원  " +
            "\n 3: 뚝섬한강공원  " +
            "\n 4: 잠원한강공원  " +
            "\n 5: 반포한강공원  " +
            "\n 6: 이촌한강공원  " +
            "\n 7: 망원한강공원  " +
            "\n 8: 여의도한강공원  "   +
            "\n 9: 난지한강공원  " +
            "\n 10: 강서한강공원  " +
            "\n 11: 양화한강공원  "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/facilities/{parkId}")
    public ResponseEntity<GenericResponseDto> getFacilityWithParkIdAndType(
            @PathVariable Long parkId, @RequestParam String category){
        return new ResponseEntity<>(sideFacilityService.getFacilitiesByParkIdAndType(parkId, category), HttpStatus.OK);
    }


//    @Operation(summary = "테스트 용도 (사용 X)", description = "사용 X.  ")
//    @PostMapping("/api/v1/facilities")
//    public ResponseEntity<GenericResponseDto> registerFacilityWithLocation(
//            @RequestBody FacilityRequestDto facilityRequestDto){
//        return new ResponseEntity<>(sideFacilityService.registerFacility(facilityRequestDto), HttpStatus.OK);
//    }

}
