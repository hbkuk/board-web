package com.study.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
public class Board {
    private final long boardId;
    private final Category category;
    private final String title;
    private final String writer;
    private final String content;
    private final String password;
    private final int hit;
    private final LocalDateTime regDate;

    private Board(Builder builder) {
        this.boardId = builder.boardId;
        this.category = builder.category;
        this.title = builder.title;
        this.writer = builder.writer;
        this.content = builder.content;
        this.password = builder.password;
        this.hit = builder.hit;
        this.regDate = builder.regDate;
    }

    public static class Builder {

        // 기본값
        private long boardId = 0;
        private Category category;
        private String title;
        private String writer;
        private String content;
        private String password;
        private int hit = 0;
        private LocalDateTime regDate = LocalDateTime.now();

        public Builder() {
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder boardId(long boardId) {
            this.boardId = boardId;
            return this;
        }

        public Builder hit(int hit) {
            this.hit = hit;
            return this;
        }

        public Builder regDate(LocalDateTime regDate) {
            this.regDate = regDate;
            return this;
        }

        public Board build() {
            if (Category.isInvalidCategory(this.category)) {
                throw new IllegalArgumentException("카테고리는 선택되어야 합니다.");
            }
            if (this.title.length() < 4 || this.title.length() > 99) {
                throw new IllegalArgumentException("제목은 4글자 미만, 100글자 이상을 입력할 수 없습니다.");
            }
            if (this.writer.length() < 3 || this.writer.length() > 4 ) {
                throw new IllegalArgumentException("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");
            }
            if (this.password.length() < 4 || this.password.length() > 15) {
                throw new IllegalArgumentException("패스워드는 4글자 미만 16글자 이상일 수 없습니다.");
            }
            String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            if (!pattern.matcher(password).matches()) {
                throw new IllegalArgumentException("패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.");
            }
            if (this.content.length() < 4 || this.content.length() > 1999) {
                throw new IllegalArgumentException("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
            }
            return new Board(this);
        }
    }
}
