package com.study.model.file;

import com.study.model.board.BoardIdx;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class file {
    private final fileIdx fileIdx;
    private final String saveFileName;
    private final originalName originalName;
    private final fileSize fileSize;
    private final BoardIdx boardIdx;

    public file(Builder builder) {
        this.fileIdx = builder.fileIdx;
        this.saveFileName = builder.saveFileName;
        this.originalName = builder.originalFileName;
        this.fileSize = builder.fileSize;
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {
        private fileIdx fileIdx;
        private String saveFileName;
        private originalName originalFileName;
        private fileSize fileSize;
        private BoardIdx boardIdx;

        public Builder(){};

        public Builder fileIdx(fileIdx imageIdx) {
            this.fileIdx = imageIdx;
            return this;
        }
        public Builder saveFileName(String saveFileName) {
            this.saveFileName = saveFileName;
            return this;
        }
        public Builder originalName(originalName originalName) {
            this.originalFileName = originalName;
            return this;
        }
        public Builder fileSize(fileSize imageSize) {
            this.fileSize = imageSize;
            return this;
        }
        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public file build() {
            if (!Stream.of(saveFileName, originalFileName, fileSize).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new file(this);
        }
    }
}
