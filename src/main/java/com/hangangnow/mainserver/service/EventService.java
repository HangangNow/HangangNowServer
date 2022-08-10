package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.event.Event;
import com.hangangnow.mainserver.domain.event.dto.EventRequestDto;
import com.hangangnow.mainserver.domain.event.dto.EventResponseDto;
import com.hangangnow.mainserver.domain.photo.EventPhoto;
import com.hangangnow.mainserver.domain.photo.ThumbnailPhoto;
import com.hangangnow.mainserver.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final S3Uploader s3Uploader;


    @Transactional
    public EventResponseDto save(EventRequestDto eventRequestDto, MultipartFile thumbnail, MultipartFile multipartFile) throws IOException {
        if (thumbnail == null){
            throw new IllegalArgumentException("이벤트 썸네일 이미지는 필수입니다.");
        }

        if (multipartFile == null){
            throw new IllegalArgumentException("행사 이미지는 필수입니다.");
        }

        ThumbnailPhoto thumbnailPhoto = new ThumbnailPhoto(s3Uploader.upload(thumbnail, "thumbnail"));
        EventPhoto eventPhoto = new EventPhoto(s3Uploader.upload(multipartFile, "event"));
        Address address = new Address(eventRequestDto.getSi(), eventRequestDto.getGu(), eventRequestDto.getDetail());
        Local local = new Local(eventRequestDto.getTitle(), null, null);

        Event event = new Event(eventRequestDto.getTitle(), local, address, eventRequestDto.getPeriod(), eventRequestDto.getTime(), eventRequestDto.getPrice(),
                eventRequestDto.getHost(), eventRequestDto.getManagement(), eventRequestDto.getContent(), thumbnailPhoto, eventPhoto);

        eventRepository.save(event);

        return new EventResponseDto(event);
    }

    @Transactional
    public ResponseDto delete(Long id){
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다"));

        eventRepository.delete(findEvent);

        return new ResponseDto("이벤트를 삭제하였습니다.");
    }


//    @Transactional
//    public EventResponseDto update(Long id, EventRequestDto eventRequestDto, MultipartFile thumbnail, MultipartFile multipartFile){
//        Event findEvent = eventRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다"));
//
//
//    }


    public EventResponseDto findOne(Long id){
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다."));

        return new EventResponseDto(findEvent);
    }


    public List<EventResponseDto> findAllEvents(){
        return eventRepository.findAllEvents()
                .stream()
                .map(EventResponseDto::new)
                .collect(Collectors.toList());
    }
}
