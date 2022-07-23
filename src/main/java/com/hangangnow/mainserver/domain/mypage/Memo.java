package com.hangangnow.mainserver.domain.mypage;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.dto.MemoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDateTime;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemoColor color;

    public void update(String content, String memoColor){
        this.content = content;
        this.color = FromStringToMemoColor(memoColor);
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    static public Memo of(MemoDto memoDto, Member member){
        return Memo.builder()
                .color(FromStringToMemoColor(memoDto.getColor()))
                .content(memoDto.getContent())
                .lastModifiedDateTime(LocalDateTime.now())
                .date(LocalDate.parse(memoDto.getDate(), DateTimeFormatter.ISO_DATE))
                .member(member)
                .build();
    }

    static public MemoColor FromStringToMemoColor(String stringColor){
        switch (stringColor){
            case "RED" : return MemoColor.RED;
            case "ORANGE" : return MemoColor.ORANGE;
            case "YELLOW" : return MemoColor.YELLOW;
            case "GREEN" : return MemoColor.GREEN;
            case "BLUE" : return MemoColor.BLUE;
            case "NAVY" : return MemoColor.NAVY;
            case "PURPLE" : return MemoColor.PURPLE;
            default: return MemoColor.GRAY;
        }
    }
}
