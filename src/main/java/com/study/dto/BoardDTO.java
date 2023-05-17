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

import java.util.List;

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
    private boolean hasImage;
    private List<CommentDTO> comments;
    private List<ImageDTO> images;

    public static BoardDTO fromEntity(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardIdx(board.getBoardIdx());
        boardDTO.setCategory(board.getCategory());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setWriter(board.getWriter());
        boardDTO.setContent(board.getContent());
        boardDTO.setPassword(board.getPassword());
        boardDTO.setHit(board.getHit());
        boardDTO.setRegDate(board.getRegDate());
        boardDTO.setModDate(board.getModDate());
        return boardDTO;
    }
}
