<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
    long boardIdx = Long.parseLong(request.getParameter("board_idx"));

    // Get the instance of BoardService
    BoardService boardService = BoardService.getInstance();

    // Call the getBoardListDetails method
    BoardDTO board = boardService.getBoardWithDetails(boardIdx);
%>
<html>
<head>
    <title>Board</title>
</head>
<body>
<h1>Board Details</h1>
<ul>
</ul>
</body>
</html>