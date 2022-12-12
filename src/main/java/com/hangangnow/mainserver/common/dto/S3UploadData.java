package com.hangangnow.mainserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class S3UploadData {

    @NotEmpty
    private String fileKey;

    @NotEmpty
    private String fileUrl;
}
