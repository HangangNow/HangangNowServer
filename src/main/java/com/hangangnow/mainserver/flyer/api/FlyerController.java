package com.hangangnow.mainserver.flyer.api;

import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.flyer.dto.FlyerRequestDto;
import com.hangangnow.mainserver.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.flyer.service.FlyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class FlyerController {

    private final ConversionService conversionService;
    private final FlyerService flyerService;


    @Operation(summary = "전단지 추가", description = "전단지를 추가할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청, 추가된 전단지 응답, swagger에서 테스트 시 content-type error가 발생할 수 있습니다.  " +
            "\n사용자를 위한 컨트롤러가 아닌 **ADMIN 계정을 위한 컨트롤러입니다.** 현재는 테스트를 위해 제약을 걸지 않았습니다.  " +
            "\nkey: multipartData(필수 이미지), jsonData(필수 json text)  " +
            "\njsonData name, parkName, address, call 필수 값. **parkName은 xx한강공원(ex, 여의도한강공원) 형식입니다.**  " +
            "\n주의 - jsonData content-type application/json으로 설정  \n  " +
            "\njsonData example: " +
            "{\n" +
            "\t\"name\" : \"비비큐 여의도점\",\n" +
            "\t\"parkName\" : \"여의도 한강공원\",\n" +
            "\t\"address\": \"서울시 영등포구 국제금융로7길 32\",\n" +
            "\t\"call\" : \"0504-3142-1881\"\n" +
            "}\n"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PostMapping("/api/v1/flyers")
    public ResponseEntity<FlyerResponseDto> registerFlyer(
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest,
            @NotBlank @RequestPart(value = "jsonData") String jsonStringRequest) throws Exception {
        FlyerRequestDto flyerRequestDto = conversionService.convert(jsonStringRequest, FlyerRequestDto.class);
        return new ResponseEntity<>(flyerService.save(imageRequest, flyerRequestDto), HttpStatus.CREATED);
    }


    @Operation(summary = "모든 전단지 조회", description = "모든 전단지를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/flyers")
    public ResponseEntity<GenericResponseDto> getAllFlyers() {
        return new ResponseEntity<>(flyerService.findAllFlyers(), HttpStatus.OK);
    }


    @Operation(summary = "단일 전단지 조회", description = "단일 전단지를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/flyers/{flyerId}")
    public ResponseEntity<FlyerResponseDto> getOneFlyer(@PathVariable Long flyerId) {
        return new ResponseEntity<>(flyerService.findOneFlyers(flyerId), HttpStatus.OK);
    }


    @Operation(summary = "공원 별 전단지 조회", description = "공원 ID를 이용해 공원 별 전단지를 조회할 수 있습니다.  " +
            "\n 1: 광나루한강공원  " +
            "\n 2: 잠실한강공원  " +
            "\n 3: 뚝섬한강공원  " +
            "\n 4: 잠원한강공원  " +
            "\n 5: 반포한강공원  " +
            "\n 6: 이촌한강공원  " +
            "\n 7: 망원한강공원  " +
            "\n 8: 여의도한강공원  " +
            "\n 9: 난지한강공원  " +
            "\n 10: 강서한강공원  " +
            "\n 11: 양화한강공원  ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/flyers/parks/{parkId}")
    public ResponseEntity<GenericResponseDto> getFlyerByParkId(@PathVariable Long parkId) {
        return new ResponseEntity<>(flyerService.findAllFlyersByPark(parkId), HttpStatus.OK);
    }


    @Operation(summary = "전단지 수정", description = "해당 전단지를 수정할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청, 수정된 전단지 응답, swagger에서 테스트 시 content-type error가 발생할 수 있습니다.  " +
            "\n사용자를 위한 컨트롤러가 아닌 **ADMIN 계정을 위한 컨트롤러입니다.** 현재는 테스트를 위해 제약을 걸지 않았습니다.  " +
            "\nkey: multipartData(전단지 이미지를 수정하고 싶은 경우에만 첨부), jsonData(필수 json text)  " +
            "\njsonData name, parkName, address, call 필수 값. **parkName은 xx한강공원(ex, 여의도한강공원) 형식입니다.**  " +
            "\n주의 - jsonData content-type application/json으로 설정  \n  " +
            "\njsonData example: " +
            "{\n" +
            "\t\"name\" : \"비비큐 여의도점 1\",\n" +
            "\t\"parkName\" : \"여의도 한강공원\",\n" +
            "\t\"address\": \"서울시 영등포구 국제금융로7길 32\",\n" +
            "\t\"call\" : \"0504-3142-1881\"\n" +
            "}\n"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PutMapping("/api/v1/flyers/{flyerId}")
    public ResponseEntity<FlyerResponseDto> modifyFlyer(@PathVariable Long flyerId,
                                                        @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest,
                                                        @NotBlank @RequestPart(value = "jsonData") String jsonStringRequest) throws IOException {
        FlyerRequestDto flyerRequestDto = conversionService.convert(jsonStringRequest, FlyerRequestDto.class);
        return new ResponseEntity<>(flyerService.update(flyerId, imageRequest, flyerRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "단일 전단지 삭제", description = "단일 전단지를 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @DeleteMapping("/api/v1/flyers/{flyerId}")
    public ResponseEntity<ResponseDto> deleteFlyer(@PathVariable Long flyerId) {
        return new ResponseEntity<>(flyerService.delete(flyerId), HttpStatus.OK);
    }

}
