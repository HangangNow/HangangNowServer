package com.hangangnow.mainserver.member.entity;

import java.util.Arrays;

public enum MemberMBTI {

    INFLUENCER("INFLUENCER"),
    EXCITED("EXCITED"),
    ARTIST("ARTIST"),
    SOCIAL_DISTANCING("SOCIAL_DISTANCING"),
    ACTIVIST("ACTIVIST"),
    PLANNER("PLANNER"),
    EXPLORER("EXPLORER"),
    STARGAZER("STARGAZER");

    private final String mbti;

    MemberMBTI(String mbti) {
        this.mbti = mbti;
    }

    public static MemberMBTI getMemberMBTI(String mbti) {
        return Arrays.stream(values())
                .filter(memberMBTI -> memberMBTI.mbti.equals(mbti))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("MBTI 입력 값을 확인하세요."));
    }
}
