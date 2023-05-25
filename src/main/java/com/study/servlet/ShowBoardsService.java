package com.study.servlet;

import com.study.dto.BoardDTO;
import com.study.dto.CategoryDTO;
import com.study.repository.board.BoardDAO;
import com.study.repository.category.CategoryDAO;
import com.study.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ShowBoardsService implements Serializable {
    public ShowBoardsService() {
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BoardDTO> boars = BoardDAO.getInstance().findAllWithImageCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap()));
        List<CategoryDTO> categorys = CategoryDAO.getInstance().findAll();

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("boardLists", boars);
        req.setAttribute("categorys", categorys);

        req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
    }
}