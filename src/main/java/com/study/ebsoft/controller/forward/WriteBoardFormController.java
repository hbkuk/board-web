package com.study.ebsoft.controller.forward;

import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 게시글 작성 View 담당
 */
public class WriteBoardFormController implements Controller {

    private BoardService boardService;

    public WriteBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }


    /**
     * 게시글 작성에 필요한 정보와 View를 응답합니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", boardService.findAllCategorys());

        return new View("/views/boardWriteView.jsp");
    }
}