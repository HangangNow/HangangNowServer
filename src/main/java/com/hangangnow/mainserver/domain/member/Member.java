package com.hangangnow.mainserver.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.uuid.Generators;
import com.hangangnow.mainserver.domain.member.dto.Gender;
import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.mypage.Memo;
import com.hangangnow.mainserver.domain.photo.MemberPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member{

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "member_id")
    private UUID id;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private MemberMBTI memberMBTI;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberProvider memberProvider;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private MemberPhoto photo;

    private Boolean marketing_agree;

    private Boolean alarm_agree;



    @Builder
    public Member(String loginId, String email, String password, String name,
                  LocalDate birthday, Gender gender, Authority authority, MemberProvider memberProvider) {
        this.loginId = loginId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.authority = authority;
        this.memberProvider = memberProvider;
    }

    public void createMemberByKakao(String loginId, String email, String password, String name){
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = Authority.ROLE_USER;
        this.memberProvider = MemberProvider.KAKAO;
    }


    public void updatePassword(String password){
        this.password = password;
    }


    public void updateMbti(String mbti){
        this.memberMBTI = fromStringToEmotion(mbti);
    }


    public void updatePhoto(MemberPhoto memberPhoto){
        this.photo = memberPhoto;
    }

    public void updateAlarmAgree(Boolean alarm_agree){
        this.alarm_agree = alarm_agree;
    }


    public void updateMarketingAgree(Boolean marketing_agree){
        this.marketing_agree = marketing_agree;
    }

    @PrePersist
    public void createMemberUniqId() {
        //sequential uuid 생성
        UUID uuid = Generators.timeBasedGenerator().generate();
        String[] uuidArr = uuid.toString().split("-");
        String uuidStr = uuidArr[2]+uuidArr[1]+uuidArr[0]+uuidArr[3]+uuidArr[4];
        StringBuffer sb = new StringBuffer(uuidStr);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        uuid = UUID.fromString(sb.toString());
        this.id = uuid;
    }

    // INFLUENCER, INSIDER, ARTIST, SOCIAL_DISTANCING, ACTIVIST
    static public MemberMBTI fromStringToEmotion(String mbti){
        if(mbti == null){
            return null;
        }

        switch (mbti){
            case "INFLUENCER": return MemberMBTI.INFLUENCER;
            case "INSIDER": return MemberMBTI.INSIDER;
            case "ARTIST": return MemberMBTI.ARTIST;
            case "SOCIAL_DISTANCING": return MemberMBTI.SOCIAL_DISTANCING;
            case "ACTIVIST": return MemberMBTI.ACTIVIST;
            default: return null;
        }
    }

    public int removeDiaries(Diary diary){
        this.diaries.remove(diary);
        return this.diaries.size();
    }

    public int removeOneMemo(Memo memo) {
        this.memos.remove(memo);
        return this.memos.size();
    }

}
