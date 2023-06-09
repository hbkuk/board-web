package com.study.ebsoft.controller.redirect;

import com.oreilly.servlet.MultipartRequest;
import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
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

/**
 * 게시물 작성 담당
 */
@Slf4j
public class WriteBoardController implements Controller {

    private BoardService boardService;

    public WriteBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시물을 작성합니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board board = null;
        List<File> files = null;
        try {
            board = BuildUtils.buildWriteBoardFromRequest(multi);
            files = BuildUtils.buildFilesFromRequest(multi);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

            return new View("/board/write/form");
        }

        BoardDTO boardDTO = boardService.saveBoardWithImages(board,files);

        return new View("redirect:" + String.format("/board?board_idx=%d", boardDTO.getBoardIdx()));
    }
}