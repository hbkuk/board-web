package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import com.study.ebsoft.repository.file.FileDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class DeleteBoardFormController extends Controller implements Serializable {

    private BoardService boardService;

    public DeleteBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDTO boardDTO = boardService.findBoardWithDetails(Long.parseLong(req.getParameter("board_idx")));

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardDTO);
        req.getRequestDispatcher("/views/boardDeleteView.jsp").forward(req, resp);
    }
}