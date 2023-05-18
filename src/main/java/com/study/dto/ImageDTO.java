package com.study.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageDTO {
    private long imageIdx;
    private String imageName;
    private int imageSize;
    private long BoardIdx;
}
