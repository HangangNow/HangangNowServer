package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.event.Event;
import com.hangangnow.mainserver.domain.event.dto.EventRequestDto;
import com.hangangnow.mainserver.domain.event.dto.EventResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.scrap.EventScrap;
import com.hangangnow.mainserver.domain.photo.EventPhoto;
import com.hangangnow.mainserver.domain.photo.ThumbnailPhoto;
import com.hangangnow.mainserver.repository.EventRepository;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final ScrapRepository scrapRepository;
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
        Address address = new Address(eventRequestDto.getAddress());
        Local local = new Local(eventRequestDto.getTitle(), eventRequestDto.getX_pos(), eventRequestDto.getY_pos());

        LocalDate startDate = LocalDate.parse(eventRequestDto.getStartDate(), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(eventRequestDto.getEndDate(), DateTimeFormatter.ISO_DATE);
        Event event = new Event(eventRequestDto.getTitle(), local, address, startDate, endDate, eventRequestDto.getEventTime(), eventRequestDto.getPrice(),
                eventRequestDto.getHost(), eventRequestDto.getManagement(), eventRequestDto.getContent(), thumbnailPhoto, eventPhoto, LocalDateTime.now());

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


    @Transactional
    public EventResponseDto update(Long id, EventRequestDto eventRequestDto, MultipartFile thumbnail, MultipartFile multipartFile) throws IOException {
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다"));

        if (thumbnail != null){
            s3Uploader.delete(findEvent.getThumbnailPhoto().getS3Key());
            ThumbnailPhoto thumbnailPhoto = new ThumbnailPhoto(s3Uploader.upload(thumbnail, "thumbnail"));
            findEvent.updateThumbnailPhoto(thumbnailPhoto);
        }

        if(multipartFile != null){
            s3Uploader.delete(findEvent.getPhoto().getS3Key());
            EventPhoto eventPhoto = new EventPhoto(s3Uploader.upload(multipartFile, "event"));
            findEvent.updateEventPhoto(eventPhoto);
        }

        Address address = new Address(eventRequestDto.getAddress());
        Local local = new Local(eventRequestDto.getTitle(), findEvent.getLocal().getX_pos(), findEvent.getLocal().getY_pos());

        findEvent.update(eventRequestDto, local, address);

        return new EventResponseDto(findEvent);
    }


    public EventResponseDto findOne(Long id){
        Event findEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다."));

        return new EventResponseDto(findEvent);
    }


    public GenericResponseDto findAllEvents(){
        List<EventResponseDto> results = eventRepository.findAllEvents()
                .stream()
                .map(EventResponseDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }


    @Transactional
    public ResponseDto updateScrap(Long eventId){
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        Event findEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));


        EventScrap eventScrap = scrapRepository.findEventScrapByMemberAndEvent(eventId, SecurityUtil.getCurrentMemberId())
                .orElse(new EventScrap());

        if(eventScrap.getEvent() == null){
            eventScrap.addMemberAndEvent(findMember, findEvent);
            return new ResponseDto("해당 이벤트 스크랩 설정이 정상적으로 처리되었습니다.");
        }

        else{
            eventScrap.cancelMemberAndEvent(findMember, findEvent);
            return new ResponseDto("해당 이벤트 스크랩 해제가 정상적으로 처리되었습니다.");
        }

    }

}
