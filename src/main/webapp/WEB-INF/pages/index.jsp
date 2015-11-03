<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
        <%@include file="/WEB-INF/pages/jspf/headerPage.jspf"%>

        <section id="steps" style="margin-bottom: 5px; padding: 5px; display: block;">
            <div class="center">
                <div class="row-height panel-primary" id="books">
                    <div class="row row-height">

                        <%--sectionBook include--%>
                        <%@include file="/WEB-INF/pages/jspf/sectionBook.jspf"%>

                        <div class="col-md-9 row-height" style="position: relative;">
                            <div class="panel panel-default">
                                <%--searchParamBook include--%>
                                <%@include file="/WEB-INF/pages/jspf/panelSearchParamBook.jspf"%>

                                <%--panelListBook include--%>
                                <%@include file="/WEB-INF/pages/jspf/panelListBook.jspf"%>

                                <%--panelPagingFooterBook include--%>
                                <%@include file="/WEB-INF/pages/jspf/panelPagingFooterBooks.jspf"%>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="clear">&nbsp;</div>
        </section>

        <%--footer include--%>
        <%@include file="/WEB-INF/pages/jspf/footerPage.jspf"%>
    </div>

    <%--modal loginForm include--%>
    <%@include file="/WEB-INF/pages/jspf/loginForm.jspf"%>

    <%--modal registrationForm include--%>
    <%@include file="/WEB-INF/pages/jspf/registrationForm.jspf" %>

    <%--modal infoForm include--%>
    <%@include file="/WEB-INF/pages/jspf/infoForm.jspf"%>

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
