package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Memo;
import com.hangangnow.mainserver.domain.mypage.dto.MemoDto;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MemoDto addMemo(MemoDto memoDto){
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
        Memo memo = Memo.of(memoDto, findMember);
        memoRepository.save(memo);
        return new MemoDto(memo);
    }

    public MemoDto findOne(Long id) {
        return memoRepository.findById(id)
                .map(MemoDto::new)
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
    }

    public List<MemoDto> findAllMemberMemo() {
        UUID currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return findMember.getMemos()
                .stream()
                .map(MemoDto::new)
                .collect(Collectors.toList());
    }

    public List<MemoDto> findAllCalendarMemo(int year, int month) {
        UUID currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return memoRepository.findAllByMemberAndYearAndMonth(findMember, year, month)
                .stream()
                .map(MemoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean modifyMemo(Long id, MemoDto memoDto){
        Memo findMemo = memoRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
        LocalDateTime prevDateTime = findMemo.getLastModifiedTime();
        LocalDateTime postDateTime = memoRepository.update(findMemo, memoDto.getContent(), memoDto.getColor());
        return prevDateTime != postDateTime;
    }

    @Transactional
    public Boolean deleteMemo(Long id){
        Memo findMemo = memoRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
        Member findMember = findMemo.getMember();
        int prevMemosSize = findMember.getMemos().size();
        int postMemosSize = findMember.removeOneMemo(findMemo);
        return prevMemosSize-1 == postMemosSize;
    }

}
