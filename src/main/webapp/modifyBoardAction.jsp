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
<%@ page import="java.util.Arrays" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");

    // 파일 업로드
    MultipartRequest multi = FileUploadUtils.fileUpload((HttpServletRequest) request);

    BoardService boardService = BoardService.getInstance();

    // 수정사항 저장
    Board updateBoard = new Board(
            Category.valueOf(multi.getParameter("category")),
            new Title(multi.getParameter("title")),
            new BoardWriter(multi.getParameter("writer")),
            new BoardContent(multi.getParameter("content")),
            new Password(multi.getParameter("password")) );


    List<Integer> oldFiles = new ArrayList<>();

    // toInteger
    for (String fileIdx : request.getParameterValues("file_idx")) {
        oldFiles.add(toInteger(fileIdx));
    }

    // DB에 저장
    BoardDTO boardDTO = boardService.updateBoardWithImages(
                buildBoardFromRequest(multi), buildFilesFromRequest(multi), oldFiles);

    // 저장 후 이동
    request.getRequestDispatcher("./boardLists.jsp").forward(request, response);
%>

<%!
    private int toInteger(String fileIdx) {
        return Integer.parseInt(fileIdx);
    }

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