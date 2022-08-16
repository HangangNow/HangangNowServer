package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.service.SideFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SideFacilityController {

    private final SideFacilityService sideFacilityService;

    @GetMapping("/api/v1/facilities")
    public ResponseEntity<GenericResponseDto> getFacilityWithLocation(
            @RequestParam String x, @RequestParam String y, @RequestParam String category){

        return new ResponseEntity<>(sideFacilityService.getFacility(x, y, category), HttpStatus.OK);
    }
}
