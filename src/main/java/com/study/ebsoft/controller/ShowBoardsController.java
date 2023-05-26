package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class ShowBoardsController extends Controller implements Serializable {

    private BoardService boardService;

    public ShowBoardsController( BoardService boardService ) {
        this.boardService = boardService;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("boardLists", boardService.findAllBoardsWithFileCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap())));
        req.setAttribute("categorys", boardService.findAllCategorys());

        req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
    }
}