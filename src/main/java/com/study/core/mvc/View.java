package com.study.core.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class View {
    private static final String EXCLUDED_DOWNLOAD_VIEW_NAME = "download";
    private static final String REDIRECT_PREFIX = "redirect:";
    private final String viewName;

    public View(String viewName) {
        this.viewName = viewName;
    }

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

    private boolean isRedirectViewName() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private boolean isExcludedViewName() {
        return viewName.equals(EXCLUDED_DOWNLOAD_VIEW_NAME);
    }

    public String getViewName() {
        return viewName;
    }
}
