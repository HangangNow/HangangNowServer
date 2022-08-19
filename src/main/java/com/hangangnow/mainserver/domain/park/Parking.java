package com.hangangnow.mainserver.domain.park;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.park.Park;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Address address;

    @Embedded
    private Local local;

    private int total_count;
    private int available_count;

    //기본 30분, 간격 10분 기준
    private String basicCharge;
    private String intervalCharge;
    private String fulldayCharge;

    private LocalDateTime lastModifiedTime;

    public void setPark(Park park){
        this.park = park;
    }

}
