<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.dto.CategoryDTO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@ page import="com.study.utils.SearchConditionUtils" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String searchConditionQueryString = (String) request.getAttribute("searchConditionQueryString").toString();

    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardLists");

    List<CategoryDTO> categorys = (List<CategoryDTO>) request.getAttribute("categorys");

    //TODO: 메서드 분리 및 리팩토링
    int paramCategoryIdx = 0;
    if (request.getParameter("category_idx") != null) {
        paramCategoryIdx = Integer.parseInt(request.getParameter("category_idx"));
    }

    String paramKeyword = "";
    if (request.getParameter("keyword") != null) {
        paramKeyword = request.getParameter("keyword");
    }


    LocalDate defaultStartDate = LocalDate.now().minusYears(1);
    LocalDate defaultEndDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String startDate = defaultStartDate.format(formatter);
    if (request.getParameter("start_date") != null) {
        startDate = request.getParameter("start_date");
    }

    String endDate = defaultEndDate.format(formatter);
    if (request.getParameter("end_date") != null) {
        endDate = request.getParameter("end_date");
    }

%>
<jsp:include page="/include/header.jsp" flush="false">
    <jsp:param name="css_path" value="board.css"/>
    <jsp:param name="js_path" value="board_lists.js"/>
</jsp:include>
<body>
<div class="con_title">
    <h1>자유 게시판 - 목록</h1>
</div>
<div class="con_txt" style="margin-top: 50px;">
    <div class="contents_sub">
        <form id="search" action="boardLists.jsp" method='get'>
            <table style="border: 1px solid #ccc; padding: 10px;">
                <tr style="text-align: center;">
                    <c:set var = "paramStartDate" value="<%=startDate%>"/>
                    <c:set var = "paramEndDate" value="<%=endDate%>"/>
                    <td width="30%">등록일 | <input class="currentDate" type="date" name="start_date" value="${paramStartDate}" placeholder="시작 날짜"> ~ <input class="currentDate" type="date" name="end_date" value="${paramEndDate}" placeholder="끝 날짜"></td>
                    <td><select id="category">
                        <option value="all">전체 카테고리</option>
                        <c:set var = "paramCategoryIdx" value="<%=paramCategoryIdx%>"/>
                        <c:set var = "categorys" value="<%=categorys%>"/>
                        <c:forEach items="${categorys}" var="category">
                            <c:choose>
                                <c:when test="${paramCategoryIdx == category.categoryIdx}">
                                    <option value="${category.categoryIdx}" selected>${category.category}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${category.categoryIdx}">${category.category}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select> |
                    <c:set var = "paramKeyword" value="<%=paramKeyword%>"/>
                    <input type="text" name="keyword" value="${paramKeyword}" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)" style="width: 500px;"> |
                    <button id="submitButton">Search</button>
                </tr>
            </table>
        </form>
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
                    <th width="5%">&nbsp;</th>
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
                        <td width="5%"> OK </td>
                    </c:when>
                    <c:otherwise>
                        <td width="5%"> NOTTING </td>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${fn:length(board.title) > 80}">
                        <c:set var="truncatedTitle" value="${fn:substring(board.title, 0, 77)}..." />
                    </c:when>
                    <c:otherwise>
                        <c:set var="truncatedTitle" value="${board.title}" />
                    </c:otherwise>
                </c:choose>
                    <td><a href="boardView.jsp?board_idx=${board.boardIdx}<%=searchConditionQueryString.isEmpty() ? "" : "&" + searchConditionQueryString%>">${truncatedTitle}</a></td>
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
                <input type="button" value="write" class="btn_write btn_txt01" style="cursor: pointer;" onclick="location.href='boardWriteView.jsp<%=searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString%>'" />

            </div>
        </div>
    </div>
</div>
</body>
</html>
