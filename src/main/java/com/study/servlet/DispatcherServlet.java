package com.study.servlet;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.BoardDTO;
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
import com.study.service.BoardService;
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

/**
 *  클라이언트로부터 모든 요청을 수신하는 서블릿 컨테이너
 */
@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private final BoardService boardService = new BoardService(BoardDAO.getInstance(), CommentDAO.getInstance(),
            FileDAO.getInstance(), CategoryDAO.getInstance() );

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        log.debug("Request URI: {}", requestUri);
        log.debug("Request Method: {}", req.getMethod());

        if (requestUri.equals("/boards")) {
            showBoardsService(req, resp);
        }

        if (requestUri.equals("/board")) {
            showBoardService(req, resp);
        }

        if (requestUri.equals("/board/write") && req.getMethod().equals("GET")) {
            writeBoardFormService(req, resp);
        }

        if (requestUri.equals("/board/modify")) {
            modifyBoardFormService(req, resp);
        }

        if (requestUri.equals("/board/delete") && req.getMethod().equals("GET") ) {
            deleteBoardFormService(req, resp);
        }

        if (requestUri.equals("/board/delete") && req.getMethod().equals("POST")) {
            deleteBoardService(req, resp);
        }

        if (requestUri.equals("/comment/delete") && req.getMethod().equals("POST")) {
            deleteCommentService(req, resp);
        }

        if (requestUri.equals("/board/modify") && req.getMethod().equals("POST")) {
            modifyBoardService(req, resp);
        }

        if (requestUri.equals("/board/write") && req.getMethod().equals("POST")) {
            writeBoardService(req, resp);
        }

        if (requestUri.equals("/comment/write") && req.getMethod().equals("POST")) {
            writeCommentService(req, resp);
        }
    }

    private void writeCommentService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        Comment comment = new Comment(
                new CommentWriter(req.getParameter("comment_writer")),
                new Password(req.getParameter("comment_password")),
                new CommentContent(req.getParameter("comment_content")),
                new BoardIdx(Long.parseLong(req.getParameter("board_idx"))));

        CommentDTO commentDTO = boardService.saveComment(comment);


        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", commentDTO.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", commentDTO.getBoardIdx(), searchConditionQueryString));
        }
    }

    private void writeBoardService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        // DB 저장
        BoardDTO board = boardService.saveBoardWithImages(BuildUtils.buildWriteBoardFromRequest(multi), BuildUtils.buildFilesFromRequest(multi));

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", board.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", board.getBoardIdx(), searchConditionQueryString));
        }
    }

    private void modifyBoardService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        // 파일 업로드
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board board = BuildUtils.buildModifyBoardFromRequest(multi);

        List<File> newFiles = BuildUtils.buildFilesFromRequest(multi);

        List<Long> oldFiles = new ArrayList<>();
        if(multi.getParameter("file_idx") != null) {
            for (String item : multi.getParameterValues("file_idx")) {
                oldFiles.add(Long.parseLong(item));
            }
        }

        BoardDTO boardDTO = boardService.updateBoardWithImages(board, newFiles, oldFiles);

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", board.getBoardIdx()));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", board.getBoardIdx(), searchConditionQueryString));
        }
    }

    private void deleteCommentService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentIdx(Long.parseLong(req.getParameter("comment_idx")));
        commentDTO.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        commentDTO.setPassword(req.getParameter("password"));

        long boardIdx = boardService.deleteCommentByCommentIdx(commentDTO);

        // 저장 후 이동
        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect(String.format("/board?board_idx=%d", boardIdx));
        } else {
            resp.sendRedirect(String.format("/board?board_idx=%d&%s", boardIdx, searchConditionQueryString));
        }
    }

    private void deleteBoardService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchConditionQueryString = SearchConditionUtils.buildQueryString(req.getParameterMap()).toString();

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardIdx(Long.parseLong(req.getParameter("board_idx")));
        boardDTO.setPassword(req.getParameter("password"));
        boardService.deleteBoardWithFilesAndComment(boardDTO);

        if (searchConditionQueryString.isEmpty()) {
            resp.sendRedirect("/boards");
        } else {
            resp.sendRedirect(String.format("/boards?%s", searchConditionQueryString));
        }
    }

    private void deleteBoardFormService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardService.getBoardWithDetails(Long.parseLong(req.getParameter("board_idx"))));
        req.getRequestDispatcher("/views/boardDeleteView.jsp").forward(req, resp);
    }

    private void modifyBoardFormService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardService.getBoardWithImages(Long.parseLong(req.getParameter("board_idx"))));
        req.getRequestDispatcher("/views/boardModifyView.jsp").forward(req, resp);
    }

    private void writeBoardFormService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("categorys", boardService.getAllCategory());
        req.getRequestDispatcher("/views/boardWriteView.jsp").forward(req, resp);
    }

    private void showBoardService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("board", boardService.getBoardWithDetails(Long.parseLong(req.getParameter("board_idx"))));
        req.getRequestDispatcher("/views/boardView.jsp").forward(req, resp);
    }

    private void showBoardsService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()).toString());
        req.setAttribute("boardLists", boardService.getBoardListDetails(SearchConditionUtils.buildQueryCondition(req.getParameterMap())));
        req.setAttribute("categorys", boardService.getAllCategory());
        req.getRequestDispatcher("/views/boardLists.jsp").forward(req, resp);
    }
}
