<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.model.file.File" %>
<%@ page import="com.study.model.board.*" %>
<%@ page import="com.study.utils.FileUploadUtils" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.study.model.file.FileOriginalName" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    // 파일 업로드
    MultipartRequest multi = FileUploadUtils.fileUpload((HttpServletRequest) request);

    // DB 저장
    BoardService boardService = BoardService.getInstance();
    BoardDTO boardDTO = boardService.saveBoardWithImages(buildBoardFromRequest(multi), buildFilesFromRequest(multi));

    // 저장 후 이동
    String redirectUrl = String.format("./boardView.jsp?board_idx=%d", boardDTO.getBoardIdx());
    request.getRequestDispatcher(redirectUrl).forward(request, response);
%>


<%!
    private List<File> buildFilesFromRequest(MultipartRequest multi) {
        List<File> files = new ArrayList<>();
        Enumeration fileNames = multi.getFileNames();

        while (fileNames.hasMoreElements()) {
            String file = (String) fileNames.nextElement();

            files.add(buildFileFromRequest(multi.getOriginalFileName(file), multi.getFilesystemName(file)));
        }
        return files;
    }

    private File buildFileFromRequest(String saveFileName, String originalFileName) {
        return new File(saveFileName, new FileOriginalName(originalFileName));
    }

    private Board buildBoardFromRequest(MultipartRequest multi) {
        return new Board(
                Category.valueOf(multi.getParameter("category")),
                new Title(multi.getParameter("title")),
                new BoardWriter(multi.getParameter("writer")),
                new BoardContent(multi.getParameter("content")),
                new Password(multi.getParameter("password")) );
    }
%>