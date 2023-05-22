package com.study.model.board;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Board {
    private final BoardIdx boardIdx;
    private final Category category;
    private final Title title;
    private final BoardWriter writer;
    private final BoardContent content;
    private final Password password;
    private final Hit hit;
    private final RegDate regDate;
    private final ModDate modDate;

    public Board(BoardIdx boardIdx, Category category, Title title, BoardWriter writer,
                 BoardContent content, Password password, Hit hit, RegDate regDate, ModDate modDate) {
        this.boardIdx = boardIdx;
        this.category = category;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.password = password;
        this.hit = hit;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    public Board(Category category, Title title, BoardWriter writer,
                 BoardContent content, Password password) {
        this(new BoardIdx(0), category, title, writer, content, password, new Hit(0), new RegDate(LocalDateTime.now()), null);
    }

    public Board(Builder builder) {
        this.boardIdx = builder.boardIdx;
        this.category = builder.category;
        this.title = builder.title;
        this.writer = builder.writer;
        this.content = builder.content;
        this.password = builder.password;
        this.hit = new Hit(0);
        this.regDate = new RegDate(LocalDateTime.now());
        this.modDate = null;
    }

    public static class Builder {

        private BoardIdx boardIdx;
        private Category category;
        private Title title;
        private BoardWriter writer;
        private BoardContent content;
        private Password password;
        private Hit hit;
        private RegDate regDate;
        private ModDate modDate;

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

        public Builder writer(BoardWriter writer) {
            this.writer = writer;
            return this;
        }

        public Builder content(BoardContent content) {
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

        public Builder modDate(ModDate modDate) {
            this.modDate = modDate;
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
