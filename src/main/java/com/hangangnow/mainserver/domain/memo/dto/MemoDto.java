package com.hangangnow.mainserver.domain.memo.dto;

import com.hangangnow.mainserver.domain.memo.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoDto {

    private Long id;

    @NotEmpty
    private String content;

    @NotEmpty
    @Pattern(regexp = "^#([0-9]|[a-f]|[A-F]){6}$", message = "올바르지 않은 컬러코드 형식 입니다.")
    private String color = "#ffffff";

    @NotEmpty
    @Pattern(regexp = "^(20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "올바르지 않은 날짜 형식 입니다.")
    private String memoDate;

    public MemoDto(Memo memo) {
        this.id = memo.getId();
        this.content = memo.getContent();
        this.color = memo.getColor();
        this.memoDate = memo.getMemoDate().toString();
    }
}