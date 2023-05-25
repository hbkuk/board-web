<%@ page import="com.study.utils.SearchConditionUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String queryString = SearchConditionUtils.buildQueryString(request.getParameterMap()).toString();
    request.setAttribute("query_string", queryString);
%>
