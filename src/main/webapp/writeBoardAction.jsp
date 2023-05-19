<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.model.file.file" %>
<%@ page import="com.study.model.board.*" %>
<%@ page import="com.study.utils.FileUploadUtils" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.io.File" %>
<%@ page import="com.study.model.file.originalName" %>
<%@ page import="com.study.model.file.fileSize" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    MultipartRequest multi =
            FileUploadUtils.fileUpload((HttpServletRequest) request);

    BoardService boardService = BoardService.getInstance();
    Board board = new Board.Builder()
            .category(Category.valueOf(multi.getParameter("category")))
            .writer(new BoardWriter(multi.getParameter("writer")))
            .title(new Title(multi.getParameter("title")))
            .password(new Password(multi.getParameter("password")))
            .content(new BoardContent(multi.getParameter("content")))
            .build();

    List<file> buildFiles = new ArrayList<>();

    Enumeration files = multi.getFileNames();

    while (files.hasMoreElements()) {
        String file = (String) files.nextElement();

        String originalFileName = multi.getOriginalFileName(file);
        String saveFileName = multi.getFilesystemName(file);

        file buildFile = new file.Builder()
                .saveFileName(saveFileName)
                .originalName(new originalName(originalFileName))
                .build();

        buildFiles.add(buildFile);
    }

    BoardDTO boardDTO = boardService.saveBoardWithImages(board, buildFiles);

    System.out.println(boardDTO.toString());
    System.out.println(buildFiles.size());

    // 해당 글로 이동
     String redirectUrl = String.format("./boardView.jsp?board_idx=%d", boardDTO.getBoardIdx());
     request.getRequestDispatcher(redirectUrl).forward(request, response);
%>
