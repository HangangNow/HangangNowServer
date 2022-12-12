package com.hangangnow.mainserver.event.api;

import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.event.dto.EventRequestDto;
import com.hangangnow.mainserver.event.dto.EventResponseDto;
import com.hangangnow.mainserver.event.service.EventService;
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
public class EventController {

    private final ConversionService conversionService;
    private final EventService eventService;

    @Operation(summary = "이벤트 추가", description = "이벤트를 추가할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청, 추가된 event 응답, swagger에서 테스트 시 content-type error가 발생할 수 있습니다.  " +
            "\n사용자를 위한 컨트롤러가 아닌 **ADMIN 계정을 위한 컨트롤러입니다.** 현재는 테스트를 위해 제약을 걸지 않았습니다.  " +
            "\nkey: multipartData(필수 이미지), thumbnail(필수 이미지), jsonData(필수 json text)  " +
            "\njsonData의 address, startDate, endDate, content는 필수 값. **startDate, endDate는 yyyy-mm-dd 형식입니다.**  " +
            "\n주의 - jsonData content-type application/json으로 설정  \n  " +
            "\njsonData example: " +
            "{\n" +
            "\t\"address\": \"서울시 영등포구 국제금융로7길 32\",\n" +
            "\t\"x_pos\" : \"135.1244214\",\n" +
            "\t\"y_pos\" : \"37.1231321\",\n" +
            "\t\"title\" : \"여의도 한강공원 축제\",\n" +
            "\t\"startDate\" : \"2022-06-01\",\n" +
            "\t\"endDate\": \"2022-07-01\",\n" +
            "\t\"eventTime\" : \"상시\",\n" +
            "\t\"price\" : \"무료\",\n" +
            "\t\"content\" : \"여의도 한강공원에서 벚꽃축제를 합니다\",\n" +
            "\t\"host\" : \"한강사업본부\",\n" +
            "\t\"management\" : \"여의도 한강공원\"\n" +
            "}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PostMapping("/api/v1/events")
    public ResponseEntity<EventResponseDto> registerEvent(
            @NotBlank @RequestPart(value = "jsonData") String jsonStringRequest,
            @Valid @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws IOException {
        EventRequestDto eventRequestDto = conversionService.convert(jsonStringRequest, EventRequestDto.class);
        return new ResponseEntity<>(eventService.save(eventRequestDto, thumbnail, imageRequest), HttpStatus.CREATED);
    }


    @Operation(summary = "모든 이벤트 조회", description = "모든 이벤트를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/events")
    public ResponseEntity<GenericResponseDto> getAllEvents(){
        return new ResponseEntity<>(eventService.findAllEvents(), HttpStatus.OK);
    }


    @Operation(summary = "단일 이벤트 조회", description = "단일 이벤트를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @GetMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long eventId){
        return new ResponseEntity<>(eventService.findOne(eventId), HttpStatus.OK);
    }


    @Operation(summary = "단일 이벤트 수정", description = "단일 이벤트를 수정할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청, 수정된 event 응답  " +
            "\n이벤트를 등록(POST)하는 방식과 요청방식이 같습니다. 기존데이터에서 수정된 부분을 반영하여 함께 요청을 보내야합니다.  " +
            "\n썸네일 이미지와, 이벤트 이미지는 필수 값이 아닙니다. 수정이 필요하지 않은 경우 요청에 담지 않으면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @PutMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventResponseDto> modifyEventById(@PathVariable Long eventId,
            @NotBlank @RequestPart(value = "jsonData") String jsonStringRequest,
            @Valid @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws IOException {
        EventRequestDto eventRequestDto = conversionService.convert(jsonStringRequest, EventRequestDto.class);
        return new ResponseEntity<>(eventService.update(eventId, eventRequestDto, thumbnail, imageRequest), HttpStatus.OK);
    }


    @Operation(summary = "단일 이벤트 삭제", description = "단일 이벤트를 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed")
    })
    @DeleteMapping("/api/v1/events/{eventId}")
    public ResponseEntity<ResponseDto> deleteEvent(@PathVariable Long eventId){
        return new ResponseEntity<>(eventService.delete(eventId), HttpStatus.OK);
    }

}
