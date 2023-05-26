package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.utils.SearchConditionUtils;
import com.study.ebsoft.repository.file.FileDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class DeleteBoardFormController extends Controller implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDTO boardDTO = BoardDAO.getInstance().increaseHitCount(Long.parseLong(req.getParameter("board_idx")));
        if (boardDTO == null) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        boardDTO = BoardDAO.getInstance().findById(Long.parseLong(req.getParameter("board_idx")));
        boardDTO.setComments(CommentDAO.getInstance().findAllByBoardId(Long.parseLong(req.getParameter("board_idx"))));
        boardDTO.setFiles(FileDAO.getInstance().findFilesByBoardId(Long.parseLong(req.getParameter("board_idx"))));

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardDTO);
        req.getRequestDispatcher("/views/boardDeleteView.jsp").forward(req, resp);
    }
}