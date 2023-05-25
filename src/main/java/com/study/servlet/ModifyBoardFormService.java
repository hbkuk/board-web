package com.study.servlet;

import com.study.dto.BoardDTO;
import com.study.repository.board.BoardDAO;
import com.study.repository.file.FileDAO;
import com.study.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

@Slf4j
public class ModifyBoardFormService implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDTO boardDTO = BoardDAO.getInstance().findById(Long.parseLong(req.getParameter("board_idx")));
        if (boardDTO == null) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("getBoardWithImages() -> findById -> BoardIDX : {}", boardDTO.getBoardIdx());
        boardDTO.setFiles(FileDAO.getInstance().findFilesByBoardId(Long.parseLong(req.getParameter("board_idx"))));

        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardDTO);
        req.getRequestDispatcher("/views/boardModifyView.jsp").forward(req, resp);
    }
}