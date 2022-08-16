package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import com.hangangnow.mainserver.domain.sidefacility.FacilityResponseDto;
import com.hangangnow.mainserver.repository.SideFacilityRepository;

import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-secret.properties")
public class SideFacilityService {

    @Value("${hangangnow.api.restapi.key}")
    private String key;

    private final SideFacilityRepository sideFacilityRepository;

    public GenericResponseDto getFacility(String x, String y, String category) {
        // 편의점, 주차장, 식당, 카페
        String[] categories = new String[]{"CS2", "PK6", "FD6", "CE7"};
        if (!Arrays.asList(categories).contains(category)){
            throw new IllegalArgumentException("존재하지 않는 카테고리 코드 입니다");
        }

        String data = "?x=" + x + "&y=" + y + "&category_group_code=" + category + "&radius=1000" + "&sort=distance";

        List<FacilityResponseDto> facilityResponseDtos = callKakaoLocalAPI(data);

        return new GenericResponseDto(facilityResponseDtos);
    }


    public List<FacilityResponseDto> callKakaoLocalAPI(String data) {
        StringBuffer response = new StringBuffer();

        try {
            String apiURL = "https://dapi.kakao.com/v2/local/search/category.json" + data;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + key);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
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

        List<FacilityResponseDto> facilityResponseDtos = getFacilityDto(response.toString());

        return facilityResponseDtos;
    }

    private List<FacilityResponseDto> getFacilityDto(String jsonString) {
        String value = "";
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        Object documents = jObj.get("documents");

        JSONArray jsonArr = (JSONArray) documents;

        List<FacilityResponseDto> facilityResponseDtos = new ArrayList<>();

        if (jsonArr.size() > 0) {
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArr.get(i);

                FacilityResponseDto facilityResponseDto = new FacilityResponseDto(String.valueOf(jsonObj.get("place_name")), String.valueOf(jsonObj.get("address_name")) , String.valueOf(jsonObj.get("phone")),
                        String.valueOf(jsonObj.get("place_url")), Double.parseDouble(String.valueOf(jsonObj.get("distance"))), Double.parseDouble(String.valueOf(jsonObj.get("x"))), Double.parseDouble(String.valueOf(jsonObj.get("y"))));

                facilityResponseDtos.add(facilityResponseDto);
            }
        }

        return facilityResponseDtos;
    }
}
