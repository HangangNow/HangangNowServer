package com.hangangnow.mainserver.service.myPageService;

import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.mypageRepo.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public Long addDiary(DiaryDto diaryDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("Failed: Not found diary"));
        Diary diary = Diary.of(diaryDto, findMember);
        diaryRepository.save(diary);
        return diary.getId();
    }

    public DiaryDto findOne(Long id){
        return diaryRepository.findById(id)
                .map(DiaryDto::new)
                .orElseThrow(() -> new NullPointerException("Failed: Not found diary"));
    }

    public List<DiaryDto> findAllMemberDiary(){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return findMember.getDiaries()
                .stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    public List<DiaryDto> findAllCalendarDiary(int year, int month) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return diaryRepository.findAllByMemberAndYearAndMonth(findMember, year, month)
                .stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    public List<DiaryDto> findAllDateDiary(LocalDate date) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return diaryRepository.findAllByMemberAndYearAndMonth(findMember, date.getYear(), date.getMonthValue())
                .stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean modifyDiary(Long id, DiaryDto diaryDto){
        Diary findDiary = diaryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found diary"));
        LocalDateTime prevDateTime = findDiary.getLastModifiedDateTime();
        LocalDateTime postDateTime = diaryRepository.update(findDiary, diaryDto);
        return prevDateTime != postDateTime;
    }

    @Transactional
    public Boolean deleteDiary(Long id){
        Diary findDiary = diaryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
        diaryRepository.remove(findDiary);
        return diaryRepository.findById(id).isEmpty();
    }
}
