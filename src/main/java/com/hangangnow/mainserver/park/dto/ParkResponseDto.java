package com.hangangnow.mainserver.park.dto;

import com.hangangnow.mainserver.park.entity.Park;
import com.hangangnow.mainserver.photo.entity.ParkPhoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkResponseDto {

    private Long id;
    private String name;
    private String address;
    private String summary;
    private List<String> photoUrls = new ArrayList<>();
    private List<String> facilities = new ArrayList<>();
    private HashMap<String, List<String>> funItems = new HashMap<>();
    private String content;
    private LocalDateTime lastModifiedTime;

    public ParkResponseDto toParkResponseDto(Park park) {
        this.id = park.getId();
        this.name = park.getName();
        this.address = park.getAddress().fullAddress();
        this.content = park.getContent();
        this.summary = park.getSummary();
        this.lastModifiedTime = park.getLastModifiedTime();

        List<ParkPhoto> photos = park.getPhotos();
        for (ParkPhoto photo : photos) {
            this.photoUrls.add(photo.getUrl());
        }

        String[] facility_strings = park.getFacilities().split("@");
        for (String string : facility_strings) {
            this.facilities.add(string);
        }

        String[] item_strings = park.getFunItems().split("@");
        for (String string : item_strings) {
            String[] split = string.split("<>");

            String key = split[0];
            List<String> hashTags = new ArrayList<>();
            String[] tags = split[1].split("\\s");
            for (String tag : tags) {
                hashTags.add(tag);
            }

            this.funItems.put(key, hashTags);
        }

        return this;
    }
}
