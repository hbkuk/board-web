package com.study.service;

import com.study.repository.file.FileDAO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Slf4j
public class DownloadService extends Service implements Serializable {

    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String root = req.getSession().getServletContext().getRealPath("/");
        log.debug("Root : {}", root);
        String savePath = root + "download";
        log.debug("save Path : {}", savePath);
        String savedFileName = FileDAO.getInstance().findSavedFileNameById(Long.parseLong(req.getParameter("file_idx")));

        req.setAttribute("savePath", savePath);
        req.setAttribute("savedFileName", savedFileName);
        req.getRequestDispatcher("/views/download.jsp").forward(req, resp);
    }
}