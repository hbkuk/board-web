package com.study.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileDTO {
    private long fileIdx;
    private String fileName;
    private int fileSize;
    private long BoardIdx;
}
