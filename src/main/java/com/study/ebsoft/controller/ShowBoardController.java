package com.study.ebsoft.controller;

import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowBoardController extends AbstractController implements Controller {

    private BoardService boardService;

    public ShowBoardController(BoardService boardService ) {
        this.boardService = boardService;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        BoardDTO boardDTO = boardService.findBoardWithDetails((Long.parseLong(req.getParameter("board_idx"))));

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardDTO);
        try {
            req.getRequestDispatcher("/views/boardView.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}