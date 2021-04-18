<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>SocialMedia</title>
    <style><%@include file="/WEB-INF/style.css"%></style>
</head>
<body>
<h1><%= "SocialMedia!" %>
</h1>
<br/>
<div id="error"></div>
<div class="login-box">
    <form name="login" action="./LoginServlet" method="POST">
        <label for="username"><h3>Username:</h3></label><br>
        <input type="text" id="username" name="username">
        <br>
        <label for="password"><h3>Password:</h3></label><br>
        <input type="password" id="password" name="password">
        <br>
        <button class="button" name="submit-button">
            <span class="mdc-button__label">Login</span>
        </button>
    </form>
</div>
</body>
</html>























