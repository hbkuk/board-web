package com.study.ebsoft.service;

import com.study.core.mvc.Service;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.utils.SearchConditionUtils;
import com.study.ebsoft.repository.category.CategoryDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ShowBoardsService extends Service implements Serializable {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BoardDTO> boars = BoardDAO.getInstance().findAllWithImageCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap()));
        List<CategoryDTO> categorys = CategoryDAO.getInstance().findAll();

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("boardLists", boars);
        req.setAttribute("categorys", categorys);

        req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
    }
}