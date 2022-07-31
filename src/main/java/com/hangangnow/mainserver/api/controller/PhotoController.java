package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.common.GenericResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/photo")
public class PhotoController {

    private final S3Uploader s3Uploader;

    @PostMapping("/diary")
    public GenericResponseDto DiaryPhotoUpload(@RequestParam("data")MultipartFile multipartFile) throws IOException{
        return new GenericResponseDto(s3Uploader.upload(multipartFile, "diary"));
    }
}
