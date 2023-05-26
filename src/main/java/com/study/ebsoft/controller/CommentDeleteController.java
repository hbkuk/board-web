package com.study.ebsoft.controller;

import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentDeleteController extends AbstractController implements Controller {

    private BoardService boardService;

    public CommentDeleteController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        CommentDTO deleteComment = new CommentDTO();
        deleteComment.setCommentIdx(Long.parseLong(req.getParameter("comment_idx")));
        deleteComment.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteComment.setPassword(req.getParameter("password"));

        long boardIdx = boardService.deleteCommentByCommentIdx(deleteComment);

        // 저장 후 이동
        try {
            if (searchConditionQueryString.isEmpty()) {
                resp.sendRedirect(String.format("/board?board_idx=%d", boardIdx));
            } else {
                resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardIdx, searchConditionQueryString));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}