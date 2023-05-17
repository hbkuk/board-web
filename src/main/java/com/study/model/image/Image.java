package com.study.model.image;

import com.study.model.board.BoardIdx;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Image {
    private final ImageIdx imageIdx;
    private final ImageName imageName;
    private final ImageSize imageSize;
    private final BoardIdx boardIdx;

    public Image(Builder builder) {
        this.imageIdx = builder.imageIdx;
        this.imageName = builder.imageName;
        this.imageSize = builder.imageSize;
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {
        private ImageIdx imageIdx;
        private ImageName imageName;
        private ImageSize imageSize;
        private BoardIdx boardIdx;

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
        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public Image build() {
            if (!Stream.of(imageName, imageSize, boardIdx).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new Image(this);
        }
    }
}
