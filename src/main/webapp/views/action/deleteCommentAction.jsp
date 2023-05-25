<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@ page import="com.study.dto.CommentDTO" %>
<%@ page import="com.study.utils.SearchConditionUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String searchConditionQueryString = SearchConditionUtils.buildQueryString(request.getParameterMap()).toString();

    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setCommentIdx(Long.parseLong(request.getParameter("comment_idx")));
    commentDTO.setBoardIdx(Long.parseLong(request.getParameter("board_idx")));
    commentDTO.setPassword(request.getParameter("password"));

    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());
    long boardIdx = boardService.deleteCommentByCommentIdx(commentDTO);

    // 저장 후 이동
    if (searchConditionQueryString.isEmpty()) {
        response.sendRedirect(String.format("/boardView.jsp?board_idx=%d", boardIdx));
    } else {
        response.sendRedirect(String.format("/boardView.jsp?board_idx=%d&%s", boardIdx, searchConditionQueryString));
    }
%>
