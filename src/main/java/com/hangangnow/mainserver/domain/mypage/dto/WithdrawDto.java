package com.hangangnow.mainserver.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawDto {
    private Boolean difficulty;
    private Boolean fewerEvents;
    private Boolean anotherApplication;
    private Boolean noCredibility;
    private Boolean noNeed;
    private Boolean etc;

    private String etcContent;
}
