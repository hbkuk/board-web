package com.study.ebsoft.controller.redirect;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentDeleteController implements Controller {

    private BoardService boardService;

    public CommentDeleteController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap());

        CommentDTO deleteComment = new CommentDTO();
        deleteComment.setCommentIdx(Long.parseLong(req.getParameter("comment_idx")));
        deleteComment.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteComment.setPassword(req.getParameter("password"));

        try {
            boardService.deleteCommentByCommentIdx(deleteComment);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error_message", e.getMessage());
            req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()));
            req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
        }

        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", deleteComment.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", deleteComment.getBoardIdx(), searchConditionQueryString));
        }
    }
}