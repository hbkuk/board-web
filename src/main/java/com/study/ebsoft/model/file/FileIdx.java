package com.study.ebsoft.model.file;

import lombok.Getter;

import java.util.Objects;

@Getter
public class FileIdx {
    private static final int MIN_IMAGEIDX_VALUE = 0;
    private long imageIdx = 0;

    public FileIdx(long value) {
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
        FileIdx imageIdx1 = (FileIdx) o;
        return imageIdx == imageIdx1.imageIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageIdx);
    }
}
