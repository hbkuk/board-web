package com.study.ebsoft.service;

import com.oreilly.servlet.MultipartRequest;
import com.study.core.mvc.Service;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.model.board.Board;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.utils.SearchConditionUtils;
import com.study.ebsoft.repository.file.FileDAO;
import com.study.ebsoft.utils.BuildUtils;
import com.study.ebsoft.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
public class ModifyBoardService extends Service implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

        log.debug(" updateBoardWithImages() 메서드 호출 -> updateBoard : {} , newUploadFiles size : {}, previouslyUploadedIndexes size : {}",
                updateBoard.toString(), newUploadFiles.size(), previouslyUploadedIndexes.size());

        BoardDTO updateReturnBoardDTO = BoardDAO.getInstance().update(updateBoard);
        if (updateReturnBoardDTO == null) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        // DB 확인
        List<Long> dbFileIndexes = FileDAO.getInstance().findFileIndexesByBoardId(updateReturnBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = new ArrayList<Long>(dbFileIndexes);
        indexesToDelete.removeAll(previouslyUploadedIndexes);

        // 저장 디렉토리에서 파일 삭제
        List<String> fileNamesToDelete = indexesToDelete.stream()
                .map(fileIdx -> FileDAO.getInstance().findFileNameById(fileIdx).getSaveFileName())
                .collect(Collectors.toList());

        fileNamesToDelete.stream()
                .forEach(fileName -> FileUtils.deleteUploadedFile(fileName));

        // DB 파일 삭제
        indexesToDelete.stream()
                .forEach(fileIdx -> FileDAO.getInstance().deleteByFileId(fileIdx));

        // 새 이미지 추가
        newUploadFiles.forEach(file -> FileDAO.getInstance().save(file, updateReturnBoardDTO.getBoardIdx()));


        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", updateReturnBoardDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", updateReturnBoardDTO.getBoardIdx(), searchConditionQueryString));
        }
    }
}