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

@Slf4j
public class DeleteBoardController implements Controller {

    private BoardService boardService;

    public DeleteBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap());

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

        if (searchConditionQueryString.isEmpty()) {
            return new View("redirect:/boards");
        } else {
            return new View("redirect:" + String.format("/boards?%s", searchConditionQueryString));
        }
    }
}