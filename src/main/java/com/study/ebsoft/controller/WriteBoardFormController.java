package com.study.ebsoft.controller;

import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WriteBoardFormController extends AbstractController implements Controller {

    private BoardService boardService;

    public WriteBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("categorys", boardService.findAllCategorys());
        try {
            req.getRequestDispatcher("/views/boardWriteView.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}