package com.hangangnow.mainserver.api.controller;


import com.hangangnow.mainserver.domain.park.dto.NearestParkDto;
import com.hangangnow.mainserver.domain.park.dto.ParkResponseDto;
import com.hangangnow.mainserver.service.ParkService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks")
public class ParkController {

    private final ParkService parkService;

    @Operation(summary = "단일 공원 조회", description = "단일 공원를 조회할 수 있습니다.  " +
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
            "\n 11: 양화한강공원  ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/{parkId}")
    public ResponseEntity<ParkResponseDto> getParkInfoById(@PathVariable Long parkId){
        return new ResponseEntity<>(parkService.findOne(parkId), HttpStatus.OK);
    }


    @Operation(summary = "현위치 기준 가장 가까운 한강공원 조회", description = "가장 가까운 한강공원 조회할 수 있습니다.  " +
            "\n requestParam: y(위도), x(경도)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/nearest")
    public ResponseEntity<NearestParkDto> getNearestPark(@RequestParam Double x, @RequestParam Double y){
        return new ResponseEntity<>(parkService.findNearestPark(x, y), HttpStatus.OK);
    }

}
