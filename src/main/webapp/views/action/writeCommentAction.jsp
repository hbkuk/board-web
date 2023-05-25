<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.model.comment.Comment" %>
<%@ page import="com.study.model.comment.CommentWriter" %>
<%@ page import="com.study.model.board.Password" %>
<%@ page import="com.study.model.comment.CommentContent" %>
<%@ page import="com.study.model.board.BoardIdx" %>
<%@ page import="com.study.dto.CommentDTO" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@ page import="com.study.utils.SearchConditionUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String searchConditionQueryString = SearchConditionUtils.buildQueryString(request.getParameterMap()).toString();

    Comment comment = new Comment(
            new CommentWriter(request.getParameter("comment_writer")),
            new Password(request.getParameter("comment_password")),
            new CommentContent(request.getParameter("commnet_content")),
            new BoardIdx(Long.parseLong(request.getParameter("board_idx"))));

    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());

    CommentDTO commentDTO = boardService.saveComment(comment);


    // 저장 후 이동
    if (searchConditionQueryString.isEmpty()) {
        response.sendRedirect(String.format("/boardView.jsp?board_idx=%d", commentDTO.getBoardIdx()));
    } else {
        response.sendRedirect(String.format("/boardView.jsp?board_idx=%d&%s", commentDTO.getBoardIdx(), searchConditionQueryString));
    }
%>