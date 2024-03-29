package com.hangangnow.mainserver.weather.api;

import com.hangangnow.mainserver.weather.entity.HangangNowData;
import com.hangangnow.mainserver.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hangangnow")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("")
    @Operation(summary = "한강나우 정보 조회", description = "한강나우 정보를 조회할 수 있습니다.  \n" +
            "skyMode: 1: 맑음, 2: 구름, 3: 흐림, 4: 비, 5: 소나기, 6: 비눈, 7: 눈비, 8: 눈  \n" +
            "Dust - DustGrade: 1: 좋음, 2: 보통, 3: 나쁨, 4: 매우나쁨")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public HangangNowData getHangangnowData(){
        return weatherService.getHangangnowData();
    }
}
