package com.study.model.board;

import java.util.Objects;

public class Content {

    private String content;

    public Content(String content) {
        if (content.length() < 4 || content.length() > 1999) {
            throw new IllegalArgumentException("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
        }
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Content content1 = (Content) o;
        return Objects.equals(content, content1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
