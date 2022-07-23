package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.member.MemberMBTI;
import com.hangangnow.mainserver.domain.photo.RecomPlacePhoto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
public class RecomPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recom_place_id")
    private Long id;
    private String name;

    @Embedded
    private Local local;

    @Embedded
    private Address address;
    private String content;

    @Enumerated(EnumType.STRING)
    private MemberMBTI mbti;

    @OneToMany(mappedBy = "recomPlace")
    private List<RecomPlacePhoto> photos;

}
