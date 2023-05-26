package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Slf4j
public class ModifyBoardFormController extends Controller implements Serializable {

    private BoardService boardService;

    public ModifyBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDTO boardDTO = boardService.findBoardWithImages(Long.parseLong(req.getParameter("board_idx")));

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardDTO);
        req.getRequestDispatcher("/views/boardModifyView.jsp").forward(req, resp);
    }
}