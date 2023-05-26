package com.study.ebsoft.controller;

import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteBoardController extends AbstractController implements Controller {

    private BoardService boardService;

    public DeleteBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        BoardDTO deleteBoardDTO = new BoardDTO();
        deleteBoardDTO.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteBoardDTO.setPassword(req.getParameter("password"));

        boardService.deleteBoardWithFilesAndComment(deleteBoardDTO);

        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        try {
            if (searchConditionQueryString.isEmpty()) {
                resp.sendRedirect("/boards");
            } else {
                resp.sendRedirect(String.format("/boards?%s", searchConditionQueryString));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}