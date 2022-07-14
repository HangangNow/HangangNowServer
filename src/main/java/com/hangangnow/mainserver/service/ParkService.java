package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.Park;
import com.hangangnow.mainserver.domain.photo.ParkPhoto;
import com.hangangnow.mainserver.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Park findOne(Long parkId) {
        return parkRepository.findOne(parkId);
    }

    public List<Park> findAll(){
        return parkRepository.findAll();
    }

//    @Transactional
//    public void updatePark(Park park){
//        Park findPark = parkRepository.findOne(park.getId());
//        findPark.setName(park.getName());
//        findPark.setAddress(park.getAddress());
//        findPark.setDescribe(park.getDescribe());
//        findPark.setLocal(park.getLocal());
//        포토는 일단 보류
//        for (ParkPhoto photo : findPark.getPhotos()) {}
//    }

}
