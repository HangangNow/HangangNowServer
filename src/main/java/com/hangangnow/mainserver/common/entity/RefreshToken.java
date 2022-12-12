package com.hangangnow.mainserver.common.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private UUID key;

    @Column(name = "rt_value")
    private String value;

    @Builder
    public RefreshToken(UUID key, String value){
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String value){
        this.value = value;
        return this;
    }
}
