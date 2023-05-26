package com.study.core.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractController implements Controller {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {};

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {};

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod() == "GET") {
            doGet(req, resp);
        }

        if (req.getMethod() == "POST") {
            doPost(req, resp);
        }
    }

}
