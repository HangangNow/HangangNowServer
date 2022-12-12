package com.hangangnow.mainserver.common.entity;

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

    public Address(String address) {
        String[] addresses = address.split("\\s");
        String si = addresses[0];
        String gu = addresses[1];
        String detail = "";

        for(int i=2; i< addresses.length; i++){
            if (i == addresses.length - 1){
                detail += addresses[i];
            }
            else{
                detail += addresses[i] + " ";
            }
        }


        this.sido = si;
        this.gu = gu;
        this.detail = detail;
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
