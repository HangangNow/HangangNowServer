package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.park.Park;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class ParkPhoto extends Photo {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    @Column(unique = true)
    private String imageNumber;


    public ParkPhoto(String url, String imageNumber, LocalDateTime lastModifiedTime){
        this.url = url;
        this.imageNumber = imageNumber;
        this.lastModifiedTime = lastModifiedTime;
    }


    public void setPark(Park park) {
        if(this.park != null){
            this.park.getPhotos().remove(this);
        }

        this.park = park;
    }

    public void updateParkPhoto(String url, LocalDateTime lastModifiedTime){
        this.url = url;
        this.lastModifiedTime = lastModifiedTime;
    }
}
