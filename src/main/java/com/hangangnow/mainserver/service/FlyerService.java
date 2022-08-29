package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.flyer.Flyer;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerRequestDto;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.scrap.FlyerScrap;
import com.hangangnow.mainserver.domain.park.Park;
import com.hangangnow.mainserver.domain.photo.FlyerPhoto;
import com.hangangnow.mainserver.repository.FlyerRepository;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.ParkRepository;
import com.hangangnow.mainserver.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlyerService {

    private final FlyerRepository flyerRepository;
    private final ParkRepository parkRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public FlyerResponseDto save(MultipartFile multipartFile, @Valid FlyerRequestDto flyerRequestDto) throws IOException {
        if (multipartFile == null){
            throw new IllegalArgumentException("전단지 이미지 파일이 존재하지 않습니다.");
        }

        Park findPark = parkRepository.findByName(flyerRequestDto.getParkName())
                .orElseThrow(() -> new IllegalArgumentException("해당 공원이 존재하지 않습니다."));
        Address address = new Address(flyerRequestDto.getAddress());
        FlyerPhoto flyerPhoto = new FlyerPhoto(s3Uploader.upload(multipartFile, "flyer"));

        Flyer flyer = new Flyer(flyerRequestDto.getName(), flyerPhoto, address, flyerRequestDto.getContent(), flyerRequestDto.getCall());
        flyerRepository.save(flyer);
        findPark.addFlyer(flyer);

        return new FlyerResponseDto(flyer);
    }


    @Transactional
    public FlyerResponseDto update(Long flyerId, MultipartFile multipartFile, @Valid FlyerRequestDto flyerRequestDto) throws IOException {
        Flyer findFlyer = flyerRepository.findById(flyerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 전단지가 존재하지 않습니다."));

        Park findPark = parkRepository.findByName(flyerRequestDto.getParkName())
                .orElseThrow(() -> new IllegalArgumentException("해당 공원이 존재하지 않습니다."));

        if(multipartFile != null){
            s3Uploader.delete(findFlyer.getPhoto().getS3Key());
            FlyerPhoto flyerPhoto = new FlyerPhoto(s3Uploader.upload(multipartFile, "flyer"));
            findFlyer.updatePhoto(flyerPhoto);
        }

        Address address = new Address(flyerRequestDto.getAddress());
        findFlyer.update(flyerRequestDto.getName(), address, flyerRequestDto.getContent(),flyerRequestDto.getCall());
        findPark.addFlyer(findFlyer);

        return new FlyerResponseDto(findFlyer);
    }

    @Transactional
    public ResponseDto delete(Long id){
        Flyer findFlyer = flyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전단지가 존재하지 않습니다."));
        if(findFlyer.getPhoto() != null) s3Uploader.delete(findFlyer.getPhoto().getS3Key());
        flyerRepository.delete(findFlyer);

        return new ResponseDto("전단지가 삭제되었습니다.");
    }


    public FlyerResponseDto findOneFlyers(Long id){
        Flyer findFlyer = flyerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 전단지가 존재하지 않습니다."));

        return new FlyerResponseDto(findFlyer);
    }


    public GenericResponseDto findAllFlyers(){
        List<Flyer> allFlyer = flyerRepository.findAllFlyer();

        List<FlyerResponseDto> results = allFlyer.stream()
                .map(f -> new FlyerResponseDto(f))
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }


    public GenericResponseDto findAllFlyersByPark(Long parkId){
        Park findPark = parkRepository.findById(parkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공원이 존재하지 않습니다."));

        List<Flyer> allFlyerByPark = flyerRepository.findAllFlyerByPark(findPark);

        List<FlyerResponseDto> results = allFlyerByPark.stream()
                .map(f -> new FlyerResponseDto(f))
                .collect(Collectors.toList());

        return new GenericResponseDto(results);
    }

}
