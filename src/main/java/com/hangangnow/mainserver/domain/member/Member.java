package com.hangangnow.mainserver.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

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
    List<Memo> Memos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Diary> Diaries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberProvider memberProvider;


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

}
