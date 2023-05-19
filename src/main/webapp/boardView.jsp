<%@page isELIgnored="false" %>
<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.dto.BoardDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    long boardIdx = Long.parseLong(request.getParameter("board_idx"));

    BoardService boardService = BoardService.getInstance();

    BoardDTO board = boardService.getBoardWithDetails(boardIdx);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/board_view.css"/>">

    <script type="text/javascript">

        window.onload = function () {
            document.getElementById("cbtn").onclick = function () {

                if (document.cfrm.comment_writer.value.trim() == '') {
                    alert("글쓴이를 입력하셔야 합니다.");
                    return false;
                }

                if (document.cfrm.comment_password.value.trim() == '') {
                    alert("비밀번호를 입력하셔야 합니다.");
                    return false;
                }

                if (document.cfrm.commnet_content.value.trim() == '') {
                    alert("내용을 입력하셔야 합니다.");
                    return false;
                }

                document.cfrm.submit();

            };

            document.getElementById("dcbtn").onclick = function () {

                if (document.dcfrm.c_password[1].value.trim() == '') {
                    alert("비밀번호를 입력하셔야 합니다.");
                    return false;
                }
                document.dcfrm.submit();

            };

        }

        const dcbtn = function (c_seq) {

            document.getElementById("dcfrm" + c_seq + "").submit();
        };
    </script>
</head>

<body>
<div class="con_title">
    <h2>자유 게시판 - 목록</h2>
</div>
<div class="contents1">
    <div class="con_title" style="margin-top: 40px;">
        <strong><%= board.getWriter() %></strong>
        <p style="margin: 0px; text-align: right">
            등록 일시: <strong><%= board.getRegDate() %></strong> ∣
            수정일시: <strong>
            <% if (board.getModDate() == null) { %>-<% } else { %><%= board.getModDate() %><% } %></strong>
        </p>
    </div>
    <div class="contents_sub">
        <!--게시판-->
        <div class="board_view">
            <table>
                <tr>
                    <td width="15%">[<%=board.getCategory() %>]</td>
                    <td><%=board.getTitle() %>
                    </td>
                    <td width="15%">조회수 : <%=board.getHit() %>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" height="200" valign="top" style="padding:20px; line-height:160%">
                        <p><%=board.getContent() %>
                        </p>
                        <hr>
                        <div id="bbs_file_wrap">
                            <div>
                                <c:forEach items="<%=board.getFiles()%>" var="file">
                                    <p>
                                        <a href="download.jsp?file_idx=${file.fileIdx}">${file.originalFileName}</a>
                                    </p>
                                </c:forEach>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <table>
                <c:forEach items="<%=board.getComments()%>" var="comment">
                    <form action='/deleteComment.jsp' method='post' id='dcfrm" + c_seq +"'>
                        <input type='hidden' name='pseq' value=" + seq + ">
                        <input type='hidden' name='c_seq' value=" + c_seq + ">
                        <input type='hidden' name='cpage' value=" + cpage + ">
                        <tr>
                            <td class='coment_re'>
                                <strong>${comment.writer}</strong> | ${comment.regDate}
                                <div class='coment_re_txt'>
                                        ${comment.content}
                                </div>
                            </td>
                            <td class='coment_re' width='20%' align='right'>
                                <input type='password' name='c_password' placeholder='비밀번호' class='coment_input pR10'/>
                                <input onclick='dcbtn(${comment.commentIdx})' type='button' value='삭제'
                                       class='btn_comment btn_txt02' style='cursor: pointer;'/>
                            </td>
                        </tr>
                    </form>
                </c:forEach>
            </table>
            <form action="./writeCommentAction.jsp" method="post" name="cfrm">
                <input type="hidden" name="board_idx" value="<%=board.getBoardIdx() %>">
                <table>
                    <tr>
                        <td width="94%" class="coment_re">
                            글쓴이 <input type="text" name="comment_writer" maxlength="5" class="coment_input"/>&nbsp;&nbsp;
                            비밀번호 <input type="password" name="comment_password" class="coment_input pR10"/>&nbsp;&nbsp;
                        </td>
                        <td width="6%" class="bg01"></td>
                    </tr>
                    <tr>
                        <td class="bg01">
                            <textarea name="commnet_content" cols="" rows="" class="coment_input_text"></textarea>
                        </td>
                        <td align="right" class="bg01">
                            <input id="cbtn" type="button" value="댓글등록" class="btn_comment btn_txt02" style="height: 60px; display: block;">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="btn_area">
            <div style="text-align: center; margin: 0 auto;">
                <input type="button" value="목록" class="btn_list btn_txt02" style="cursor: pointer;"
                       onclick="location.href='boardLists.jsp'"/>
                <input type="button" value="수정" class="btn_list btn_txt02" style="cursor: pointer;"
                       onclick="location.href='board_delete.do?seq=&cpage='"/>
                <input type="button" value="삭제" class="btn_write btn_txt01" style="cursor: pointer;"
                       onclick="location.href='board_write.do?cpage='"/>
            </div>
        </div>
        <!--//게시판-->
    </div>
    <!-- 하단 디자인 -->
</div>

</body>
</html>