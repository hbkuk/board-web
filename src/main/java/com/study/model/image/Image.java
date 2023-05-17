package com.study.model.image;

import java.util.Objects;
import java.util.stream.Stream;

public class Image {
    private final ImageIdx imageIdx;
    private final ImageName imageName;
    private final ImageSize imageSize;

    public Image(Builder builder) {
        this.imageIdx = builder.imageIdx;
        this.imageName = builder.imageName;
        this.imageSize = builder.imageSize;
    }

    public static class Builder {
        private ImageIdx imageIdx;
        private ImageName imageName;
        private ImageSize imageSize;

        public Builder(){};

        public Builder imageIdx(ImageIdx imageIdx) {
            this.imageIdx = imageIdx;
            return this;
        }
        public Builder imageName(ImageName imageName) {
            this.imageName = imageName;
            return this;
        }
        public Builder imageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public Image build() {
            if (!Stream.of(imageName, imageSize).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new Image(this);
        }
    }
}
