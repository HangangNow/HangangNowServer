package com.hangangnow.mainserver.domain.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class WithdrawalReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdrawal_reason_id")
    private Long id;

    private Boolean difficulty;
    private Boolean fewerEvents;
    private Boolean anotherApplication;
    private Boolean noCredibility;
    private Boolean noNeed;
    private Boolean etc;

    private String etcContent;

    public WithdrawalReason(Boolean difficulty, Boolean fewerEvents, Boolean anotherApplication, Boolean noCredibility,
                            Boolean noNeed, Boolean etc, String etcContent) {
        this.difficulty = difficulty;
        this.fewerEvents = fewerEvents;
        this.anotherApplication = anotherApplication;
        this.noCredibility = noCredibility;
        this.noNeed = noNeed;
        this.etc = etc;
        this.etcContent = etcContent;
    }
}
