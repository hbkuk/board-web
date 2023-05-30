package com.study.ebsoft.controller.forward;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * 게시글 번호에 해당하는 게시글 삭제 View 담당
 */
public class DeleteBoardFormController implements Controller {

    private BoardService boardService;

    public DeleteBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @catch 게시글 번호에 해당하는 게시물이 없는 경우 (NoSuchElementException) 에러페이지를 응답합니다
     */
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDTO boardDTO = null;

        try {
            boardDTO = boardService.findBoardWithDetails(Long.parseLong(req.getParameter("board_idx")));
        } catch (NoSuchElementException e) {
            req.setAttribute("error_message", e.getMessage());
            req.getRequestDispatcher("/views/error/error404.jsp").forward(req, resp);
        }

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()));
        req.setAttribute("board", boardDTO);

        req.getRequestDispatcher("/views/boardDeleteView.jsp").forward(req, resp);
    }
}