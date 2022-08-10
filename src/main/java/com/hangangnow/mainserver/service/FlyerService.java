package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.flyer.Flyer;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerRequestDto;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.domain.park.Park;
import com.hangangnow.mainserver.domain.photo.FlyerPhoto;
import com.hangangnow.mainserver.repository.FlyerRepository;
import com.hangangnow.mainserver.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlyerService {

    private final FlyerRepository flyerRepository;
    private final ParkRepository parkRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public FlyerResponseDto save(MultipartFile multipartFile, FlyerRequestDto flyerRequestDto) throws IOException {
        if (multipartFile == null){
            throw new IllegalArgumentException("전단지 이미지 파일이 존재하지 않습니다.");
        }

        Park findPark = parkRepository.findByName(flyerRequestDto.getName());
        Address address = new Address(flyerRequestDto.getSi(), flyerRequestDto.getGu(), flyerRequestDto.getDetail());
        FlyerPhoto flyerPhoto = new FlyerPhoto(s3Uploader.upload(multipartFile, "flyer"));

        Flyer flyer = new Flyer(flyerRequestDto.getName(), findPark, flyerPhoto, address, flyerRequestDto.getCall());
        findPark.addFlyer(flyer);

        return new FlyerResponseDto(flyer);
    }

    @Transactional
    public ResponseDto delete(Long id){
        Flyer findFlyer = flyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전단지가 존재하지 않습니다."));
        if(findFlyer.getPhoto() != null) s3Uploader.delete(findFlyer.getPhoto().getS3Key());
        flyerRepository.delete(findFlyer);

        return new ResponseDto("전단지가 삭제되었습니다.");
    }


    public List<FlyerResponseDto> findAllFlyers(){
        List<FlyerResponseDto> flyerResponseDtos = new ArrayList<>();

        List<Flyer> allFlyer = flyerRepository.findAllFlyer();
        for (Flyer flyer : allFlyer) {
            FlyerResponseDto flyerResponseDto = new FlyerResponseDto(flyer.getId(), flyer.getName(), flyer.getPark().getName(),
                   flyer.getPhoto().getUrl(), flyer.getAddress(), flyer.getCall());
            flyerResponseDtos.add(flyerResponseDto);
        }
        return flyerResponseDtos;
    }


    public List<FlyerResponseDto> findAllFlyersByPark(Long parkId){
        List<FlyerResponseDto> flyerResponseDtos = new ArrayList<>();

        Park findPark = parkRepository.findById(parkId);

        List<Flyer> allFlyerByPark = flyerRepository.findAllFlyerByPark(findPark);
        for (Flyer flyer : allFlyerByPark) {
            FlyerResponseDto flyerResponseDto = new FlyerResponseDto(flyer.getId(), flyer.getName(), flyer.getPark().getName(),
                    flyer.getPhoto().getUrl(), flyer.getAddress(), flyer.getCall());
            flyerResponseDtos.add(flyerResponseDto);
        }

        return flyerResponseDtos;
    }
}
