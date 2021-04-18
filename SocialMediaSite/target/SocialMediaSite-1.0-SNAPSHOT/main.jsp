<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.SocialMediaSite.ConnectSQL" %>
<%@ page import="java.io.PrintWriter" %>
<%--
  Created by IntelliJ IDEA.
  User: JustMorgan
  Date: 4/17/2021
  Time: 8:27 AM
  To change this template use File | Settings | File Templates.

--%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style><%@include file="/WEB-INF/style.css"%></style>
    <jsp:include page="/PostPageServlet" />
    <title>PostPage</title>
</head>
<body>
<div class="header">
       <a href="#default" class="logo">SocialMedia</a>
       <a href="#default" class="logo"><%=session.getAttribute("username")%></a>
       <div class="header-right">
           <a class="active" href="#home">Home</a>
           <a href="#contact">Contact</a>
           <a href="#about">About</a>
          </div>
        </div>

<h2>PostPage</h2>

<div class="center">
    <form method="POST" action="main.jsp">
        <textarea placeholder="What's up?" id="w3review" name="w3review" rows="4" cols="50"></textarea>
        </br>
        <button class="button" name="submit-button">
            <span class="mdc-button__label">Submit Post</span>
        </button>
    </form>
</div>

<h2></h2>
            <%
                ConnectSQL connectSQL = new ConnectSQL();
                PrintWriter writer = response.getWriter();
                connectSQL.getPostsSQL();
                writer.println("<div class=\"posts\" >");
                        connectSQL.getHashMap(response.getWriter());
                writer.print("</div>");
            %>
</body>
</html>
