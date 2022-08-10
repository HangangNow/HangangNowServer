package com.hangangnow.mainserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String sido;
    private String gu;
    private String detail;

    public Address(){
    }

    public String fullAddress(){
        return this.getSido() + " " + this.getGu() + " " + this.getDetail();
    }

    public Address(String sido, String gu, String gil) {
        this.sido = sido;
        this.gu = gu;
    }

    public Address(String sido, String gu, String gil, String detail) {
        this.sido = sido;
        this.gu = gu;
        this.detail = detail;
    }
}
