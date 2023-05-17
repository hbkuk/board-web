package com.study.dto;

import com.study.model.image.Image;
import com.study.model.image.ImageIdx;
import com.study.model.image.ImageName;
import com.study.model.image.ImageSize;

public class ImageDTO {
    private ImageIdx imageIdx;
    private ImageName imageName;
    private ImageSize imageSize;

    public ImageDTO() {
    }

    public ImageDTO(Image image) {
        this.imageIdx = image.getImageIdx();
        this.imageName = image.getImageName();
        this.imageSize = image.getImageSize();
    }
}
