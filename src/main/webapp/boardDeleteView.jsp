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
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    long boardIdx = Long.parseLong(request.getParameter("board_idx"));

    // Get the instance of BoardService
    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());

    // Call the getBoardListDetails method
    BoardDTO boardDTO = boardService.getBoardWithDetails(boardIdx);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/board_write.css"/>">

    <script type="text/javascript">
        window.onload = function() {
            document.getElementById( 'dbtn' ).onclick = function() {
                if( document.dfrm.password.value.trim() == '' ) {
                    alert( '비밀번호를 입력하셔야 합니다.' );
                    return false;
                }
                document.dfrm.submit();
            };
        };
    </script>

</head>

<body>
<!-- 상단 디자인 -->
<div class="contents1">
    <form action="action/deleteBoardAction.jsp" method="post" name="dfrm">
        <input type = "hidden" name = "board_idx" value="<%=boardDTO.getBoardIdx()%>" >
        <div class="contents_sub">
            <!--게시판-->
            <div class="board_write">
                <table>
                    <tr>
                        <th class="top">글쓴이</th>
                        <td class="top" colspan="3"><input type="text" name="writer" value="<%=boardDTO.getWriter() %>" class="board_view_input_mail" maxlength="5" /></td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="3"><input type="text" name="title" value="<%=boardDTO.getTitle() %>" class="board_view_input" /></td>
                    </tr>
                    <tr>
                        <th>비밀번호</th>
                        <td colspan="3"><input type="password" name="password" value="" class="board_view_input_mail"/></td>
                    </tr>
                </table>
            </div>
            <div class="btn_area">
                <div class="align_left">
                    <input type="button" value="목록" class="btn_list btn_txt02" style="cursor: pointer;"
                           onclick="location.href='boardLists.jsp'"/>
                </div>
                <div class="align_right">
                    <input id="dbtn" type="button" value="삭제" class="btn_write btn_txt01" style="cursor: pointer;" />
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
