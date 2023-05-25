<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.model.file.File" %>
<%@ page import="com.study.model.board.*" %>
<%@ page import="com.study.utils.FileUtils" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.study.model.file.FileOriginalName" %>
<%@ page import="com.study.model.file.FileSize" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@ page import="com.study.utils.SearchConditionUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String searchConditionQueryString = QueryUtils.buildQueryString(request.getParameterMap()).toString();

    // 파일 업로드
    MultipartRequest multi = FileUtils.fileUpload((HttpServletRequest) request);

    // DB 저장
    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());
    BoardDTO board = boardService.saveBoardWithImages(buildBoardFromRequest(multi), buildFilesFromRequest(multi));

    // 저장 후 이동
    if (searchConditionQueryString.isEmpty()) {
        response.sendRedirect(String.format("/boardView.jsp?board_idx=%d", board.getBoardIdx()));
    } else {
        response.sendRedirect(String.format("/boardView.jsp?board_idx=%d&%s", board.getBoardIdx(), searchConditionQueryString));
    }
%>


<%!
    private SearchConditionUtils QueryUtils;

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

    private File buildFileFromRequest(String saveFileName, String originalFileName, int fileSize) {
        return new File(saveFileName, new FileOriginalName(originalFileName), new FileSize(fileSize));
    }

    private Board buildBoardFromRequest(MultipartRequest multi) {
        return new Board(
                (Integer.parseInt(multi.getParameter("category"))),
                new Title(multi.getParameter("title")),
                new BoardWriter(multi.getParameter("writer")),
                new BoardContent(multi.getParameter("content")),
                new Password(multi.getParameter("password")) );
    }
%>