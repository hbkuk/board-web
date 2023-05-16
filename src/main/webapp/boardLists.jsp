<%@ page import="com.study.repository.BoardDAO" %>
<%@ page import="com.study.model.board.Board" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    BoardDAO boardDAO = new BoardDAO();
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
<div class="con_txt">
    <div class="contents_sub">
        <table style="border: 1px solid #ccc; padding: 10px;">
            <tr style="text-align: center;">
                <td>등록일<input type="date" id="start_date" placeholder="시작 날짜"></td>
                <td><input type="date" id="end_date" placeholder="끝 날짜"></td>
                <td><select id="category">
                    <option value="전체">전체 카테고리</option>
                    <option value="카테고리1">카테고리1</option>
                    <option value="카테고리2">카테고리2</option>
                    <option value="카테고리3">카테고리3</option>
                </select></td>
                <td><input type="text" id="search_query" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)" style="width: 200px;"></td>
                <td><button>검색</button></td>
            </tr>
        </table>
    </div>
</div>
<div class="con_txt">
    <div class="contents_sub">
        <div class="board_top">
            <div class="bold">총 <span class="txt_orange">100</span>건</div>
        </div>

        <!--게시판-->
        <div class="board">
            <table>
                <tr>
                    <th width="3%">&nbsp;</th>
                    <th width="5%">카테고리</th>
                    <th>제목</th>
                    <th width="10%">작성자</th>
                    <th width="5%">조회수</th>
                    <th width="12%">등록 일시</th>
                    <th width="12%">수정 일시</th>
                    <th width="3%">&nbsp;</th>
                </tr>
            </table>
        </div>

        <div class="btn_area">
            <div class="align_right">
                <input type="button" value="쓰기" class="btn_write btn_txt01" style="cursor: pointer;" onclick="location.href='board_write1.jsp'" />
            </div>
        </div>
    </div>
</div>
</body>
</html>
