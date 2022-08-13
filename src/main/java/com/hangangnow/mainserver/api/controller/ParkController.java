package com.hangangnow.mainserver.api.controller;


import com.hangangnow.mainserver.domain.park.dto.ParkResponseDto;
import com.hangangnow.mainserver.service.ParkService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks")
public class ParkController {

    private final ParkService parkService;

    @GetMapping("/{parkId}")
    public ResponseEntity<ParkResponseDto> getParkInfoById(@PathVariable Long parkId){
        return new ResponseEntity<>(parkService.findOne(parkId), HttpStatus.OK);
    }

}
