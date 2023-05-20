package com.study.utils;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FileUploadUtils {

    private static final String UPLOAD_PATH = "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download";

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
}
