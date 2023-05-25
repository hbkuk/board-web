package com.study.servlet;

import com.study.service.*;
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
    private Map<String, Service> serviceMapping;

    @Override
    public void init() {
        serviceMapping = new HashMap<>();

        serviceMapping.put("/boards", new ShowBoardsService());
        serviceMapping.put("/board", new ShowBoardService());
        serviceMapping.put("/board/write/form", new WriteBoardFormService());
        serviceMapping.put("/board/modify/form", new ModifyBoardFormService());
        serviceMapping.put("/board/delete/form", new DeleteBoardFormService());
        serviceMapping.put("/board/delete", new DeleteBoardService());
        serviceMapping.put("/comment/delete", new CommentDeleteService());
        serviceMapping.put("/board/modify", new ModifyBoardService());
        serviceMapping.put("/board/write", new WriteBoardService());
        serviceMapping.put("/comment/write", new WriteCommentService());
        serviceMapping.put("/download", new DownloadService());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        String method = req.getMethod();
        log.debug("Request URI: {}", requestUri);
        log.debug("Request Method: {}", method);

        Service service = serviceMapping.get(requestUri);
        if (service != null) {
            service.process(req, resp);
        } else {
            log.error("Throw Exception!!!");
            throw new RuntimeException();
        }
    }
}
