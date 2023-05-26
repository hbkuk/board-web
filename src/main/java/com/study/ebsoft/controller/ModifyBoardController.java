package com.study.ebsoft.controller;

import com.oreilly.servlet.MultipartRequest;
import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.model.board.Board;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.BuildUtils;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModifyBoardController extends AbstractController implements Controller {

    private BoardService boardService;

    public ModifyBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board updateBoard = BuildUtils.buildModifyBoardFromRequest(multi);

        List<File> newUploadFiles = BuildUtils.buildFilesFromRequest(multi);
        log.debug("새로 업로드 된 파일의 개수 : {} ", newUploadFiles.size());

        List<Long> previouslyUploadedIndexes = new ArrayList<Long>();
        if (multi.getParameter("file_idx") != null) {
            for (String item : multi.getParameterValues("file_idx")) {
                previouslyUploadedIndexes.add(Long.parseLong(item));
            }
        }

        BoardDTO updateReturnBoardDTO = boardService.updateBoardWithImages(
                                        updateBoard, newUploadFiles, previouslyUploadedIndexes);

        try {
            if (searchConditionQueryString.isEmpty()) {
                resp.sendRedirect(String.format("/board?board_idx=%d", updateReturnBoardDTO.getBoardIdx()));
            } else {
                resp.sendRedirect(String.format("/board?board_idx=%d&%s", updateReturnBoardDTO.getBoardIdx(), searchConditionQueryString));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}