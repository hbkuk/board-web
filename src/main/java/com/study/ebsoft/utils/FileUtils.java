package com.study.ebsoft.utils;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 게시글 작성 또는 수정시
 *      서버 디렉토리에 파일을 저장 및 삭제하는 유틸 클래스
 */
@Slf4j
public class FileUtils {

    public static final String UPLOAD_PATH = "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download";
    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024;

    /**
     * HttpServletRequest 를 인자로 받아 MultipartRequest 생성하고 리턴합니다.
     *
     * @param request HttpServletRequest 객체
     * @return MultipartRequest 객체
     */
    public static MultipartRequest fileUpload(HttpServletRequest request) {

        try {
            MultipartRequest multi = new MultipartRequest(
                    request,
                    UPLOAD_PATH,
                    MAX_FILE_SIZE,
                    "utf-8",
                    new DefaultFileRenamePolicy());

            return multi;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 파일 이름에 해당하는 파일을 삭제했다면 true, 그렇지 않다면 false 를 반환합니다.
     *
     * @param fileName 삭제할 파일이름
     * @return 파일을 삭제했다면 true, 그렇지 않다면 false
     */
    public static boolean deleteUploadedFile(String fileName) {
        log.debug("삭제할 File : {}{}{} ", UPLOAD_PATH,"\\",fileName);
        File file = new File(UPLOAD_PATH, fileName);
        return file.delete();
    }

    /**
     * 디렉토리 내에서 해당 파일의 크기를 반환합니다.
     *
     * @param fileName 파일의 이름
     * @return 파일의 크기를 반환
     */
    public static long getFileSize(String fileName) {
        java.io.File file = new java.io.File(
                FileUtils.UPLOAD_PATH, fileName);
        return file.length();
    }
}
