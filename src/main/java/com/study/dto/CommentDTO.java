package com.study.dto;

import com.study.model.board.BoardIdx;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import com.study.model.comment.Comment;
import com.study.model.comment.CommentIdx;
import com.study.model.comment.CommentWriter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDTO {
    private CommentIdx commentIdx;
    private CommentWriter writer;
    private Password password;
    private String content;
    private RegDate regDate;
    private BoardIdx boardIdx;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        this.commentIdx = comment.getCommentIdx();
        this.writer = comment.getWriter();
        this.password = comment.getPassword();
        this.content = comment.getContent();
        this.regDate = comment.getRegDate();
        this.boardIdx = comment.getBoardIdx();
    }
}
