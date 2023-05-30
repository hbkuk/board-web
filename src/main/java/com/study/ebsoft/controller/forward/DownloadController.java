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
import java.util.NoSuchElementException;

/**
 * 파일 번호에 해당하는 파일을 바이너리 다운로드 형태로 제공
 */
@Slf4j
public class DownloadController implements Controller {

    /**
     * 파일 번호에 해당하는 파일을 응답합니다
     */
    public View process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileDTO fileDTO = FileDAO.getInstance().findFileNameById(Long.parseLong(req.getParameter("file_idx")));
        if( fileDTO == null ) {
            throw new NoSuchElementException("파일을 찾지 못했습니다.");
        }

        FileUtils.serveDownloadFile(req, resp, fileDTO.getSavedFileName(), fileDTO.getOriginalFileName());
        return new View("download");
    }
}
