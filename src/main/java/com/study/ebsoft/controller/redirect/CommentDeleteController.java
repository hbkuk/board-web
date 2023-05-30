package com.study.ebsoft.controller.redirect;

import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 댓글 번호에 해당하는 댓글 삭제 담당
 */
@Slf4j
public class CommentDeleteController implements Controller {

    private BoardService boardService;

    public CommentDeleteController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 댓글 번호에 해당하는 댓글을 삭제합니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap());

        CommentDTO deleteComment = new CommentDTO();
        deleteComment.setCommentIdx(Long.parseLong(req.getParameter("comment_idx")));
        deleteComment.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteComment.setPassword(req.getParameter("password"));

        try {
            boardService.deleteCommentByCommentIdx(deleteComment);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

            return new View(String.format("/board?board_idx=%d", deleteComment.getBoardIdx()));
        }

        if (searchConditionQueryString.isEmpty()) {
            return new View("redirect:" + String.format("/board?board_idx=%d", deleteComment.getBoardIdx()));
        } else {
            return new View("redirect:" + String.format("/board?board_idx=%d&%s", deleteComment.getBoardIdx(), searchConditionQueryString));
        }
    }
}