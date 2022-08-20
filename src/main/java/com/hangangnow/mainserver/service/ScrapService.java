package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.event.Event;
import com.hangangnow.mainserver.domain.event.dto.EventResponseDto;
import com.hangangnow.mainserver.domain.flyer.Flyer;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.scrap.EventScrap;
import com.hangangnow.mainserver.domain.mypage.scrap.FlyerScrap;
import com.hangangnow.mainserver.repository.EventRepository;
import com.hangangnow.mainserver.repository.FlyerRepository;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.ScrapRepository;
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


    public GenericResponseDto getEventScraps(){
        List<EventResponseDto> results = scrapRepository.findEventScrapsByMemberId(SecurityUtil.getCurrentMemberId())
                .stream()
                .map(EventResponseDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }

    @Transactional
    public ResponseDto updateEventScrap(Long eventId){
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


    public GenericResponseDto getFlyerScraps(){
        List<FlyerResponseDto> results = scrapRepository.findFlyerScrapsByMemberId(SecurityUtil.getCurrentMemberId())
                .stream()
                .map(FlyerResponseDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }


    @Transactional
    public ResponseDto updateFlyerScrap(Long flyerId){
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        Flyer findFlyer = flyerRepository.findById(flyerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 전단지를 찾을 수 없습니다."));

        FlyerScrap flyerScrap = scrapRepository.findFlyerScrapByMemberAndEvent(flyerId, SecurityUtil.getCurrentMemberId())
                .orElse(new FlyerScrap());

        if(flyerScrap.getFlyer() == null){
            flyerScrap.addMemberAndEvent(findMember, findFlyer);
            return new ResponseDto("해당 전단지 스크랩 설정이 정상적으로 처리되었습니다.");
        }

        else {
            flyerScrap.cancelMemberAndEvent(findMember, findFlyer);
            return new ResponseDto("해당 전단지 스크랩 해제가 정상적으로 처리되었습니다.");
        }

    }
}
