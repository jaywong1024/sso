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
            username: <input type="text" name="username">
            password: <input type="text" name="password">
            <input type="submit" value="登录">
        </form>
        <h3>测试 Token 有效性</h3>
        <button>Test LocalStorage</button>
    </div>
</body>
</html>
