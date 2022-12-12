package com.hangangnow.mainserver.memo.entity;

import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.memo.dto.MemoDto;
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
    private LocalDate memoDate;

    @Column(nullable = false)
    private LocalDateTime lastModifiedTime;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String color;

    public void update(String content, String memoColor){
        this.content = content;
        this.color = memoColor;
        this.lastModifiedTime = LocalDateTime.now();
    }

    static public Memo of(MemoDto memoDto, Member member){
        return Memo.builder()
                .color(memoDto.getColor())
                .content(memoDto.getContent())
                .lastModifiedTime(LocalDateTime.now())
                .memoDate(LocalDate.parse(memoDto.getMemoDate(), DateTimeFormatter.ISO_DATE))
                .member(member)
                .build();
    }

}
