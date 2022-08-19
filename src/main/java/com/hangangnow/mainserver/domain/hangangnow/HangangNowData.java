package com.hangangnow.mainserver.domain.hangangnow;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HangangNowData {
    @Id
    private Long id;
    private LocalDateTime lastModifiedTime;
    private Double temperature;

    @Embedded
    private Weather weather;
    @Embedded
    private Dust dust;
    @Embedded
    private SunMoonRiseSet sunMoonRiseSet;

}

