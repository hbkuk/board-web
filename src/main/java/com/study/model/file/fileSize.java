package com.study.model.file;

import java.util.Objects;

public class fileSize {

    private static final int MAX_IMAGE_SIZE_VALUE = 10_485_760;
    private int imageSize;

    public fileSize(int imageSize) {
        if (imageSize >= MAX_IMAGE_SIZE_VALUE) {
            throw new IllegalArgumentException("이미지의 크기가 10_485_760 byte 이상일 수 없습니다.");
        }
        this.imageSize = imageSize;
    }

    public int getImageSize() {
        return imageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        fileSize imageSize1 = (fileSize) o;
        return imageSize == imageSize1.imageSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageSize);
    }
}
