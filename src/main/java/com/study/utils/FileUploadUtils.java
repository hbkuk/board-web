package com.study.utils;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FileUploadUtils {

    public static MultipartRequest fileUpload(HttpServletRequest request) {

        String uploadPath = "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download";
        int maxFileSize = 2 * 1024 * 1024;
        String encoding = "utf-8";

        try {
            MultipartRequest multi = new MultipartRequest(
                    request,
                    uploadPath,
                    maxFileSize,
                    encoding,
                    new DefaultFileRenamePolicy());

            return multi;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
