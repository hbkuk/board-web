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
<%@ page import="java.util.logging.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.study.model.file.FileSize" %>
<%@ page import="java.util.Arrays" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");

    // 파일 업로드
    MultipartRequest multi = FileUploadUtils.fileUpload((HttpServletRequest) request);

    System.out.println(Long.parseLong(multi.getParameter("board_idx")));


    Board board = buildBoardFromRequest(multi);

    System.out.println(board.toString());


    List<File> newFiles = buildFilesFromRequest(multi);

    System.out.println(String.valueOf(newFiles.size()));


    List<Long> oldFiles = new ArrayList<>();
    if(multi.getParameter("file_idx") != null) {
        for (String item : multi.getParameterValues("file_idx")) {
            oldFiles.add(Long.parseLong(item));
        }
        System.out.println(String.valueOf(oldFiles.size()));
    }

    BoardService boardService = BoardService.getInstance();
    BoardDTO boardDTO = boardService.updateBoardWithImages(board, newFiles, oldFiles);

    // 저장 후 이동
    String redirectUrl = String.format("/boardView.jsp?board_idx=%d", boardDTO.getBoardIdx());
    response.sendRedirect(redirectUrl);

%>


<%!
    private List<File> buildFilesFromRequest(MultipartRequest multi) {
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


    private File buildFileFromRequest(String saveFileName, String originalFileName, long fileSize) {
        return new File(saveFileName, new FileOriginalName(originalFileName), new FileSize((int) fileSize));
    }

    private Board buildBoardFromRequest(MultipartRequest multi) {
        return new Board.Builder()
                .boardIdx(new BoardIdx(Long.parseLong(multi.getParameter("board_idx"))))
                .category(Category.valueOf(multi.getParameter("category")))
                .title(new Title(multi.getParameter("title")))
                .writer(new BoardWriter(multi.getParameter("writer")))
                .content(new BoardContent(multi.getParameter("content")))
                .password(new Password(multi.getParameter("password")))
                .build();
    }
%>