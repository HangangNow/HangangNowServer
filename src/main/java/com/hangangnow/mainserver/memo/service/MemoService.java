package com.hangangnow.mainserver.memo.service;

import com.hangangnow.mainserver.util.SecurityUtil;
import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.memo.entity.Memo;
import com.hangangnow.mainserver.memo.dto.MemoDto;
import com.hangangnow.mainserver.member.repository.MemberRepository;
import com.hangangnow.mainserver.memo.repository.MemoRepository;
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
    public MemoDto addMemo(MemoDto memoDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("일치하는 멤버를 찾을 수 없습니다."));

        Memo memo = Memo.of(memoDto, findMember);
        memoRepository.save(memo);
        return new MemoDto(memo);
    }

    public MemoDto findOne(Long id) {
        return memoRepository.findById(id)
                .map(MemoDto::new)
                .orElseThrow(() -> new NullPointerException("일치하는 메모를 찾을 수 없습니다."));
    }

    public List<MemoDto> findAllMemberMemo() {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("일치하는 멤버를 찾을 수 없습니다."));

        return findMember.getMemos()
                .stream()
                .map(MemoDto::new)
                .collect(Collectors.toList());
    }

    public List<MemoDto> findAllCalendarMemo(int year, int month) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return memoRepository.findAllByMemberAndYearAndMonth(findMember, year, month)
                .stream()
                .map(MemoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean modifyMemo(Long id, MemoDto memoDto) {
        Memo findMemo = memoRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
        LocalDateTime prevDateTime = findMemo.getLastModifiedTime();
        LocalDateTime postDateTime = memoRepository.update(findMemo, memoDto.getContent(), memoDto.getColor());
        return prevDateTime != postDateTime;
    }

    @Transactional
    public Boolean deleteMemo(Long id) {
        Memo findMemo = memoRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found memo"));
        Member findMember = findMemo.getMember();
        int prevMemosSize = findMember.getMemos().size();
        int postMemosSize = findMember.removeOneMemo(findMemo);
        return prevMemosSize - 1 == postMemosSize;
    }

}
