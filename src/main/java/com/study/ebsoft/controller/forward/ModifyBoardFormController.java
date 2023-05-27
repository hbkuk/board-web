package com.study.ebsoft.controller.forward;

import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * 게시글 번호에 해당하는 게시글 수정 View 담당
 */
@Slf4j
public class ModifyBoardFormController extends AbstractController implements Controller {

    private BoardService boardService;

    public ModifyBoardFormController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글 번호에 해당하는 게시글 수정 정보를 응답합니다
     *
     * @catch 게시글 번호에 해당하는 게시물이 없는 경우 (NoSuchElementException) 에러페이지를 응답합니다
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDTO boardDTO = null;

        try {
            boardDTO = boardService.findBoardWithImages(Long.parseLong(req.getParameter("board_idx")));
        } catch (NoSuchElementException e) {
            req.setAttribute("error_message", e.getMessage());
            req.getRequestDispatcher("/views/error/error404.jsp").forward(req, resp);
        }

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardDTO);

        req.getRequestDispatcher("/views/boardModifyView.jsp").forward(req, resp);
    }
}