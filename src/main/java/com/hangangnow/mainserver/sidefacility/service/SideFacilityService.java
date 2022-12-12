package com.hangangnow.mainserver.sidefacility.service;

import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.park.entity.Park;
import com.hangangnow.mainserver.sidefacility.entity.FacilityType;
import com.hangangnow.mainserver.sidefacility.entity.SideFacility;
import com.hangangnow.mainserver.sidefacility.dto.FacilityRequestDto;
import com.hangangnow.mainserver.sidefacility.dto.FacilityKakaoResponseDto;
import com.hangangnow.mainserver.sidefacility.dto.FacilityResponseDto;
import com.hangangnow.mainserver.park.repository.ParkRepository;
import com.hangangnow.mainserver.sidefacility.repository.SideFacilityRepository;

import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-secret.properties")
public class SideFacilityService {

    private final ParkRepository parkRepository;
    private final SideFacilityRepository sideFacilityRepository;

    @Value("${hangangnow.api.restapi.key}")
    private String key;

    @Transactional
    public GenericResponseDto registerFacility(FacilityRequestDto facilityRequestDto) {
        Park findPark = parkRepository.findByName(facilityRequestDto.getParkName())
                .orElseThrow(() -> new IllegalArgumentException("해당 한강공원이 존재하지 않습니다"));

        Address address = new Address(facilityRequestDto.getAddress());
        Local local = new Local(facilityRequestDto.getName(), facilityRequestDto.getX_pos(), facilityRequestDto.getY_pos());

        SideFacility sideFacility = new SideFacility(address, local, findPark, FacilityType.getFacilityType(facilityRequestDto.getType()));
        sideFacilityRepository.save(sideFacility);

        FacilityResponseDto facilityResponseDto = new FacilityResponseDto(sideFacility);
        return new GenericResponseDto(facilityResponseDto);
    }

    @Transactional
    public void save(SideFacility sideFacility) {
        sideFacilityRepository.save(sideFacility);
    }

    public FacilityResponseDto getSideFacilityByXposAndYpos(Double x, Double y) {
        SideFacility sideFacility = sideFacilityRepository.findByFacilityWithXY(x, y)
                .orElseThrow(() -> new IllegalArgumentException("해당 위도, 경도에 해당하는 주변시설이 없습니다."));

        return new FacilityResponseDto(sideFacility);
    }

    public GenericResponseDto getFacilities(String x, String y, String category) {

        // 편의점, 주차장, 식당, 카페
        List<String> categories = List.of("CS2", "PK6", "FD6", "CE7");
        if (!categories.contains(category)) {
            throw new IllegalArgumentException("존재하지 않는 카테고리 코드 입니다");
        }

        String data = "?x=" + x + "&y=" + y + "&category_group_code=" + category + "&radius=1000" + "&sort=distance";

        List<FacilityKakaoResponseDto> facilityKakaoResponseDtos = callKakaoLocalAPI(data);

        return new GenericResponseDto(facilityKakaoResponseDtos);
    }


    public List<FacilityKakaoResponseDto> callKakaoLocalAPI(String data) {
        StringBuffer response = new StringBuffer();

        try {
            String apiURL = "https://dapi.kakao.com/v2/local/search/category.json" + data;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + key);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();


        } catch (Exception e) {
            System.out.println(e);
        }

        List<FacilityKakaoResponseDto> facilityKakaoResponseDtos = getFacilityDto(response.toString());

        return facilityKakaoResponseDtos;
    }

    private List<FacilityKakaoResponseDto> getFacilityDto(String jsonString) {
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        Object documents = jObj.get("documents");

        JSONArray jsonArr = (JSONArray) documents;

        List<FacilityKakaoResponseDto> facilityKakaoResponseDtos = new ArrayList<>();

        if (jsonArr.size() > 0) {
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArr.get(i);

                FacilityKakaoResponseDto facilityKakaoResponseDto = new FacilityKakaoResponseDto(String.valueOf(jsonObj.get("place_name")), String.valueOf(jsonObj.get("address_name")), String.valueOf(jsonObj.get("phone")),
                        String.valueOf(jsonObj.get("place_url")), Double.valueOf(String.valueOf(jsonObj.get("distance"))), Double.valueOf(String.valueOf(jsonObj.get("x"))), Double.valueOf(String.valueOf(jsonObj.get("y"))));

                facilityKakaoResponseDtos.add(facilityKakaoResponseDto);
            }
        }

        return facilityKakaoResponseDtos;
    }


    public GenericResponseDto getFacilitiesByParkIdAndType(Long parkId, String type) {
        List<String> categories = List.of("TOILET", "SUN_SHADOW", "BICYCLE", "STORE", "VIEW", "DELIVERY_ZONE", "STORE",
                "CAFE", "RESTAURANT", "PARKING", "LOAD_FOOD", "BASKETBALL", "BASEBALL", "SOCCER", "TENNIS", "SWIM");

        if (!categories.contains(type)) {
            throw new IllegalArgumentException("존재하지 않는 카테고리 코드 입니다");
        }

        FacilityType facilityType = FacilityType.getFacilityType(type);

        parkRepository.findById(parkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 공원이 존재하지 않습니다."));

        List<FacilityResponseDto> results = sideFacilityRepository.findByParkAndType(parkId, facilityType)
                .stream()
                .map(FacilityResponseDto::new)
                .collect(Collectors.toList());

        return new GenericResponseDto(results);

    }
}
