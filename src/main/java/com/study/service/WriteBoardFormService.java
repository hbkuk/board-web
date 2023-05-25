package com.study.service;

import com.study.dto.CategoryDTO;
import com.study.repository.category.CategoryDAO;
import com.study.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class WriteBoardFormService extends Service implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CategoryDTO> categorys = CategoryDAO.getInstance().findAll();

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("categorys", categorys);
        req.getRequestDispatcher("/views/boardWriteView.jsp").forward(req, resp);
    }
}