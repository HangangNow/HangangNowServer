package com.hangangnow.mainserver.park.service;

import com.hangangnow.mainserver.util.DistanceUtil;
import com.hangangnow.mainserver.park.entity.Park;
import com.hangangnow.mainserver.park.dto.NearestParkDto;
import com.hangangnow.mainserver.park.dto.ParkResponseDto;
import com.hangangnow.mainserver.park.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParkService {

    private final ParkRepository parkRepository;

    @Transactional
    public Long addPark(Park park) {
        parkRepository.save(park);
        return park.getId();
    }

    public ParkResponseDto findOne(Long parkId) {
        Park findPark = parkRepository.findParkInfoById(parkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공원을 찾을 수 없습니다."));
        ParkResponseDto parkResponseDto = new ParkResponseDto();
        return parkResponseDto.toParkResponseDto(findPark);
    }

    public List<Park> findAll() {
        return parkRepository.findAll();
    }

    public NearestParkDto findNearestPark(Double x, Double y) {
        HashMap<Park, Double> map = new HashMap<>();

        List<Park> allParks = parkRepository.findAll();
        for (Park park : allParks) {
            double distance = DistanceUtil.distance(x, y, park.getLocal().getX_pos(), park.getLocal().getY_pos());
            map.put(park, distance);
        }

        List<Map.Entry<Park, Double>> entryList = new LinkedList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());

        Park park = entryList.get(0).getKey();

        return new NearestParkDto(park.getId(), park.getName(), park.getLocal().getX_pos(), park.getLocal().getY_pos());

    }
}
