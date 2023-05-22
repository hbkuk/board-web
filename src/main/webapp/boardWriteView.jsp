<%@page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.study.model.board.Category" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String cpage = request.getParameter("cpage");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/board_write.css"/>">
    <script type="text/javascript">
        window.onload = function () {
            document.getElementById("wbtn").onclick = function () {

                // if (document.wfrm.writer.value.trim() == '') {
                //     alert("글쓴이를 입력하셔야 합니다.");
                //     return false;
                // }
                //
                // if (document.wfrm.title.value.trim() == '') {
                //     alert("글제목을 입력하셔야 합니다.");
                //     return false;
                // }
                //
                // if (document.wfrm.password.value.trim() == '') {
                //     alert("비밀번호를 입력하셔야 합니다.");
                //     return false;
                // }

                document.wfrm.submit();
            };
        }
    </script>
</head>

<body>
<!-- 상단 디자인 -->
<div class="contents1">
    <h1>게시판 - 등록</h1>
    <form action="action/writeBoardAction.jsp" method="post" name="wfrm" enctype="multipart/form-data">
        <div class="contents_sub" style="margin-top: 50px;">
            <!--게시판-->
            <div class="board_write">
                <h3>기본 정보</h3>
                <table>
                    <tr>
                        <th class="top">카테고리</th>
                        <td class="top" colspan="3">
                            <select id="category" name="category">
                                <option value="전체">카테고리 선택</option>
                                <c:forEach items="${Category.values()}" var="category">
                                    <option value="${category.name()}">${category.name()}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th class="top">작성자</th>
                        <td class="top" colspan="3"><input type="text" name="writer" value=""
                                                           class="board_view_input_mail" maxlength="5"/></td>
                    </tr>
                    <tr>
                        <th>비밀번호</th>
                        <td colspan="3">
                            <input type="password" name="password" value="" class="board_view_input_mail" placeholder="비밀번호"/>
                            <input type="password" name="passwordConfirm" value="" class="board_view_input_mail" placeholder="비밀번호 확인"/>
                        </td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="3"><input type="text" name="title" value="" class="board_view_input"/></td>
                    </tr>
                    <tr>
                        <th>내용</th>
                        <td colspan="3">
                            <textarea name="content" class="board_editor_area"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>이미지</th>
                        <td colspan="3">
                            <input type="file" multiple="multiple" name="upload1" class="board_view_input"/><br/><br/>
                            <input type="file" multiple="multiple" name="upload2" class="board_view_input"/><br/><br/>
                            <input type="file" multiple="multiple" name="upload3" class="board_view_input"/><br/><br/>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="btn_area">
                <div class="align_left">
                    <input type="button" value="취소" class="btn_list btn_txt02" style="cursor: pointer;"
                           onclick="location.href='board_list1.jsp?cpage=<%=cpage %>'"/>
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
