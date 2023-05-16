package com.study.dto;

import com.study.model.board.BoardId;
import com.study.model.board.Category;
import com.study.model.board.Content;
import com.study.model.board.Hit;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import com.study.model.board.Title;
import com.study.model.board.Writer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private BoardId boardId;
    private Category category;
    private Title title;
    private Writer writer;
    private Content content;
    private Password password;
    private Hit hit;
    private RegDate regDate;
}
