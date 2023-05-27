package com.study.core.mvc;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AbstractController implements Controller {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {};

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {};

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String httpMethod = req.getMethod();
        log.debug("request...Http Method : {}", httpMethod);

        if(httpMethod.equals("GET")) {
            doGet(req, resp);
        }
        if(httpMethod.equals("POST")) {
            doPost(req, resp);
        }
    }
}
