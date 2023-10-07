package com.ld.poetry.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileVO {

    private String type;

    private String storeType;

    private String relativePath;

    private String absolutePath;

    private String visitPath;

    private MultipartFile file;
}
