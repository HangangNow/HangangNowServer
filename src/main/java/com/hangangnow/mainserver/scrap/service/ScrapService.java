package com.hangangnow.mainserver.scrap.service;

import com.hangangnow.mainserver.event.repository.EventRepository;
import com.hangangnow.mainserver.flyer.repository.FlyerRepository;
import com.hangangnow.mainserver.member.repository.MemberRepository;
import com.hangangnow.mainserver.picnic.repository.PicnicRepository;
import com.hangangnow.mainserver.scrap.repository.ScrapRepository;
import com.hangangnow.mainserver.util.SecurityUtil;
import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.event.entity.Event;
import com.hangangnow.mainserver.event.dto.EventResponseDto;
import com.hangangnow.mainserver.flyer.entity.Flyer;
import com.hangangnow.mainserver.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.scrap.entity.EventScrap;
import com.hangangnow.mainserver.scrap.entity.FlyerScrap;
import com.hangangnow.mainserver.scrap.entity.RecomCourseScrap;
import com.hangangnow.mainserver.scrap.entity.RecomPlaceScrap;
import com.hangangnow.mainserver.picnic.entity.RecomCourse;
import com.hangangnow.mainserver.picnic.entity.RecomPlace;
import com.hangangnow.mainserver.scrap.dto.RecomCourseScrapDto;
import com.hangangnow.mainserver.scrap.dto.RecomPlaceScrapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final FlyerRepository flyerRepository;
    private final PicnicRepository picnicRepository;


    public GenericResponseDto getEventScraps() {
        List<EventResponseDto> results = scrapRepository.findEventScrapsByMemberId(SecurityUtil.getCurrentMemberId())
                .stream()
                .map(EventResponseDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }

    @Transactional
    public ResponseDto updateEventScrap(Long eventId) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        Event findEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        EventScrap eventScrap = scrapRepository.findEventScrapByMemberAndEvent(eventId, SecurityUtil.getCurrentMemberId())
                .orElse(new EventScrap());

        if (eventScrap.getEvent() == null) {
            eventScrap.addMemberAndEvent(findMember, findEvent);
            return new ResponseDto("해당 이벤트 스크랩 설정이 정상적으로 처리되었습니다.");
        } else {
            eventScrap.cancelMemberAndEvent(findMember, findEvent);
            return new ResponseDto("해당 이벤트 스크랩 해제가 정상적으로 처리되었습니다.");
        }
    }


    public GenericResponseDto getFlyerScraps() {
        List<FlyerResponseDto> results = scrapRepository.findFlyerScrapsByMemberId(SecurityUtil.getCurrentMemberId())
                .stream()
                .map(FlyerResponseDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }


    @Transactional
    public ResponseDto updateFlyerScrap(Long flyerId) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        Flyer findFlyer = flyerRepository.findById(flyerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 전단지를 찾을 수 없습니다."));

        FlyerScrap flyerScrap = scrapRepository.findFlyerScrapByMemberAndFlyer(flyerId, SecurityUtil.getCurrentMemberId())
                .orElse(new FlyerScrap());

        if (flyerScrap.getFlyer() == null) {
            flyerScrap.addMemberAndEvent(findMember, findFlyer);
            return new ResponseDto("해당 전단지 스크랩 설정이 정상적으로 처리되었습니다.");
        } else {
            flyerScrap.cancelMemberAndEvent(findMember, findFlyer);
            return new ResponseDto("해당 전단지 스크랩 해제가 정상적으로 처리되었습니다.");
        }
    }


    public GenericResponseDto getRecomCourseScraps() {
        List<RecomCourseScrapDto> results = scrapRepository.findRecomCourseByMemberId(SecurityUtil.getCurrentMemberId())
                .stream()
                .map(RecomCourseScrapDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }


    @Transactional
    public ResponseDto updateRecomCourseScrap(Long recomCourseId) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        RecomCourse recomCourse = picnicRepository.findCourseById(recomCourseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 코스를 찾을 수 없습니다."));

        RecomCourseScrap recomCourseScrap = scrapRepository.findRecomCourseScrapByMemberAndEvent(recomCourseId, SecurityUtil.getCurrentMemberId())
                .orElse(new RecomCourseScrap());

        if (recomCourseScrap.getRecomCourse() == null) {
            recomCourseScrap.addMemberAndRecomCourse(findMember, recomCourse);
            return new ResponseDto("해당 추천코스 스크랩 설정이 정상적으로 처리되었습니다.");
        } else {
            recomCourseScrap.cancelMemberAndRecomCourse(findMember, recomCourse);
            return new ResponseDto("해당 추천코스 스크랩 해제가 정상적으로 처리되었습니다.");
        }
    }


    public GenericResponseDto getRecomPlaceScraps() {
        List<RecomPlaceScrapDto> results = scrapRepository.findRecomPlaceByMemberId(SecurityUtil.getCurrentMemberId())
                .stream()
                .map(RecomPlaceScrapDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }


    @Transactional
    public ResponseDto updateRecomPlaceScrap(Long recomPlaceId) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        RecomPlace recomPlace = picnicRepository.findPlaceById(recomPlaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 추천장소를 찾을 수 없습니다."));

        RecomPlaceScrap recomPlaceScrap = scrapRepository.findRecomPlaceScrapByMemberAndEvent(recomPlaceId, SecurityUtil.getCurrentMemberId())
                .orElse(new RecomPlaceScrap());

        if (recomPlaceScrap.getRecomPlace() == null) {
            recomPlaceScrap.addMemberAndRecomPlace(findMember, recomPlace);
            return new ResponseDto("해당 추천장소 스크랩 설정이 정상적으로 처리되었습니다.");
        } else {
            recomPlaceScrap.cancelMemberAndRecomPlace(findMember, recomPlace);
            return new ResponseDto("해당 추천코스 스크랩 해제가 정상적으로 처리되었습니다.");
        }
    }
}
