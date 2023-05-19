<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.model.comment.Comment" %>
<%@ page import="com.study.model.comment.CommentWriter" %>
<%@ page import="com.study.model.board.Password" %>
<%@ page import="com.study.model.comment.CommentContent" %>
<%@ page import="com.study.model.board.BoardIdx" %>
<%@ page import="com.study.dto.CommentDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    Comment comment = new Comment.Builder()
            .writer(new CommentWriter(request.getParameter("comment_writer")))
            .password(new Password(request.getParameter("comment_password")))
            .content(new CommentContent(request.getParameter("commnet_content")))
            .boardIdx(new BoardIdx(Long.parseLong(request.getParameter("board_idx"))))
            .build();

    BoardService boardService = BoardService.getInstance();

    CommentDTO commentDTO = boardService.saveComment(comment);

    long boardIdx = commentDTO.getBoardIdx();


    // 해당 글로 이동
    String redirectUrl = String.format("./boardView.jsp?board_idx=%d", boardIdx);
    request.getRequestDispatcher(redirectUrl).forward(request, response);
%>