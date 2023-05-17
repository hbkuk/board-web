package com.study.dto;

import com.study.model.board.Board;
import com.study.model.board.BoardContent;
import com.study.model.board.BoardIdx;
import com.study.model.board.BoardWriter;
import com.study.model.board.Category;
import com.study.model.board.Hit;
import com.study.model.board.ModDate;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import com.study.model.board.Title;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private BoardIdx boardIdx;
    private Category category;
    private Title title;
    private BoardWriter writer;
    private BoardContent content;
    private Password password;
    private Hit hit;
    private RegDate regDate;
    private ModDate modDate;

    public BoardDTO() {
    }

    public BoardDTO(Board board) {
        this.boardIdx = board.getBoardIdx();
        this.category = board.getCategory();
        this.title = board.getTitle();
        this.writer = board.getWriter();
        this.content = board.getContent();
        this.password = board.getPassword();
        this.hit = board.getHit();
        this.regDate = board.getRegDate();
        this.modDate = board.getModDate();
    }
}
