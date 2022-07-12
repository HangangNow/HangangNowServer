package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.Park;
import com.hangangnow.mainserver.domain.Weather;
import com.hangangnow.mainserver.domain.photo.ParkPhoto;
import com.hangangnow.mainserver.service.ParkService;
import com.hangangnow.mainserver.service.WeatherService;
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
public class ParkApiController {

    private final ParkService parkService;
    private final WeatherService weatherService;

    @GetMapping("/api/parks")
    public Result parks(){
        List<Park> findParks = parkService.findAll();
        List<FindAllParkDto> collect = findParks.stream()
                .map(p -> new FindAllParkDto(p.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @GetMapping("/api/parks/{parkid}")
    public FindOneParkDto parkOne(@PathVariable Long parkid){

        Park findPark = parkService.findOne(parkid);

        return new FindOneParkDto(
                findPark.getName(),
                findPark.getLocal(),
                findPark.getAddress(),
                findPark.getDescribe(),
                findPark.getPhotos()
        );
    }

    @GetMapping("api/parks/{parkid}/weather")
    public ParkWeatherResponse parkWeather(@PathVariable Long parkid) {

        Weather findParkWeather = weatherService.findOne(parkid);

        return new ParkWeatherResponse(
                findParkWeather.getTemperature(),
                findParkWeather.getWindPower(),
                findParkWeather.getRainPercent(),
                findParkWeather.getDust()
        );
    }

    @PostMapping("api/parks")
    public CreateParkResponse savePark(@RequestBody CreateParkRequest request) {
        Park park = new Park(
                request.getName(),
                new Local(request.getLocalname(), request.x_pos, request.y_pos),
                new Address(request.getSido(), request.getGu(), request.getGil(), request.getDetail()),
                request.getDescribe()
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
    static class CreateParkRequest {
        @NotEmpty
        private String name;

        private String localname;
        private Double x_pos;
        private Double y_pos;

        private String sido;
        private String gu;
        private String gil;
        private String detail;

        private String describe;
    }

    @Data
    @AllArgsConstructor
    static class FindOneParkDto {
        private String name;

        @Embedded
        private Local local;
        @Embedded
        private Address address;

        private String describe;
        private List<ParkPhoto> photos = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    static class ParkWeatherResponse {
        private Double temperature;
        private Double windPower;
        private Double rainPercent;
        private Double Dust;
    }

    // valid 조건?
}
