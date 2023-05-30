package com.study.core.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 클라이언트의 요청에 대한 응답을 렌더링하는 뷰를 나타냅니다
 */
public class View {
    private static final String EXCLUDED_DOWNLOAD_VIEW_NAME = "download";
    private static final String REDIRECT_PREFIX = "redirect:";
    private final String viewName;

    /**
     * 지정된 뷰 이름으로 새로운 View 객체를 생성합니다
     *
     * @param viewName 뷰 이름
     */
    public View(String viewName) {
        this.viewName = viewName;
    }

    /**
     * 뷰를 렌더링하여 요청을 포워딩하거나 리다이렉트 응답을 보냅니다
     *
     * @param req  HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 요청 포워딩 중에 오류가 발생한 경우
     * @throws IOException      요청 포워딩 또는 리다이렉트 중에 입출력 오류가 발생한 경우
     */
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isExcludedViewName()) {
            return;
        }
        if (isRedirectViewName()) {
            resp.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }
        req.getRequestDispatcher(viewName).forward(req, resp);
    }

    /**
     * 뷰 이름이 리다이렉트 접두사로 시작한다면 true, 그렇지 않다면 false
     *
     * @return 뷰 이름이 리다이렉트 접두사로 시작하면 true, 그렇지 않으면 false
     */
    private boolean isRedirectViewName() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    /**
     * 뷰 이름이 제외된 렌더링 이름이라면 true, 그렇지 않다면 false
     *
     * @return 뷰 이름이 제외된 다운로드 뷰 이름과 일치하면 true, 그렇지 않으면 false
     */
    private boolean isExcludedViewName() {
        return viewName.equals(EXCLUDED_DOWNLOAD_VIEW_NAME);
    }


    /**
     * 뷰의 이름을 가져옵니다.
     *
     * @return 뷰 이름
     */
    public String getViewName() {
        return viewName;
    }
}
