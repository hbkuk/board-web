package com.study.model.file;

import com.study.model.board.BoardIdx;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class file {
    private final fileIdx imageIdx;
    private final fileName imageName;
    private final fileSize imageSize;
    private final BoardIdx boardIdx;

    public file(Builder builder) {
        this.imageIdx = builder.imageIdx;
        this.imageName = builder.imageName;
        this.imageSize = builder.imageSize;
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {
        private fileIdx imageIdx;
        private fileName imageName;
        private fileSize imageSize;
        private BoardIdx boardIdx;

        public Builder(){};

        public Builder imageIdx(fileIdx imageIdx) {
            this.imageIdx = imageIdx;
            return this;
        }
        public Builder imageName(fileName imageName) {
            this.imageName = imageName;
            return this;
        }
        public Builder imageSize(fileSize imageSize) {
            this.imageSize = imageSize;
            return this;
        }
        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public file build() {
            if (!Stream.of(imageName, imageSize, boardIdx).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new file(this);
        }
    }
}
