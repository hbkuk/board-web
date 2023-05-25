package com.study.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract  class Service {

    public abstract void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
