<%@ page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--Общие переменные--%>
<c:set var="currentPage" value = "main" />
<c:set var="currentLocale" value = "${pageContext.session.getAttribute('currentLocale')}" />
<c:set var="currentUser" value = "${pageContext.session.getAttribute('sessionUser')}" />

<%--Активная табпанель--%>
<c:set var="currentTab" value = "${pageContext.session.getAttribute('currentTab')}" />

<%--Переменные для обработки запросов по книгам--%>
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

<%--Переменные для обработки запросов по заказам--%>
<c:set var="statusOrder" value = "${pageContext.session.getAttribute('statusOrder')}"/>
<c:set var="typeOrderID" value = "${pageContext.session.getAttribute('typeOrderID')}"/>
<c:set var="typeSearchOrder" value = "${pageContext.session.getAttribute('typeSearchOrder')}"/>
<c:set var="searchTextOrder" value = "${pageContext.session.getAttribute('searchTextOrder')}"/>
<c:set var="pageOrderNumber" value = "${pageContext.session.getAttribute('pageOrderNumber')}"/>
<c:set var="listTypeOrder" value = "${pageContext.session.getAttribute('listTypeOrder')}"/>
<c:set var="countOrders" value = "${pageContext.session.getAttribute('countOrders')}"/>
<c:set var="countOrderPages" value = "${pageContext.session.getAttribute('countOrderPages')}"/>

<%--Переменные для обработки запросов по пользователям--%>
<c:set var="statusUser" value = "${pageContext.session.getAttribute('statusUser')}"/>
<c:set var="typeSearchUser" value = "${pageContext.session.getAttribute('typeSearchUser')}"/>
<c:set var="searchTextUser" value = "${pageContext.session.getAttribute('searchTextUser')}"/>
<c:set var="pageUserNumber" value = "${pageContext.session.getAttribute('pageUserNumber')}"/>
<c:set var="listUserPage" value = "${pageContext.session.getAttribute('listUserPage')}"/>
<c:set var="countUsers" value = "${pageContext.session.getAttribute('countUsers')}"/>
<c:set var="countUserPages" value = "${pageContext.session.getAttribute('countUserPages')}"/>

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
    <!-- CSS виджета "Bootstrap datetimepicker" -->
    <link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"/>

    <link rel="stylesheet" type="text/css" href="css/fileinput.min.css" />

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
                <ul class="nav nav-tabs" data-tabs="tabs">
                    <li
                        <c:if test="${currentTab eq 'books'}">
                            class="active"
                        </c:if>
                        >
                        <a href="#books" data-toggle="tab"><fmt:message bundle="${rb}" key="mainPage.tabBook"/></a>
                    </li>

                    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('EMPLOYEE')}">
                        <li
                                <c:if test="${currentTab eq 'workOrders'}">
                                    class="active"
                                </c:if>
                                >
                            <a href="#workOrders" data-toggle="tab"><fmt:message bundle="${rb}" key="mainPage.tabOrder"/></a>
                        </li>
                        <li
                                <c:if test="${currentTab eq 'workUsers'}">
                                    class="active"
                                </c:if>
                                >
                            <a href="#workUsers" data-toggle="tab"><fmt:message bundle="${rb}" key="mainPage.tabUser"/></a>
                        </li>
                    </c:if>
                </ul>

                <div class="tab-content home-style" style="padding-top: 5px;">
                    <%--Tab с книгами--%>
                    <div class="row-height tab-pane fade
                                <c:if test="${currentTab eq 'books'}">
                                    in active
                                </c:if>" id="books">
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
                    <%--Tab с книгами--%>

                    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('EMPLOYEE')}">
                        <%--Tab Работа с заказами--%>
                        <div class="row-height tab-pane fade
                                <c:if test="${currentTab eq 'workOrders'}">
                                    in active
                                </c:if>" id="workOrders">
                            <div class="row row-height">
                                <div class="col-md-12 row-height" style="position:relative">
                                    <div class="panel panel-default">
                                            <%--searchParamOrder include--%>
                                        <%@include file="/pages/jspf/panelSearchParamOrder.jspf"%>

                                            <%--panelGridOrder include--%>
                                        <%@include file="/pages/jspf/panelGridOrder.jspf"%>

                                            <%--panelPagingFooterWorkOrders include--%>
                                        <%@include file="/pages/jspf/panelPagingFooterWorkOrders.jspf"%>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--Tab Работа с заказами--%>

                        <%--Tab Работа с пользователями--%>
                        <div class="row-height tab-pane fade
                                <c:if test="${currentTab eq 'workUsers'}">
                                    in active
                                </c:if>" id="workUsers">
                            <div class="row row-height">
                                <div class="col-md-12 row-height" style="position:relative">
                                    <div class="panel panel-default">
                                            <%--searchParamUser include--%>
                                        <%@include file="/pages/jspf/panelSearchParamUser.jspf"%>

                                            <%--panelGridUser include--%>
                                        <%@include file="/pages/jspf/panelGridUser.jspf"%>

                                            <%--panelPagingFooterWorkUsers include--%>
                                        <%@include file="/pages/jspf/panelPagingFooterWorkUsers.jspf"%>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--Tab Работа с пользователями--%>
                    </c:if>

                </div>

            </div>

            <div class="clear">&nbsp;</div>
        </section>

        <%--footer include--%>
        <%@include file="/pages/jspf/footerPage.jspf"%>

    </div>

    <%--modal userProfileForm include--%>
    <%@include file="/pages/jspf/userProfileForm.jspf"%>

    <%--modal createOrderForm include--%>
    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('READER')}">
        <%@include file="/pages/jspf/createOrderForm.jspf"%>
    </c:if>

    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('EMPLOYEE')}">
        <%--modal deleteBookForm include--%>
        <%@include file="/pages/jspf/deleteBookForm.jspf"%>
    </c:if>

    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('EMPLOYEE')}">
        <%--modal blockUnBlockUserForm include--%>
        <%@include file="/pages/jspf/blockUnBlockUserForm.jspf"%>
    </c:if>

    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('EMPLOYEE')}">
        <%--modal bookForm include--%>
        <%@include file="/pages/jspf/bookForm.jspf"%>
    </c:if>

    <c:if test="${currentPage eq 'main' and currentUser.isAuthorized() and currentUser.isFoundUserRoleByName('EMPLOYEE')}">
        <%--modal closeOrderForm include--%>
        <%@include file="/pages/jspf/closeOrderForm.jspf"%>
    </c:if>

    <%--modal infoForm include--%>
    <%@include file="/pages/jspf/infoForm.jspf"%>

</body>

<!-- java Script" -->
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/moment-with-locales.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="js/fileinput.min.js"></script>
<script type="text/javascript" src="js/fileinput_locale_ru.js"></script>

<c:if test="${autoShowModalForm.toString().length() > 0}">
    <script type="text/javascript">
        $(document).ready(function(){
            $("${autoShowModalForm}").modal('show');
        });
    </script>
</c:if>

<script type="text/javascript">
    $(function () {
        $('#datePrevOrder').datetimepicker(
            {locale: 'ru'}
        <c:choose>
            <c:when test="${currentLocale == 'en_us'}">
                {locale: 'en'}
            </c:when>
            <c:otherwise>
                {locale: 'ru'}
            </c:otherwise>
        </c:choose>
        );
    });
</script>

</html>
