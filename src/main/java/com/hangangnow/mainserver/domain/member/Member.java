package com.hangangnow.mainserver.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.uuid.Generators;
import com.hangangnow.mainserver.domain.member.dto.Gender;
import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.mypage.Memo;
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

    @OneToMany(mappedBy = "member")
    private List<Memo> Memos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Diary> Diaries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberProvider memberProvider;

    private String profileUrl;


//    @OneToOne(fetch = FetchType.LAZY)
//    private RefreshToken refreshToken;


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


    public void updateProfile(String profileUrl){
        this.profileUrl = profileUrl;
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

}
