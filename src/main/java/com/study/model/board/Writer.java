package com.study.model.board;

import java.util.Objects;

public class Writer {
    private static final int MIN_WRITER_LENGTH_VALUE = 3;
    private static final int MAX_WRITER_LENGTH_VALUE = 4;
    private String writer;

    public Writer(String writer) {
        if (writer.length() < MIN_WRITER_LENGTH_VALUE || writer.length() > MAX_WRITER_LENGTH_VALUE) {
            throw new IllegalArgumentException("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");
        }
        this.writer = writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Writer writer1 = (Writer) o;
        return Objects.equals(writer, writer1.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writer);
    }
}
