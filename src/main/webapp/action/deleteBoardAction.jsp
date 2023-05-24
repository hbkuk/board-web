<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<jsp:include page="../encodingFilter.jsp" flush="false"/>

<%

    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setBoardIdx(Long.parseLong(request.getParameter("board_idx")));
    boardDTO.setPassword(request.getParameter("password"));

    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());
    boardService.deleteBoardWithFilesAndComment(boardDTO);

    // 저장 후 이동
    String redirectUrl = String.format("/boardLists.jsp");
    response.sendRedirect(redirectUrl);

%>
