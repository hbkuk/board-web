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

    Enumeration files = multi.getFileNames();
    List<file> buildFiles = new ArrayList<>();

    while(files.hasMoreElements()){
        String file = (String) files.nextElement();
        try{
            File fileObj = new File(multi.getOriginalFileName(file));

            file buildFile = new file.Builder()
                    .saveFileName(multi.getFilesystemName(file))
                    .originalName(new originalName(multi.getOriginalFileName(file)))
                    .fileSize(new fileSize((int) fileObj.length()))
                    .build();

            buildFiles.add(buildFile);
        } catch(NullPointerException e){
            // 예외 처리..
        }
    }

    BoardDTO boardDTO = boardService.saveBoardWithImages(board, buildFiles);

    // 해당 글로 이동
    String redirectUrl = String.format("./boardView.jsp?board_idx=%d", boardDTO.getBoardIdx());
    request.getRequestDispatcher(redirectUrl).forward(request, response);

%>