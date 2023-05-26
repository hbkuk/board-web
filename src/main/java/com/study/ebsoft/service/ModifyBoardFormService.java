package com.study.ebsoft.service;

import com.study.core.mvc.Service;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.utils.SearchConditionUtils;
import com.study.ebsoft.repository.file.FileDAO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

@Slf4j
public class ModifyBoardFormService extends Service implements Serializable {

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