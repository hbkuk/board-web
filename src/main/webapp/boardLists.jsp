<%@page isELIgnored="false" %>
<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.dto.CategoryDTO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    // Get the instance of BoardService
    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());

    // Call the getBoardListDetails method
    List<BoardDTO> boardList = boardService.getBoardListDetails();

    List<CategoryDTO> categorys = boardService.getAllCategory();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/board.css"/>">
</head>

<body>
<!-- 상단 디자인 -->
<div class="con_title">
    <h1>자유 게시판 - 목록</h1>
</div>
<div class="con_txt" style="margin-top: 50px;">
    <div class="contents_sub">
        <table style="border: 1px solid #ccc; padding: 10px;">
            <tr style="text-align: center;">
                <td width="30%">등록일 | <input type="date" id="start_date" placeholder="시작 날짜"> ~ <input type="date" id="end_date" placeholder="끝 날짜"></td>
                <td><select id="category">
                    <option value="전체">전체 카테고리</option>
                    <c:forEach items="<%=categorys%>" var="category">
                        <option value="${category.categoryIdx}">${category.category}</option>
                    </c:forEach>
                </select> |
                <input type="text" id="search_query" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)" style="width: 500px;"> | <button>검색</button></td>
            </tr>
        </table>
    </div>
</div>
<div class="con_txt">
    <div class="contents_sub">
        <div class="board_top">
            <div class="bold">총 <span class="txt_orange"><%=boardList.size()%></span>건</div>
        </div>
        <!--게시판-->
        <div class="board">
            <table>
                <tr>
                    <th width="3%">&nbsp;</th>
                    <th width="10%">카테고리</th>
                    <th width="3%">&nbsp;</th>
                    <th width="3%">&nbsp;</th>
                    <th>제목</th>
                    <th width="10%">작성자</th>
                    <th width="5%">조회수</th>
                    <th width="12%">등록 일시</th>
                    <th width="12%">수정 일시</th>
                    <th width="3%">&nbsp;</th>
                </tr>
            <c:forEach items="<%=boardList%>" var="board">
                <tr>
                    <td width="3%">&nbsp;</td>
                    <td width="10%">${board.category}</td>
                    <td width="3%"> &nbsp; </td>
                <c:choose>
                    <c:when test="${board.hasFile eq true}">
                        <td width="3%"> OK </td>
                    </c:when>
                    <c:otherwise>
                        <td width="3%"> nothing </td>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${fn:length(board.title) > 80}">
                        <c:set var="truncatedTitle" value="${fn:substring(board.title, 0, 80)}" />
                        <c:set var="remainingWords" value="${fn:substring(board.title, 80, fn:length(board.title))}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="truncatedTitle" value="${board.title}" />
                    </c:otherwise>
                </c:choose>
                    <td><a href=boardView.jsp?board_idx=${board.boardIdx}>${truncatedTitle}</a></td>
                    <td width="10%">${board.writer}</td>
                    <td width="5%">${board.hit}</td>
                    <td width="12%">
                        <fmt:parseDate value="${board.regDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${ parsedDateTime }" />
                    </td>
                    <td width="12%">
                        <fmt:parseDate value="${board.modDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${ parsedDateTime }" />
                    </td>
                    <td width="3%">&nbsp;</td>
                </tr>
            </c:forEach>
            </table>
        </div>

        <div class="btn_area">
            <div class="align_right">
                <input type="button" value="쓰기" class="btn_write btn_txt01" style="cursor: pointer;" onclick="location.href='boardWriteView.jsp'" />
            </div>
        </div>
    </div>
</div>
</body>
</html>
