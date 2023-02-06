package com.hangangnow.mainserver.event.service;

import com.hangangnow.mainserver.util.SecurityUtil;
import com.hangangnow.mainserver.util.s3.S3Uploader;
import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.event.entity.Event;
import com.hangangnow.mainserver.event.dto.EventRequestDto;
import com.hangangnow.mainserver.event.dto.EventResponseDto;
import com.hangangnow.mainserver.photo.entity.EventPhoto;
import com.hangangnow.mainserver.photo.entity.ThumbnailPhoto;
import com.hangangnow.mainserver.event.repository.EventRepository;
import com.hangangnow.mainserver.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final ScrapRepository scrapRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public EventResponseDto save(@Valid EventRequestDto eventRequestDto, MultipartFile thumbnail, MultipartFile multipartFile) throws IOException {
        if (thumbnail == null) {
            throw new IllegalArgumentException("이벤트 썸네일 이미지는 필수입니다.");
        }

        if (multipartFile == null) {
            throw new IllegalArgumentException("행사 이미지는 필수입니다.");
        }

        Event event = createEvent(eventRequestDto, thumbnail, multipartFile);

        eventRepository.save(event);
        return new EventResponseDto(event);
    }

    private Event createEvent(EventRequestDto eventRequestDto, MultipartFile thumbnail, MultipartFile multipartFile) throws IOException {
        return Event.builder()
                .title(eventRequestDto.getTitle())
                .local(new Local(eventRequestDto.getTitle(), eventRequestDto.getX_pos(), eventRequestDto.getY_pos()))
                .address(new Address(eventRequestDto.getAddress()))
                .startDate(LocalDate.parse(eventRequestDto.getStartDate(), DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse(eventRequestDto.getEndDate(), DateTimeFormatter.ISO_DATE))
                .eventTime(eventRequestDto.getEventTime())
                .price(eventRequestDto.getPrice())
                .host(eventRequestDto.getHost())
                .management(eventRequestDto.getManagement())
                .content(eventRequestDto.getContent())
                .thumbnailPhoto(new ThumbnailPhoto(s3Uploader.upload(thumbnail, "thumbnail")))
                .photo(new EventPhoto(s3Uploader.upload(multipartFile, "event")))
                .lastModifiedTime(LocalDateTime.now())
                .build();
    }

    @Transactional
    public ResponseDto delete(Long id) {
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다"));

        eventRepository.delete(findEvent);

        return new ResponseDto("이벤트를 삭제하였습니다.");
    }


    @Transactional
    public EventResponseDto update(Long id, @Valid EventRequestDto eventRequestDto, MultipartFile thumbnail, MultipartFile multipartFile) throws IOException {
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다"));

        if (thumbnail != null) {
            s3Uploader.delete(findEvent.getThumbnailPhoto().getS3Key());
            ThumbnailPhoto thumbnailPhoto = new ThumbnailPhoto(s3Uploader.upload(thumbnail, "thumbnail"));
            findEvent.updateThumbnailPhoto(thumbnailPhoto);
        }

        if (multipartFile != null) {
            s3Uploader.delete(findEvent.getPhoto().getS3Key());
            EventPhoto eventPhoto = new EventPhoto(s3Uploader.upload(multipartFile, "event"));
            findEvent.updateEventPhoto(eventPhoto);
        }

        Address address = new Address(eventRequestDto.getAddress());
        Local local = new Local(eventRequestDto.getTitle(), findEvent.getLocal().getX_pos(), findEvent.getLocal().getY_pos());

        findEvent.update(eventRequestDto, local, address);

        return new EventResponseDto(findEvent);
    }


    public EventResponseDto findOne(Long id) {
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다."));

        EventResponseDto eventResponseDto = new EventResponseDto(findEvent);
        List<Event> eventScraps = scrapRepository.findEventScrapsByMemberId(SecurityUtil.getCurrentMemberId());

        eventResponseDto.setIsScrap(isScraped(eventScraps, findEvent));
        return eventResponseDto;
    }

    public GenericResponseDto findAllEvents() {
        List<Event> allEvents = eventRepository.findAllEvents();
        List<Event> eventScraps = scrapRepository.findEventScrapsByMemberId(SecurityUtil.getCurrentMemberId());

        List<EventResponseDto> results = new ArrayList<>();

        for (Event event : allEvents) {
            EventResponseDto eventResponseDto = new EventResponseDto(event);
            eventResponseDto.setIsScrap(isScraped(eventScraps, event));
            results.add(eventResponseDto);
        }
        return new GenericResponseDto(results);
    }

    private boolean isScraped(List<Event> events, Event findEvent) {
        if (events.contains(findEvent)) {
            return true;
        }
        return false;
    }
}
