package com.study.ebsoft.controller;

import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.SearchConditionUtils;
import com.study.ebsoft.repository.file.FileDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteBoardController extends Controller implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BoardDTO deleteBoardDTO = new BoardDTO();
        deleteBoardDTO.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        deleteBoardDTO.setPassword(req.getParameter("password"));

        BoardDTO boardDTO = BoardDAO.getInstance().findById(deleteBoardDTO.getBoardIdx());

        if (!boardDTO.getPassword().equals(deleteBoardDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        CommentDAO.getInstance().deleteAllByBoardIdx(deleteBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = FileDAO.getInstance().findFileIndexesByBoardId(boardDTO.getBoardIdx());

        // 저장 디렉토리에서 파일 삭제
        List<String> fileNamesToDelete = indexesToDelete.stream()
                .map(fileIdx -> FileDAO.getInstance().findFileNameById(fileIdx).getSaveFileName())
                .collect(Collectors.toList());

        fileNamesToDelete.stream()
                .forEach(fileName -> FileUtils.deleteUploadedFile(fileName));

        // DB 파일 삭제
        indexesToDelete.stream()
                .forEach(fileIdx -> FileDAO.getInstance().deleteByFileId(fileIdx));


        BoardDAO.getInstance().deleteById(deleteBoardDTO.getBoardIdx(), deleteBoardDTO.getPassword());


        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect("/boards");
        } else {
            resp.sendRedirect(String.format("/boards?%s", searchConditionQueryString));
        }
    }
}