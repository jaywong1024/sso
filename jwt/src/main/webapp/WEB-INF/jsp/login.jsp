<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
    <script src="webjars/jquery/3.3.1-1/jquery.js"></script>
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
            username: <input type="text" name="username" id="username">
            password: <input type="text" name="password" id="password">
            <input type="submit" value="登录" id="submit_login">
        </form>
        <h3>测试 Token 有效性</h3>
        <button id="button_test_localStorage">Test LocalStorage</button>
    </div>
    <script>
        $(function () {
            $('#submit_login').click(function () {
                // $.post('/login', {
                //     username: $('#username').val(),
                //     password: $('#password').val()
                // }, function (result) {
                //     console.log(result);
                // });
                alert('login');
            });
            $('#button_test_localStorage').click(function () {
                alert('test');
            });
        });
    </script>
</body>
</html>
