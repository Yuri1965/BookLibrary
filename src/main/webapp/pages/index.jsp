<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="currentPage" value = "index"/>
<c:set var="currentLocale" value = "${pageContext.session.getAttribute('currentLocale')}"/>
<c:set var="currentUser" value = "${pageContext.session.getAttribute('sessionUser')}"/>

<c:set var="typeSearchBook" value = "${pageContext.session.getAttribute('typeSearchBook')}"/>
<c:set var="searchTextBook" value = "${pageContext.session.getAttribute('searchTextBook')}"/>
<c:set var="bookSectionID" value = "${pageContext.session.getAttribute('bookSectionID')}"/>
<c:set var="bookGenreID" value = "${pageContext.session.getAttribute('bookGenreID')}"/>
<c:set var="pageBookNumber" value = "${pageContext.session.getAttribute('pageBookNumber')}"/>

<c:set var="countBooks" value = "${pageContext.session.getAttribute('countBooks')}"/>
<c:set var="countBookPages" value = "${pageContext.session.getAttribute('countBookPages')}"/>
<c:set var="listBookPage" value = "${pageContext.session.getAttribute('listBookPage')}"/>
<c:set var="listBookSection" value = "${pageContext.session.getAttribute('listBookSection')}"/>
<c:set var="listBookGenre" value = "${pageContext.session.getAttribute('listBookGenre')}"/>

<fmt:setLocale value="${currentLocale}" scope="session"/>
<fmt:setBundle basename="messages" scope="session" var="rb"/>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <title>
        <fmt:message bundle="${rb}" key="welcome_title"/>
    </title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSS платформы Bootstrap 3 -->
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.min.css"/>
    <!-- CSS шрифты" -->
    <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css"/>

    <!-- CSS локальный" -->
    <link rel="stylesheet" type="text/css" href="css/ustyle.css"/>

    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
</head>

<body class="aga-body" style="margin-top: 45px; padding-top: 0px; padding-right: 0px;">

    <div class="aga-page" style="margin-top: 30px;">
        <%--header include--%>
        <%@include file="/pages/jspf/headerPage.jspf"%>

        <section id="steps" style="margin-bottom: 5px; padding: 5px; display: block;">
            <div class="center">
                <div class="row-height panel-primary" id="books">
                    <div class="row row-height">

                        <%--sectionBook include--%>
                        <%@include file="/pages/jspf/sectionBook.jspf"%>

                        <div class="col-md-9 row-height" style="position: relative;">
                            <div class="panel panel-default">
                                <%--searchParamBook include--%>
                                <%@include file="/pages/jspf/panelSearchParamBook.jspf"%>

                                <%--panelListBook include--%>
                                <%@include file="/pages/jspf/panelListBook.jspf"%>

                                <%--panelPagingFooterBook include--%>
                                <%@include file="/pages/jspf/panelPagingFooterBooks.jspf"%>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="clear">&nbsp;</div>
        </section>

        <%--footer include--%>
        <%@include file="/pages/jspf/footerPage.jspf"%>
    </div>

    <%--modal loginForm include--%>
    <%@include file="/pages/jspf/loginForm.jspf"%>

    <%--modal registrationForm include--%>
    <%@include file="/pages/jspf/registrationForm.jspf" %>

    <%--modal infoForm include--%>
    <%@include file="/pages/jspf/infoForm.jspf"%>

</body>

<!-- java Script" -->
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>

<c:if test="${autoShowModalForm.toString().length() > 0}">
    <script type="text/javascript">
        $(document).ready(function(){
            $("${autoShowModalForm}").modal('show');
        });
    </script>
</c:if>

</html>
