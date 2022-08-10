package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.event.dto.EventRequestDto;
import com.hangangnow.mainserver.domain.event.dto.EventResponseDto;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import com.hangangnow.mainserver.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/api/v1/events")
    public ResponseEntity<EventResponseDto> registerEvent(
            @RequestPart(value = "jsonData") EventRequestDto eventRequestDto,
            @Valid @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws IOException {
        return new ResponseEntity<>(eventService.save(eventRequestDto, thumbnail, imageRequest), HttpStatus.CREATED);
    }


    @GetMapping("/api/v1/events")
    public ResponseEntity<List<EventResponseDto>> getAllEvents(){
        return new ResponseEntity<>(eventService.findAllEvents(), HttpStatus.OK);
    }


    @GetMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long eventId){
        return new ResponseEntity<>(eventService.findOne(eventId), HttpStatus.OK);
    }


//    @PutMapping("/api/v1/events/{eventId}")
//    public ResponseEntity<EventResponseDto> modifyEventById(@PathVariable Long eventId,
//            @RequestPart(value = "jsonData") EventRequestDto eventRequestDto,
//            @Valid @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
//            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest){
//        return new ResponseEntity<>(eventService.update(eventId, eventRequestDto, thumbnail, imageRequest), HttpStatus.OK);
//    }


    @DeleteMapping("/api/v1/events/{eventId}")
    public ResponseEntity<ResponseDto> deleteEvent(@PathVariable Long eventId){
        return new ResponseEntity<>(eventService.delete(eventId), HttpStatus.OK);
    }



}
