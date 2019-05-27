<%--
  Created by IntelliJ IDEA.
  User: jiqunsoftpc4
  Date: 2019/5/27
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
    <style>
        *{
            text-align: center;
        }
    </style>
</head>
<body>
    <div>
        <h3>登录测试</h3>
        <form action="" method="post">
            username: <input type="text" name="username"><br>
            password: <input type="text" name="password"><br>
            <input type="submit" value="登录">
        </form>
        <h3>测试 Token 有效性</h3>
        <button>Test LocalStorage</button>
    </div>
</body>
</html>
