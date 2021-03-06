package com.hangangnow.mainserver.service.myPageService;

import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDateRequestDto;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.mypageRepo.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public Long addDiary(DiaryDto diaryDto, MultipartFile multipartFile) throws IOException {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("Failed: Not found member"));

        Diary diary = Diary.of(diaryDto, findMember);

        if(multipartFile != null){
            DiaryPhoto diaryPhoto = new DiaryPhoto(s3Uploader.upload(multipartFile, "diary"));
            diary.updateDiaryPhoto(diaryPhoto);
        }

        diaryRepository.save(diary);
        return diary.getId();
    }

    public DiaryDto findOne(Long id){
        return diaryRepository.findById(id)
                .map(DiaryDto::new)
                .orElseThrow(() -> new NullPointerException("Failed: Not found diary"));
    }

    public List<DiaryDto> findAllMemberDiary(){
        UUID currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return findMember.getDiaries()
                .stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    public List<DiaryDto> findAllCalendarDiary(int year, int month) {
        UUID currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));

        return diaryRepository.findAllByMemberAndYearAndMonth(findMember, year, month)
                .stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    public List<DiaryDto> findAllDateDiary(DiaryDateRequestDto diaryDateRequestDto) {
        UUID currentMemberId = SecurityUtil.getCurrentMemberId();
        log.info(currentMemberId.toString());
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Fail: Not found member"));
        LocalDate date = LocalDate.parse(diaryDateRequestDto.getDate(), DateTimeFormatter.ISO_DATE);
        return diaryRepository.findAllByMemberAndDate(findMember, date.getYear(), date.getMonthValue(), date.getDayOfMonth())
                .stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @param diaryDto
     * @param multipartFile
     * @return update success of failure
     * @throws IOException
     * current method: ???????????? ???????????? ?????? ???????????? ??????
     * improvement plan: s3 file version?????? ???????????? ???????????? ?????? ???????????? ??????
     * @comment: ????????? ?????? ????????? ???????????? ??????, request url??? ?????? ?????? url??? ?????? ????????? ?????? ???????????? ????????? ????????? ????????? ??? ???????????? ????????? ?????? ?????????. dto contents ??????
     *           ??????????????? ????????????????????? ????????????, ?????? ????????? ????????? ????????? ???????????? ?????? or ?????? ????????? ????????? ??????????????? ?????? ??????
     */
    @Transactional
    public Boolean modifyDiary(Long id, DiaryDto diaryDto, MultipartFile multipartFile) throws IOException {
        Diary findDiary = diaryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found diary"));
        if(multipartFile != null) {
            DiaryPhoto diaryPhoto = new DiaryPhoto(s3Uploader.upload(multipartFile, "diary"));
            if(findDiary.getPhoto() != null) {
                String PreviousKey = findDiary.getPhoto().getS3Key();
                s3Uploader.delete(PreviousKey);
            }
            findDiary.updateDiaryPhoto(diaryPhoto);
        }
        else {
            if(findDiary.getPhoto() != null && diaryDto.getUrl() == null){
                String PreviousKey = findDiary.getPhoto().getS3Key();
                s3Uploader.delete(PreviousKey);
                findDiary.updateDiaryPhoto(null);
            }
        }

        LocalDateTime prevDateTime = findDiary.getLastModifiedDateTime();
        LocalDateTime postDateTime = diaryRepository.update(findDiary, diaryDto);

        return prevDateTime != postDateTime;
    }

    @Transactional
    public Boolean deleteDiary(Long id){
        Diary findDiary = diaryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found diary"));
        if(findDiary.getPhoto() != null) s3Uploader.delete(findDiary.getPhoto().getS3Key());
        diaryRepository.remove(findDiary);
        return diaryRepository.findById(id).isEmpty();
    }
}
