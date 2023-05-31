package com.study.ebsoft.controller.redirect;

import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 게시물 번호에 해당하는 게시물 삭제 담당
 */
@Slf4j
public class DeleteBoardController implements Controller {

    private BoardService boardService;

    public DeleteBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시물 번호에 해당하는 게시물 삭제을 삭제합니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        BoardDTO deleteBoardDTO = new BoardDTO();
        deleteBoardDTO.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteBoardDTO.setPassword(req.getParameter("password"));

        try {
            boardService.deleteBoardWithFilesAndComment(deleteBoardDTO);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

            return new View(String.format("/board/delete/form?board_idx=%d", deleteBoardDTO.getBoardIdx()));
        }

        return new View("redirect:/boards");
    }
}