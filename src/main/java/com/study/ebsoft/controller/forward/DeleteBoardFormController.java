package com.study.ebsoft.controller.forward;

import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;

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
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoSuchElementException {
        BoardDTO boardDTO = boardService.findBoardWithDetails(Long.parseLong(req.getParameter("board_idx")));

        req.setAttribute("board", boardDTO);
        return new View("/views/boardDeleteView.jsp");
    }
}