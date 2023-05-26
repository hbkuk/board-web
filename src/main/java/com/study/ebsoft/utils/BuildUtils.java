package com.study.ebsoft.utils;

import com.oreilly.servlet.MultipartRequest;
import com.study.ebsoft.model.board.*;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.model.file.FileOriginalName;
import com.study.ebsoft.model.file.FileSize;
import com.study.model.board.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BuildUtils {

    public static List<File> buildFilesFromRequest(MultipartRequest multi) {
        List<File> files = new ArrayList<>();
        Enumeration fileNames = multi.getFileNames();

        while (fileNames.hasMoreElements()) {
            String fileName = (String) fileNames.nextElement();

            String originalFileName = multi.getOriginalFileName(fileName);
            String fileSystemName = multi.getFilesystemName(fileName);

            if (originalFileName != null && fileSystemName != null) {
                java.io.File file = new java.io.File(
                        "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download", fileSystemName);
                files.add(buildFileFromRequest(originalFileName, fileSystemName, (int) file.length()));
            }
        }
        return files;
    }


    public static File buildFileFromRequest(String saveFileName, String originalFileName, long fileSize) {
        return new File(saveFileName, new FileOriginalName(originalFileName), new FileSize((int) fileSize));
    }

    public static Board buildModifyBoardFromRequest(MultipartRequest multi) {
        return new Board.Builder()
                .boardIdx(new BoardIdx(Long.parseLong(multi.getParameter("board_idx"))))
                .category(Integer.parseInt(multi.getParameter("category")))
                .title(new Title(multi.getParameter("title")))
                .writer(new BoardWriter(multi.getParameter("writer")))
                .content(new BoardContent(multi.getParameter("content")))
                .password(new Password(multi.getParameter("password")))
                .build();
    }
    public static Board buildWriteBoardFromRequest(MultipartRequest multi) {
        return new Board.Builder()
                .category(Integer.parseInt(multi.getParameter("category")))
                .title(new Title(multi.getParameter("title")))
                .writer(new BoardWriter(multi.getParameter("writer")))
                .content(new BoardContent(multi.getParameter("content")))
                .password(new Password(multi.getParameter("password")))
                .build();
    }
}
