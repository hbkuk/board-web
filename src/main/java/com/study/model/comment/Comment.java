package com.study.model.comment;

import com.study.model.board.BoardIdx;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Comment {
    private final CommentIdx commentIdx;
    private final CommentWriter writer;
    private final Password password;
    private final String content;
    private final RegDate regDate;
    private final BoardIdx boardIdx;

    public Comment(Builder builder) {
        this.commentIdx = builder.commentIdx;
        this.writer = builder.writer;
        this.password = builder.password;
        this.content = builder.content;
        this.regDate = builder.regDate;
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {

        private CommentIdx commentIdx;
        private CommentWriter writer;
        private Password password;
        private String content;
        private RegDate regDate;
        private BoardIdx boardIdx;

        public Builder() {
        }

        public Builder commentIdx(CommentIdx commentIdx) {
            this.commentIdx = commentIdx;
            return this;
        }
        public Builder writer(CommentWriter Writer) {
            this.writer = Writer;
            return this;
        }
        public Builder password(Password Password) {
            this.password = Password;
            return this;
        }
        public Builder content(String Content) {
            this.content = Content;
            return this;
        }
        public Builder regDate(RegDate RegDate) {
            this.regDate = RegDate;
            return this;
        }
        public Builder boardIdx(BoardIdx BoardIdx) {
            this.boardIdx = BoardIdx;
            return this;
        }

        public Comment build() {
            if (!Stream.of(writer, password, content, boardIdx).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            if (content.length() < 4 || content.length() > 999) {
                throw new IllegalArgumentException("내용은 4글자 미만 1000글자를 초과할 수 없습니다.");
            }
            return new Comment(this);
        }
    }

}