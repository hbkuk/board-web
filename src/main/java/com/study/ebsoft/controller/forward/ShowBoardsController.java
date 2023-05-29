package com.study.ebsoft.controller.forward;

import com.study.core.mvc.Controller;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 전체 게시물 리스트 View 담당
 */
public class ShowBoardsController implements Controller {

    private BoardService boardService;

    public ShowBoardsController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 검색조건(searchConditionQueryString)에 맞는 전체 게시물 리스트와 View를 응답합니다.
     */
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()));
        req.setAttribute("boardLists", boardService.findAllBoardsWithFileCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap())));
        req.setAttribute("categorys", boardService.findAllCategorys());

        req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
    }
}