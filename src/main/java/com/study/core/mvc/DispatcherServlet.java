package com.study.core.mvc;

import com.study.ebsoft.controller.forward.*;
import com.study.ebsoft.controller.redirect.*;
import com.study.ebsoft.exception.SearchConditionException;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.repository.category.CategoryDAO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.repository.file.FileDAO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 클라이언트로부터 모든 요청을 수신하는 서블릿 컨테이너
 */
@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private Map<String, Controller> requestMap;
    private BoardService boardService = new BoardService(
            BoardDAO.getInstance(), CommentDAO.getInstance(), CategoryDAO.getInstance(), FileDAO.getInstance());

    /**
     * 서블릿 컨테이너로부터 초기화를 진행하고, 클라이언트로부터 요청에 대한 처리를 위한 매핑을 설정합니다
     */
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

    /**
     * 서블릿 컨테이너로부터 호출되며, 요청 URI와 HTTP Method를 확인하여 해당 컨트롤러를 호출합니다
     * 만약 요청에 대한 매핑이 없는 경우 404 에러 페이지로 리다이렉트합니다
     *
     * @param req  클라이언트로부터의 HttpServletRequest 객체입니다.
     * @param resp 클라이언트로부터의 HttpServletResponse 객체입니다.
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String requestUri = req.getRequestURI();
        String method = req.getMethod();
        log.debug("Request URI: {}", requestUri);
        log.debug("Request Method: {}", method);

        Controller controller = requestMap.get(requestUri);
        try {
            try {
                if (controller != null) {
                    req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()));

                    View view = controller.process(req, resp);
                    view.render(req, resp);

                    log.error("View Name : {}", view.getViewName());
                } else {
                    log.error("Not found Page...");
                    resp.sendRedirect("/views/error/error404.jsp");
                }
            } catch (SearchConditionException e) {
                log.error("error : {}", e.getMessage());

                String encodedErrorMessage = URLEncoder.encode(e.getMessage(), "UTF-8");
                resp.sendRedirect("/boards?error=" + encodedErrorMessage);
            } catch (NoSuchElementException e) {
                log.error("NoSuchElementException --> Not found...");
                resp.sendRedirect("/views/error/error404.jsp");
            }
        } catch (Throwable e) {
            log.debug("error log : {} ", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
