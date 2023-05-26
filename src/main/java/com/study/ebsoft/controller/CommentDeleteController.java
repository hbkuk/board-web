package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class CommentDeleteController extends Controller implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        CommentDTO deleteComment = new CommentDTO();
        deleteComment.setCommentIdx(Long.parseLong(req.getParameter("comment_idx")));
        deleteComment.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteComment.setPassword(req.getParameter("password"));

        CommentDTO commentDTO = CommentDAO.getInstance().findByCommentIdx(deleteComment.getCommentIdx());

        if (!commentDTO.getPassword().equals(deleteComment.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
        CommentDAO.getInstance().deleteCommentByCommentIdx(deleteComment);

        long boardIdx = deleteComment.getBoardIdx();

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", boardIdx));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardIdx, searchConditionQueryString));
        }
    }
}