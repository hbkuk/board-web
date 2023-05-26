package com.study.core.mvc;

import com.study.ebsoft.controller.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 클라이언트로부터 모든 요청을 수신하는 서블릿 컨테이너
 */
@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private Map<String, Controller> requestMap;

    @Override
    public void init() {
        requestMap = new HashMap<>();

        requestMap.put("/boards", new ShowBoardsController());
        requestMap.put("/board", new ShowBoardController());
        requestMap.put("/board/write/form", new WriteBoardFormController());
        requestMap.put("/board/modify/form", new ModifyBoardFormController());
        requestMap.put("/board/delete/form", new DeleteBoardFormController());
        requestMap.put("/board/delete", new DeleteBoardController());
        requestMap.put("/comment/delete", new CommentDeleteController());
        requestMap.put("/board/modify", new ModifyBoardController());
        requestMap.put("/board/write", new WriteBoardController());
        requestMap.put("/comment/write", new WriteCommentController());
        requestMap.put("/download", new DownloadController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        String method = req.getMethod();
        log.debug("Request URI: {}", requestUri);
        log.debug("Request Method: {}", method);

        Controller controller = requestMap.get(requestUri);
        if (controller != null) {
            controller.process(req, resp);
        } else {
            log.error("Throw Exception!!!");
            throw new RuntimeException();
        }
    }
}
