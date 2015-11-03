<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${currentLocale}" scope="session"/>
<fmt:setBundle basename="messages" scope="session" var="rb"/>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title>
        <fmt:message bundle="${rb}" key="welcome_title"/>
    </title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSS платформы Bootstrap 3 -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap-theme.min.css"/>"/>

    <!-- CSS шрифты" -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/font-awesome.min.css"/>"/>

    <!-- CSS локальный" -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/ustyle.css"/>"/>

    <link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon">
</head>

<body class="aga-body" style="margin-top: 45px; padding-top: 0px; padding-right: 0px;">

<div class="aga-page" style="margin-top: 30px;">
    <%--header include--%>
    <%@include file="/WEB-INF/pages/jspf/headerPage.jspf" %>

    <section id="errorInfo" style="margin-bottom: 5px; padding: 5px;">
        <h1><fmt:message bundle="${rb}" key="labelError"/>: ${pageContext.errorData.statusCode} <fmt:message bundle="${rb}" key="servletError.${pageContext.errorData.statusCode}"/></h1>
        <a href="javascript: history.back()"><fmt:message bundle="${rb}" key="labelBack"/></a>
        <h3>${pageContext.errorData.requestURI}</h3>
        <h3>${pageContext.exception}</h3>
        <c:forEach var="trace" items="${pageContext.exception.stackTrace}">
            ${trace}<br/>
        </c:forEach>
    </section>

    <%--footer include--%>
    <%@include file="/WEB-INF/pages/jspf/footerPage.jspf" %>

</div>

</body>

<!-- java Script" -->
<script type="text/javascript" src="<c:url value="/js/jquery-2.1.4.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>

</html>
