package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.Park;
import com.hangangnow.mainserver.domain.photo.ParkPhoto;
import com.hangangnow.mainserver.service.ParkService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Embedded;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parks")
public class ParkApiController {

    private final ParkService parkService;

    @GetMapping("")
    @Operation(summary = "모든 공원 조회", description = "공원 이름을 리스트 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!")
    })
    public Result parks(){
        List<Park> findParks = parkService.findAll();
        List<FindAllParkDto> collect = findParks.stream()
                .map(p -> new FindAllParkDto(p.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @GetMapping("/{parkid}")
    @Operation(summary = "단일 공원 조회", description = "공원id를 이용해 해당 공원의 정보를 상세 조회할 수 있습니다. (park id 1,2 테스트 가능)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!")
    })
    public FindOneParkDto parkOne(@PathVariable Long parkid){

        Park findPark = parkService.findOne(parkid);

        return new FindOneParkDto(
                findPark.getName(),
                findPark.getLocal(),
                findPark.getAddress(),
                findPark.getContent(),
                findPark.getPhotos()
        );
    }

    @PostMapping("")
    @Operation(summary = "단일 공원 추가", description = "공원정보를 추가할 수 있습니다.  "+"\nname 값은 필수로 포함되어야 하며, 공원 추가 성공시 공원 id가 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!")
    })
    public CreateParkResponse savePark(@RequestBody CreateParkRequest request) {
        Park park = new Park(
                request.getName(),
                new Local(request.getLocalname(), request.x_pos, request.y_pos),
                new Address(request.getSido(), request.getGu(), request.getGil(), request.getDetail()),
                request.getContent()
        );

        Long id = parkService.addPark(park);
        return new CreateParkResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class FindAllParkDto{
        private String name;
    }

    @Data
    static class CreateParkResponse {
        private Long id;
        public CreateParkResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @ApiModel(description="공원 이름, 공원 설명, 지역 이름, 지역 좌표 x, 지역 좌표 y, 시/도, 구, 길, 상세주소")
    static class CreateParkRequest {

        @NotEmpty
        @ApiModelProperty(value = "공원 이름")
        private String name;
        @ApiModelProperty(value = "공원 설명")
        private String content;

        @ApiModelProperty(value = "지역 이름")
        private String localname;
        @ApiModelProperty(value = "지역 좌표 x")
        private Double x_pos;
        @ApiModelProperty(value = "지역 좌표 y")
        private Double y_pos;

        @ApiModelProperty(value = "시/도")
        private String sido;
        @ApiModelProperty(value = "구")
        private String gu;
        @ApiModelProperty(value = "길")
        private String gil;
        @ApiModelProperty(value = "상세주소")
        private String detail;
    }

    @Data
    @AllArgsConstructor
    static class FindOneParkDto {
        private String name;

        @Embedded
        private Local local;
        @Embedded
        private Address address;

        private String content;
        private List<ParkPhoto> photos = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @ApiModel(description="온도, 풍속, 강수확률, 미세먼지")
    static class ParkWeatherResponse {
        @ApiModelProperty(value = "온도")
        private Double temperature;

        @ApiModelProperty(value = "풍속")
        private Double windPower;

        @ApiModelProperty(value = "강수확률")
        private Double rainPercent;

        @ApiModelProperty(value = "미세먼지")
        private Double Dust;
    }

    // valid 조건?
}
