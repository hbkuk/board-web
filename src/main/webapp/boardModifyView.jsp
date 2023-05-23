<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ page import="com.study.repository.board.BoardDAO" %>
<%@ page import="com.study.repository.comment.CommentDAO" %>
<%@ page import="com.study.repository.file.FileDAO" %>
<%@ page import="com.study.repository.category.CategoryDAO" %>
<%@page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    long boardIdx = Long.parseLong(request.getParameter("board_idx"));

    BoardService boardService = new BoardService(new BoardDAO(), new CommentDAO(), new FileDAO(), new CategoryDAO());

    BoardDTO board = boardService.getBoardWithImages(boardIdx);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/board_write.css"/>">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">
    window.onload = function () {
            document.getElementById("wbtn").onclick = function () {
                if (document.wfrm.writer.value.trim() === '') {
                    alert("글쓴이를 입력하셔야 합니다.");
                    return false;
                }

                if (document.wfrm.title.value.trim() === '') {
                    alert("글제목을 입력하셔야 합니다.");
                    return false;
                }

                if (document.wfrm.password.value.trim() === '') {
                    alert("비밀번호를 입력하셔야 합니다.");
                    return false;
                }
                document.wfrm.submit();
            };

        $('.delete-button').on('click', function() {
            $(this).parent('.upload_file').remove();
        });
    }
    </script>
</head>

<body>
<!-- 상단 디자인 -->
<div class="contents1">
    <h1>게시판 - 등록</h1>
    <form action="<c:url value="/action/modifyBoardAction.jsp"/>" method="post" name="wfrm" enctype="multipart/form-data">
        <input type="hidden" name="board_idx" value="<%=board.getBoardIdx()%>" />
        <input type="hidden" name="category" value="<%=board.getCategoryIdx()%>" />
        <div class="contents_sub" style="margin-top: 50px;">
            <!--게시판-->
            <div class="board_write">
                <h3>기본 정보</h3>
                <table>
                    <tr>
                        <th class="top">카테고리</th>
                        <td class="top" colspan="3"><%=board.getCategory()%></td>
                    </tr>
                    <tr>
                        <th class="top">등록 일시</th>
                        <td class="top" colspan="3"><%=board.getRegDate()%></td>
                    </tr>
                    <tr>
                        <th class="top">수정 일시</th>
                        <td class="top" colspan="3"><%=board.getModDate()%></td>
                    </tr>
                    <tr>
                        <th class="top">조회수</th>
                        <td class="top" colspan="3"><%=board.getHit()%></td>
                    </tr>
                    <tr>
                        <th class="top">작성자</th>
                        <td class="top" colspan="3"><input type="text" name="writer" value="<%=board.getWriter()%>"
                                                           class="board_view_input_mail" maxlength="4" style="width: 500px"/></td>
                    </tr>
                    <tr>
                        <th>비밀번호</th>
                        <td colspan="3">
                            <input type="password" name="password" value="" class="board_view_input_mail" placeholder="비밀번호" style="width: 500px"/>
                        </td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="3"><input type="text" name="title" value="<%=board.getTitle() %>" class="board_view_input" style="width: 500px"/></td>
                    </tr>
                    <tr>
                        <th>내용</th>
                        <td colspan="3">
                            <textarea name="content" class="board_editor_area"><%=board.getTitle()%></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>이미지</th>
                        <td colspan="3">
                            <c:forEach items="<%=board.getFiles()%>" var="file">
                                <div class="upload_file">
                                    <input type="hidden" name="file_idx" value="${file.fileIdx}" class="board_view_input" />
                                    <a href="download.jsp?file_idx=${file.fileIdx}">${file.originalFileName}</a>
                                    <input type="button" class="board_view_delete delete-button" value="delete"/><br/><br/>
                                </div>
                            </c:forEach>
                            <c:forEach begin="<%=board.getFiles().size() + 1%>" end="3" var="index">
                                <input type="file" multiple="multiple" name="upload" class="board_view_input" /><br/><br/>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="btn_area">
                <div class="align_left">
                    <input type="button" value="취소" class="btn_list btn_txt02" style="cursor: pointer;"
                           onclick="location.href='boardLists.jsp'">
                </div>
                <div class="align_right">
                    <input id="wbtn" type="button" value="저장" class="btn_write btn_txt01" style="cursor: pointer;"/>
                </div>
            </div>
            <!--//게시판-->
        </div>
    </form>
</div>
<!-- 하단 디자인 -->

</body>
</html>
