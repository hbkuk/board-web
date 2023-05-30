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
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModifyBoardController implements Controller {

    private BoardService boardService;

    public ModifyBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap());

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board updateBoard = null;
        List<File> newUploadFiles = null;
        try {
            updateBoard = BuildUtils.buildModifyBoardFromRequest(multi);
            newUploadFiles = BuildUtils.buildFilesFromRequest(multi);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

            req.getRequestDispatcher(String.format("/boards/modify/form?board_idx=%d", Long.parseLong(multi.getParameter("board_idx")))).forward(req, resp);
            return;
        }

        List<Long> previouslyUploadedIndexes = new ArrayList<Long>();
        if (multi.getParameter("file_idx") != null) {
            for (String item : multi.getParameterValues("file_idx")) {
                previouslyUploadedIndexes.add(Long.parseLong(item));
            }
        }

        BoardDTO updateReturnBoardDTO = null;
        try {
            updateReturnBoardDTO = boardService.updateBoardWithImages(updateBoard, newUploadFiles, previouslyUploadedIndexes);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

            req.getRequestDispatcher(String.format("/board/modify/form?board_idx=%d", updateBoard.getBoardIdx().getBoardIdx())).forward(req, resp);
            return;
        }

        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", updateReturnBoardDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", updateReturnBoardDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}