package com.study.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            ShowBoardsService showBoardsService = new ShowBoardsService();
            showBoardsService.process(req, resp);
        }

        if (requestUri.equals("/board")) {
            ShowBoardService showBoardService = new ShowBoardService();
            showBoardService.process(req, resp);
        }

        if (requestUri.equals("/board/write") && req.getMethod().equals("GET")) {
            WriteBoardFormService writeBoardFormService = new WriteBoardFormService();
            writeBoardFormService.process(req, resp);
        }

        if (requestUri.equals("/board/modify") && req.getMethod().equals("GET")) {
            ModifyBoardFormService modifyBoardFormService = new ModifyBoardFormService();
            modifyBoardFormService.process(req, resp);
        }

        if (requestUri.equals("/board/delete") && req.getMethod().equals("GET")) {
            DeleteBoardFormService deleteBoardFormService = new DeleteBoardFormService();
            deleteBoardFormService.process(req, resp);
        }

        if (requestUri.equals("/board/delete") && req.getMethod().equals("POST")) {
            DeleteBoardService deleteBoardService = new DeleteBoardService();
            deleteBoardService.process(req, resp);
        }

        if (requestUri.equals("/comment/delete") && req.getMethod().equals("POST")) {
            CommentDeleteService commentDeleteService = new CommentDeleteService();
            commentDeleteService.process(req, resp);
        }

        if (requestUri.equals("/board/modify") && req.getMethod().equals("POST")) {
            ModifyBoardService modifyBoardService = new ModifyBoardService();
            modifyBoardService.process(req, resp);
        }

        if (requestUri.equals("/board/write") && req.getMethod().equals("POST")) {
            WriteBoardService writeBoardService = new WriteBoardService();
            writeBoardService.process(req, resp);
        }

        if (requestUri.equals("/comment/write") && req.getMethod().equals("POST")) {
            WriteCommentService writeCommentService = new WriteCommentService();
            writeCommentService.process(req, resp);
        }

        if (requestUri.equals("/download") && req.getMethod().equals("GET")) {
            DownloadService downloadService = new DownloadService();
            downloadService.process(req, resp);
        }
    }
}
