package com.study.model.file;

import com.study.model.board.BoardIdx;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class File {
    private final FileIdx fileIdx;
    private final String saveFileName;
    private final FileOriginalName originalName;
    private final FileSize fileSize;
    private final BoardIdx boardIdx;

    public File(FileIdx fileIdx, String saveFileName, FileOriginalName originalName,
                FileSize fileSize, BoardIdx boardIdx) {
        this.fileIdx = fileIdx;
        this.saveFileName = saveFileName;
        this.originalName = originalName;
        this.fileSize = fileSize;
        this.boardIdx = boardIdx;
    }

    public File(String saveFileName, FileOriginalName originalName) {
        this(new FileIdx(0), saveFileName, originalName, new FileSize(0), new BoardIdx(0));
    }

    public File(Builder builder) {
        this.fileIdx = builder.FileIdx;
        this.saveFileName = builder.saveFileName;
        this.originalName = builder.originalFileName;
        this.fileSize = new FileSize(0);
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {
        private FileIdx FileIdx;
        private String saveFileName;
        private FileOriginalName originalFileName;
        private FileSize fileSize;
        private BoardIdx boardIdx;

        public Builder(){};

        public Builder fileIdx(FileIdx imageIdx) {
            this.FileIdx = imageIdx;
            return this;
        }
        public Builder saveFileName(String saveFileName) {
            this.saveFileName = saveFileName;
            return this;
        }
        public Builder originalName(FileOriginalName originalName) {
            this.originalFileName = originalName;
            return this;
        }
        public Builder fileSize(FileSize imageSize) {
            this.fileSize = imageSize;
            return this;
        }
        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public File build() {
            if (!Stream.of(saveFileName, originalFileName).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new File(this);
        }
    }
}
