<%--
  Created by IntelliJ IDEA.
  User: vane
  Date: 2014/11/20
  Time: 03:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html lang="EN" xmlns:jsp="http://java.sun.com/JSP/Page" >

    <head>
        <title>leaveTrack</title>
        <link href="<c:url value="/resources/theme/css/design.css"/>" rel="stylesheet"/>
    </head>
    <body>
        <c:url value="/j_spring_security_logout" var="logoutUrl" />
        <div class="applicationBody">

            <div class="heading">
                <p>&nbsp<span class="headingLeft">Change Password </span>
                    <span class="headingRight">${employeeSession.firstName} ${employeeSession.lastName} (${employeeSession.username})</span></p>
            </div>

            <div class="form">
                <div class="notification">
                    <p>Employee details successfully changed</p>
                    <P>Please log out</p>
                    <form action="${logoutUrl}" method="post">
                        <input type="submit" value="Log out" />
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </div>
                <%@ include file="/resources/theme/footer.jsp" %>
            </div>
        </div>
    </body>
</html>
