package com.study.service;

import com.study.dto.BoardDTO;
import com.study.dto.CommentDTO;
import com.study.model.board.BoardIdx;
import com.study.model.board.Password;
import com.study.model.comment.Comment;
import com.study.model.comment.CommentContent;
import com.study.model.comment.CommentWriter;
import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

@Slf4j
public class WriteCommentService extends Service implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        Comment comment = new Comment(
                new CommentWriter(req.getParameter("comment_writer")),
                new Password(req.getParameter("comment_password")),
                new CommentContent(req.getParameter("comment_content")),
                new BoardIdx(Long.parseLong(req.getParameter("board_idx"))));

        BoardDTO boardDTO = BoardDAO.getInstance().findById(comment.getBoardIdx().getBoardIdx());
        if (boardDTO == null) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("New Comment / request! Comment  : {} ", comment);

        CommentDTO commentDTO = CommentDAO.getInstance().save(comment);

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", commentDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", commentDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}