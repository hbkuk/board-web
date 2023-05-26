package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class WriteBoardFormController extends Controller implements Serializable {

    private BoardService boardService;

    public WriteBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("categorys", boardService.findAllCategorys());
        req.getRequestDispatcher("/views/boardWriteView.jsp").forward(req, resp);
    }
}