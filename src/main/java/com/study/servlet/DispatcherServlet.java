package com.study.servlet;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.BoardDTO;
import com.study.dto.CategoryDTO;
import com.study.dto.CommentDTO;
import com.study.model.board.Board;
import com.study.model.board.BoardIdx;
import com.study.model.board.Password;
import com.study.model.comment.Comment;
import com.study.model.comment.CommentContent;
import com.study.model.comment.CommentWriter;
import com.study.model.file.File;
import com.study.repository.board.BoardDAO;
import com.study.repository.category.CategoryDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.FileDAO;
import com.study.utils.BuildUtils;
import com.study.utils.FileUtils;
import com.study.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 클라이언트로부터 모든 요청을 수신하는 서블릿 컨테이너
 */
@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        log.debug("Request URI: {}", requestUri);
        log.debug("Request Method: {}", req.getMethod());

        if (requestUri.equals("/boards")) {

            List<BoardDTO> boars = BoardDAO.getInstance().findAllWithImageCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap()));
            List<CategoryDTO> categorys = CategoryDAO.getInstance().findAll();

            req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
            req.setAttribute("boardLists", boars);
            req.setAttribute("categorys", categorys);

            req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
        }

        if (requestUri.equals("/board")) {

            BoardDTO boardDTO = BoardDAO.getInstance().increaseHitCount(Long.parseLong(req.getParameter("board_idx")));
            if (boardDTO == null) {
                throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
            }
            boardDTO = BoardDAO.getInstance().findById(Long.parseLong(req.getParameter("board_idx")));
            boardDTO.setComments(CommentDAO.getInstance().findAllByBoardId(Long.parseLong(req.getParameter("board_idx"))));
            boardDTO.setFiles(FileDAO.getInstance().findFilesByBoardId(Long.parseLong(req.getParameter("board_idx"))));

            req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
            req.setAttribute("board", boardDTO);
            req.getRequestDispatcher("/views/boardView.jsp").forward(req, resp);
        }

        if (requestUri.equals("/board/write") && req.getMethod().equals("GET")) {
            List<CategoryDTO> categorys = CategoryDAO.getInstance().findAll();

            req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
            req.setAttribute("categorys", categorys);
            req.getRequestDispatcher("/views/boardWriteView.jsp").forward(req, resp);
        }

        if (requestUri.equals("/board/modify") && req.getMethod().equals("GET")) {
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

        if (requestUri.equals("/board/delete") && req.getMethod().equals("GET")) {
            BoardDTO boardDTO = BoardDAO.getInstance().increaseHitCount(Long.parseLong(req.getParameter("board_idx")));
            if (boardDTO == null) {
                throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
            }
            boardDTO = BoardDAO.getInstance().findById(Long.parseLong(req.getParameter("board_idx")));
            boardDTO.setComments(CommentDAO.getInstance().findAllByBoardId(Long.parseLong(req.getParameter("board_idx"))));
            boardDTO.setFiles(FileDAO.getInstance().findFilesByBoardId(Long.parseLong(req.getParameter("board_idx"))));

            req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
            req.setAttribute("board", boardDTO);
            req.getRequestDispatcher("/views/boardDeleteView.jsp").forward(req, resp);
        }

        if (requestUri.equals("/board/delete") && req.getMethod().equals("POST")) {
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
                    .map(fileIdx -> FileDAO.getInstance().findSavedFileNameById(fileIdx))
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

        if (requestUri.equals("/comment/delete") && req.getMethod().equals("POST")) {

            String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

            CommentDTO deleteComment = new CommentDTO();
            deleteComment.setCommentIdx(Long.parseLong(req.getParameter("comment_idx")));
            deleteComment.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
            deleteComment.setPassword(req.getParameter("password"));

            CommentDTO commentDTO = CommentDAO.getInstance().findByCommentIdx(deleteComment.getCommentIdx());

            if (!commentDTO.getPassword().equals(deleteComment.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 다릅니다.");
            }
            CommentDAO.getInstance().deleteCommentByCommentIdx(deleteComment);

            long boardIdx = deleteComment.getBoardIdx();

            // 저장 후 이동
            if (searchConditionQueryString.isEmpty()) {
                resp.sendRedirect(String.format("/board?board_idx=%d", boardIdx));
            } else {
                resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardIdx, searchConditionQueryString));
            }
        }

        if (requestUri.equals("/board/modify") && req.getMethod().equals("POST")) {
            String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

            // 파일 업로드
            MultipartRequest multi = FileUtils.fileUpload(req);

            Board updateBoard = BuildUtils.buildModifyBoardFromRequest(multi);

            List<File> newUploadFiles = BuildUtils.buildFilesFromRequest(multi);
            log.debug("새로 업로드 된 파일의 개수 : {} ", newUploadFiles.size());

            List<Long> previouslyUploadedIndexes = new ArrayList<>();
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

            List<Long> indexesToDelete = new ArrayList<>(dbFileIndexes);
            indexesToDelete.removeAll(previouslyUploadedIndexes);

            // 저장 디렉토리에서 파일 삭제
            List<String> fileNamesToDelete = indexesToDelete.stream()
                    .map(fileIdx -> FileDAO.getInstance().findSavedFileNameById(fileIdx))
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

        if (requestUri.equals("/board/write") && req.getMethod().equals("POST")) {
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

        if (requestUri.equals("/comment/write") && req.getMethod().equals("POST")) {
            String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

            Comment comment = new Comment(
                    new CommentWriter(req.getParameter("comment_writer")),
                    new Password(req.getParameter("comment_password")),
                    new CommentContent(req.getParameter("comment_content")),
                    new BoardIdx(Long.parseLong(req.getParameter("board_idx"))));

            BoardDTO boardDTO = BoardDAO.getInstance().findById(comment.getBoardIdx().getBoardIdx());
            if (boardDTO == null) {
                throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
            }
            log.debug("New Comment / request! Comment  : {} ", comment);

            CommentDTO commentDTO = CommentDAO.getInstance().save(comment);

            // 저장 후 이동
            if (searchConditionQueryString.isEmpty()) {
                resp.sendRedirect(String.format("/board?board_idx=%d", commentDTO.getBoardIdx()));
            } else {
                resp.sendRedirect(String.format("/board?board_idx=%d&%s", commentDTO.getBoardIdx(), searchConditionQueryString));
            }
        }

        if (requestUri.equals("/download") && req.getMethod().equals("GET")) {
            String root = req.getSession().getServletContext().getRealPath("/");
            log.debug("Root : {}", root);
            String savePath = root + "download";
            log.debug("save Path : {}", savePath);
            String savedFileName = FileDAO.getInstance().findSavedFileNameById(Long.parseLong(req.getParameter("file_idx")));

            req.setAttribute("savePath", savePath);
            req.setAttribute("savedFileName", savedFileName);
            req.getRequestDispatcher("/views/download.jsp").forward(req, resp);
        }
    }

}
