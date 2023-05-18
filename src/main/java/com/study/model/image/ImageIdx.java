package com.study.model.image;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ImageIdx {
    private static final int MIN_IMAGEIDX_VALUE = 0;
    private long imageIdx = 0;

    public ImageIdx(long value) {
        if(value < MIN_IMAGEIDX_VALUE) {
            throw new IllegalArgumentException("이미지 번호는 음수일 수 없습니다.");
        }
        this.imageIdx = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImageIdx imageIdx1 = (ImageIdx) o;
        return imageIdx == imageIdx1.imageIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageIdx);
    }
}
