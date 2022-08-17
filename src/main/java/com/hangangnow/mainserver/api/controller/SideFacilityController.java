package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.sidefacility.dto.FacilityRequestDto;
import com.hangangnow.mainserver.service.SideFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SideFacilityController {

    private final SideFacilityService sideFacilityService;

    @GetMapping("/api/v1/facilities")
    public ResponseEntity<GenericResponseDto> getFacilityWithLocation(
            @RequestParam String x, @RequestParam String y, @RequestParam String category){
        return new ResponseEntity<>(sideFacilityService.getFacilities(x, y, category), HttpStatus.OK);
    }


    @PostMapping("/api/v1/facilities")
    public ResponseEntity<GenericResponseDto> registerFacilityWithLocation(
            @RequestBody FacilityRequestDto facilityRequestDto){
        return new ResponseEntity<>(sideFacilityService.registerFacility(facilityRequestDto), HttpStatus.OK);
    }


    @GetMapping("/api/v1/facilities/{parkId}")
    public ResponseEntity<GenericResponseDto> getFacilityWithParkIdAndType(
            @PathVariable Long parkId, @RequestParam String category){
        return new ResponseEntity<>(sideFacilityService.getFacilitiesByParkIdAndType(parkId, category), HttpStatus.OK);
    }
}
