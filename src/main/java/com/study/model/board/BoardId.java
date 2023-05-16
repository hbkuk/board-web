package com.study.model.board;

import java.util.Objects;

public class BoardId {
    private static final int MIN_BOARDID_VALUE = 0;
    private long boardId = 0;

    public BoardId(long value) {
        if(value < MIN_BOARDID_VALUE) {
            throw new IllegalArgumentException("글 번호는 음수일 수 없습니다.");
        }
        this.boardId = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BoardId boardId1 = (BoardId) o;
        return boardId == boardId1.boardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId);
    }
}
