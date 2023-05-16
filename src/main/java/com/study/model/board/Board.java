package com.study.model.board;

import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Board {
    private final BoardIdx boardIdx;
    private final Category category;
    private final Title title;
    private final Writer writer;
    private final Content content;
    private final Password password;
    private final Hit hit;
    private final RegDate regDate;

    public Board(Builder builder) {
        this.boardIdx = builder.boardIdx;
        this.category = builder.category;
        this.title = builder.title;
        this.writer = builder.writer;
        this.content = builder.content;
        this.password = builder.password;
        this.hit = builder.hit;
        this.regDate = builder.regDate;
    }

    public static class Builder {

        private BoardIdx boardIdx;
        private Category category;
        private Title title;
        private Writer writer;
        private Content content;
        private Password password;
        private Hit hit;
        private RegDate regDate;

        public Builder() {
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder title(Title title) {
            this.title = title;
            return this;
        }

        public Builder writer(Writer writer) {
            this.writer = writer;
            return this;
        }

        public Builder content(Content content) {
            this.content = content;
            return this;
        }

        public Builder password(Password password) {
            this.password = password;
            return this;
        }

        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public Builder hit(Hit hit) {
            this.hit = hit;
            return this;
        }

        public Builder regDate(RegDate regDate) {
            this.regDate = regDate;
            return this;
        }

        public Board build() {
            if (!Stream.of(category, title, writer, content, password).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new Board(this);
        }
    }
}
