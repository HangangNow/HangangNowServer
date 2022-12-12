package com.hangangnow.mainserver.park.api;

import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.park.dto.ParkingResponseDto;
import com.hangangnow.mainserver.park.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parkings")
public class ParkingController {
    private final ParkingService parkingService;

    @GetMapping("/{parkingid}")
    @Operation(summary = "단일 주차장 조회", description = "주차장id를 이용해 주차장을 상세 조회할 수 있습니다.  \n" +
            "basicCharge(기본 요금)은 30분 기준이며, null값인 경우도 있습니다.  \n" +
            "intervalCharge는 초과 10분당 기준입니다.  \n" +
            "(참고) 모든 주차장의 주중, 주말 운영시간은 00:00 ~ 23:59로 고정되어 있기 때문에 운영시간 값은 응답값에 포함되어 있지 않습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public ParkingResponseDto ParkingOne(@PathVariable("parkingid") Long parkingid) {
        return parkingService.findOne(parkingid);
    }

    @GetMapping("/parks/{parkid}")
    @Operation(summary = "공월별 주차장 조회", description = "공원id를 이용해 공원별 주차장을 상세 조회할 수 있습니다.\n  " +
            "1: 광나루 한강공원, 2: 잠실 한강공원, 3: 뚝섬 한강공원, 4: 잠원 한강공원, 5:반포 한강공원, 6:이촌 한강공원, 7:망원 한강공원, 8: 여의도 한강공원, 9: 난지 한강공원, 10: 강서 한강공원, 11: 양화 한강공원, 12: 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public GenericResponseDto ParkParkings(@PathVariable("parkid") Long parkid) {
        return new GenericResponseDto(parkingService.findParkParking(parkid));
    }

    @GetMapping("/map")
    @Operation(summary = "전체 주차장 좌표 조회", description = "지도로 찾기에 필요한 모든 주차장의 id와 좌표를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public GenericResponseDto MapParkings() {
        return new GenericResponseDto(parkingService.findMapParking());
    }

    @GetMapping("")
    @Operation(summary = "전체 주차장 조회", description = "모든 주차장을 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public GenericResponseDto Parkings() {
        return new GenericResponseDto(parkingService.findAllParking());
    }
}
