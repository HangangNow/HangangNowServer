package com.hangangnow.mainserver.domain.mypage;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class WithdrawalReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdrawal_reason_id")
    private Long id;

    private Boolean diffculty;
    private Boolean fewerEvents;
    private Boolean anotherApplication;
    private Boolean noCredibility;
    private Boolean noNeed;
    private Boolean etc;

    private String etcContent;
}
