package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.park.Park;
import com.hangangnow.mainserver.domain.park.Parking;
import com.hangangnow.mainserver.domain.park.dto.ParkingMapDto;
import com.hangangnow.mainserver.domain.park.dto.ParkingResponseDto;
import com.hangangnow.mainserver.domain.park.dto.ParkingSimpleDto;
import com.hangangnow.mainserver.repository.ParkRepository;
import com.hangangnow.mainserver.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParkingService {
    private final ParkingRepository parkingRepository;
    private final ParkRepository parkRepository;

    public ParkingResponseDto findOne(Long id){
        Parking findParking = parkingRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Failed: Not found parking"));
        return new ParkingResponseDto(findParking);
    }

    public List<ParkingSimpleDto> findParkParking(Long park_id){
        List<Parking> parkings;
        if(park_id == 12L) {
            parkings = parkingRepository.findAll();
        }else{
            Park findPark = parkRepository.findById(park_id)
                    .orElseThrow(() -> new NullPointerException("Failed: Not found park"));
            parkings = findPark.getParkings();
        }
        return parkings.stream()
                .map(ParkingSimpleDto::new)
                .collect(Collectors.toList());
    }

    public List<ParkingMapDto> findMapParking(){
        return parkingRepository.findAll()
                .stream()
                .map(ParkingMapDto::new)
                .collect(Collectors.toList());
    }

    public List<ParkingResponseDto> findAllParking(){
        return parkingRepository.findAll()
                .stream()
                .map(ParkingResponseDto::new)
                .collect(Collectors.toList());
    }
}
