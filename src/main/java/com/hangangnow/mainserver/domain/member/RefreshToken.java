package com.hangangnow.mainserver.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private Long key;

    @Column(name = "rt_value")
    private String value;

    @Builder
    public RefreshToken(Long key, String value){
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String value){
        this.value = value;
        return this;
    }
}
