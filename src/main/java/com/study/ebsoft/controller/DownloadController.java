package com.study.ebsoft.controller;

import com.study.core.mvc.AbstractController;
import com.study.core.mvc.Controller;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.repository.file.FileDAO;
import com.study.ebsoft.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class DownloadController extends AbstractController implements Controller {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        FileDTO fileDTO = FileDAO.getInstance().findFileNameById(Long.parseLong(req.getParameter("file_idx")));

        try {
            FileUtils.serveDownloadFile(req, resp, fileDTO.getSavedFileName(), fileDTO.getOriginalFileName());
        } catch (Exception e) {
            // TODO: 에러 페이지로 send_redirect 할것.
            throw new RuntimeException(e);
        }
    }
}
