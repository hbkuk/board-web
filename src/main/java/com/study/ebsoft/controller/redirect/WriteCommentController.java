package com.study.ebsoft.controller.redirect;

import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.model.board.BoardIdx;
import com.study.ebsoft.model.board.Password;
import com.study.ebsoft.model.comment.Comment;
import com.study.ebsoft.model.comment.CommentContent;
import com.study.ebsoft.model.comment.CommentWriter;
import com.study.ebsoft.service.BoardService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 댓글 작성 담당
 */
@Slf4j
public class WriteCommentController implements Controller {

    private BoardService boardService;

    public WriteCommentController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 댓글을 작성합니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Comment comment = null;
        try {
            comment = new Comment(
                    new CommentWriter(req.getParameter("comment_writer")),
                    new Password(req.getParameter("comment_password")),
                    new CommentContent(req.getParameter("comment_content")),
                    new BoardIdx(Long.parseLong(req.getParameter("board_idx"))));
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

            return new View(String.format("/board?board_idx=%d", Long.parseLong(req.getParameter("board_idx"))));
        }
        CommentDTO commentDTO = boardService.saveComment(comment);

        return new View("redirect:" + String.format("/board?board_idx=%d", commentDTO.getBoardIdx()));
    }
}