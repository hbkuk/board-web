package com.study.core.mvc;

import com.study.ebsoft.controller.forward.*;
import com.study.ebsoft.controller.redirect.*;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.repository.category.CategoryDAO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.repository.file.FileDAO;
import com.study.ebsoft.service.BoardService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 클라이언트로부터 모든 요청을 수신하는 서블릿 컨테이너
 */
@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private Map<String, Controller> requestMap;
    private BoardService boardService = new BoardService(
            BoardDAO.getInstance(), CommentDAO.getInstance(), CategoryDAO.getInstance(), FileDAO.getInstance());

    @Override
    public void init() {
        requestMap = new HashMap<>();

        requestMap.put("/boards", new ShowBoardsController(boardService));
        requestMap.put("/board", new ShowBoardController(boardService));
        requestMap.put("/board/write/form", new WriteBoardFormController(boardService));
        requestMap.put("/board/modify/form", new ModifyBoardFormController(boardService));
        requestMap.put("/board/delete/form", new DeleteBoardFormController(boardService));
        requestMap.put("/board/delete", new DeleteBoardController(boardService));
        requestMap.put("/comment/delete", new CommentDeleteController(boardService));
        requestMap.put("/board/modify", new ModifyBoardController(boardService));
        requestMap.put("/board/write", new WriteBoardController(boardService));
        requestMap.put("/comment/write", new WriteCommentController(boardService));
        requestMap.put("/download", new DownloadController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String requestUri = req.getRequestURI();
        String method = req.getMethod();
        log.debug("Request URI: {}", requestUri);
        log.debug("Request Method: {}", method);

        Controller controller = requestMap.get(requestUri);
        try {
            if (controller != null) {
                controller.process(req, resp);
            } else {
                // TODO -> REDIRECT -> 404 NOT FOUND??
                log.error("an exception occurred");
            }
        } catch (Throwable e) {
            log.debug("error log : {} ", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
