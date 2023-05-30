package com.study.ebsoft.controller.forward;

import com.study.core.mvc.Controller;
import com.study.core.mvc.View;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.repository.file.FileDAO;
import com.study.ebsoft.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DownloadController implements Controller {

    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileDTO fileDTO = FileDAO.getInstance().findFileNameById(Long.parseLong(req.getParameter("file_idx")));

        FileUtils.serveDownloadFile(req, resp, fileDTO.getSavedFileName(), fileDTO.getOriginalFileName());
        return new View("download");
    }
}
