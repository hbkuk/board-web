package com.study.utils;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Slf4j
public class FileUtils {

    private static final String UPLOAD_PATH = "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download";

    /**
     * HttpServletRequest 를 인자로 받아 MultipartRequest 생성하고 리턴합니다.
     * @param request HttpServletRequest 객체
     * @return MultipartRequest 객체
     */
    public static MultipartRequest fileUpload(HttpServletRequest request) {

        try {
            MultipartRequest multi = new MultipartRequest(
                    request,
                    UPLOAD_PATH,
                    2 * 1024 * 1024,
                    "utf-8",
                    new DefaultFileRenamePolicy());

            return multi;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 파일 이름에 해당하는 디렉토리의 파일을 삭제합니다.
     * @param fileName 삭제할 파일이름
     * @return 디렉토리의 파일이 삭제되었으면 true, 그렇지 않다면 false
     */
    public static void deleteUploadedFile(String fileName) {
        log.debug("삭제할 File : {}{}{} ", UPLOAD_PATH,"\\",fileName);
        File file = new File(UPLOAD_PATH, fileName);
        file.delete();
    }

}
