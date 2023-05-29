package com.study.ebsoft.controller.redirect;

import com.oreilly.servlet.MultipartRequest;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.model.board.Board;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.BuildUtils;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class WriteBoardController implements Controller {

    private BoardService boardService;

    public WriteBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap());

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board board = null;
        List<File> files = null;
        try {
            board = BuildUtils.buildWriteBoardFromRequest(multi);
            files = BuildUtils.buildFilesFromRequest(multi);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error_message", e.getMessage());
            if (searchConditionQueryString.isEmpty()) {
                resp.sendRedirect(String.format("/board?board_idx=%d", board.getBoardIdx().getBoardIdx()));
            } else {
                resp.sendRedirect(String.format("/board?board_idx=%d&%s", board.getBoardIdx().getBoardIdx()));
            }
        }

        BoardDTO boardDTO = boardService.saveBoardWithImages(board,files);
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", boardDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}