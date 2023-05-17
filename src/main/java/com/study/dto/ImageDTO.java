package com.study.dto;

import com.study.model.board.BoardIdx;
import com.study.model.image.Image;
import com.study.model.image.ImageIdx;
import com.study.model.image.ImageName;
import com.study.model.image.ImageSize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageDTO {
    private ImageIdx imageIdx;
    private ImageName imageName;
    private ImageSize imageSize;
    private BoardIdx BoardIdx;

    public ImageDTO() {
    }

    public ImageDTO(Image image) {
        this.imageIdx = image.getImageIdx();
        this.imageName = image.getImageName();
        this.imageSize = image.getImageSize();
    }
}
