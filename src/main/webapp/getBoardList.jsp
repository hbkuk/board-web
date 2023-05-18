<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Get the instance of BoardService
    BoardService boardService = BoardService.getInstance();

    // Call the getBoardListDetails method
    List<BoardDTO> boardList = boardService.getBoardListDetails();
%>
<html>
<head>
    <title>Board List</title>
</head>
<body>
<h1>Board List</h1>
<ul>
    <c:forEach items="<%= boardList %>" var="board">
        <li>Board ID: ${board.boardIdx}</li>
        <li>Title: ${board.title}</li>
        <li>Author: ${board.writer}</li>
        <li>...</li>
        <br>
    </c:forEach>
</ul>
</body>
</html>
