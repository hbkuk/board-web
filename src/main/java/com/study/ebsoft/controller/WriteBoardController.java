package com.study.ebsoft.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Slf4j
public class WriteBoardController extends Controller implements Serializable {

    private BoardService boardService;

    public WriteBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board board = BuildUtils.buildWriteBoardFromRequest(multi);
        List<File> files = BuildUtils.buildFilesFromRequest(multi);

        // DB 저장
        BoardDTO boardDTO = boardService.saveBoardWithImages(board,files);

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", boardDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}