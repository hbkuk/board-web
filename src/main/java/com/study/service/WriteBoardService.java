package com.study.service;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.BoardDTO;
import com.study.model.board.Board;
import com.study.model.file.File;
import com.study.repository.board.BoardDAO;
import com.study.repository.file.FileDAO;
import com.study.utils.BuildUtils;
import com.study.utils.FileUtils;
import com.study.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Slf4j
public class WriteBoardService extends Service implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        // DB 저장
        Board board = BuildUtils.buildWriteBoardFromRequest(multi);
        List<File> files = BuildUtils.buildFilesFromRequest(multi);

        log.debug(" saveBoardWithImages() 메서드 호출 -> board : {} , files의 size : {} ",
                board.toString(), files.size());
        BoardDTO boardDTO = BoardDAO.getInstance().save(board);

        if (files.size() != 0) {
            files.forEach(file -> FileDAO.getInstance().save(file, boardDTO.getBoardIdx()));
        }

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", boardDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}