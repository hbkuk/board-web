package com.study.model.board;

import lombok.Getter;

import java.util.Objects;

@Getter
public class BoardIdx {
    private static final int MIN_BOARDID_VALUE = 0;
    private long boardId = 0;

    public BoardIdx(long value) {
        if(value < MIN_BOARDID_VALUE) {
            throw new IllegalArgumentException("글 번호는 음수일 수 없습니다.");
        }
        this.boardId = value;
    }

    public long getBoardIdx() {
        return boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BoardIdx boardId1 = (BoardIdx) o;
        return boardId == boardId1.boardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId);
    }
}
