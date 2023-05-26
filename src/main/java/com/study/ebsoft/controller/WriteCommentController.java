package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.model.board.BoardIdx;
import com.study.ebsoft.model.board.Password;
import com.study.ebsoft.model.comment.Comment;
import com.study.ebsoft.model.comment.CommentContent;
import com.study.ebsoft.model.comment.CommentWriter;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Slf4j
public class WriteCommentController extends Controller implements Serializable {

    private BoardService boardService;

    public WriteCommentController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        Comment comment = new Comment(
                new CommentWriter(req.getParameter("comment_writer")),
                new Password(req.getParameter("comment_password")),
                new CommentContent(req.getParameter("comment_content")),
                new BoardIdx(Long.parseLong(req.getParameter("board_idx"))));

        CommentDTO commentDTO = boardService.saveComment(comment);

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", commentDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", commentDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}