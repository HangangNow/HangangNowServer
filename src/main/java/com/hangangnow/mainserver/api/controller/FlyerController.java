package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerRequestDto;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.service.FlyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class FlyerController {

    private final FlyerService flyerService;

    @PostMapping("/api/v1/flyers")
    public ResponseEntity<FlyerResponseDto> registerFlyer(
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest,
            @RequestPart(value = "jsonData") FlyerRequestDto flyerRequestDto) throws Exception{
        return new ResponseEntity<>(flyerService.save(imageRequest, flyerRequestDto), HttpStatus.CREATED);
    }


    @GetMapping("/api/v1/flyers")
    public ResponseEntity<GenericResponseDto> getAllFlyers(){
        return new ResponseEntity<>(flyerService.findAllFlyers(), HttpStatus.OK);
    }


    @GetMapping("/api/v1/flyers/{flyerId}")
    public ResponseEntity<FlyerResponseDto> getOneFlyer(@PathVariable Long flyerId){
        return new ResponseEntity<>(flyerService.findOneFlyers(flyerId), HttpStatus.OK);
    }


    @GetMapping("/api/v1/flyers/parks/{parkId}")
    public ResponseEntity<GenericResponseDto> getFlyerByParkId(@PathVariable Long parkId){
        return new ResponseEntity<>(flyerService.findAllFlyersByPark(parkId), HttpStatus.OK);
    }



    @PutMapping("/api/v1/flyers/{flyerId}")
    public ResponseEntity<FlyerResponseDto> modifyFlyer(@PathVariable Long flyerId,
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest,
            @RequestPart(value = "jsonData") FlyerRequestDto flyerRequestDto) throws IOException {
        return new ResponseEntity<>(flyerService.update(flyerId, imageRequest, flyerRequestDto), HttpStatus.OK);
    }



    @DeleteMapping("/api/v1/flyers/{flyerId}")
    public ResponseEntity<ResponseDto> deleteFlyer(@PathVariable Long flyerId){
        return new ResponseEntity<>(flyerService.delete(flyerId), HttpStatus.OK);
    }


    @PostMapping("/api/v1/flyers/{flyerId}/scraps")
    public ResponseEntity<ResponseDto> scrapFlyer(@PathVariable Long flyerId){
        return new ResponseEntity<>(flyerService.updateScrap(flyerId), HttpStatus.CREATED);
    }

}
