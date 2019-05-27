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
            <button id="submit_login">登录</button>
        </form>
        <h3>测试 Token 有效性</h3>
        <button id="button_test_localStorage">Test LocalStorage</button>
    </div>
    <script>
        $(function () {
            $('#submit_login').click(function () {
                $.post('/login', {
                    username: $('#username').val(),
                    password: $('#password').val()
                }, function (result) {
                    console.log(result);
                    if (200 === result.code) {
                        // 将 token 存放到 localStorage 中
                        // 可以在浏览器的开发者工具中的 Application 查看 LocalStorage
                        window.localStorage.token = result.token;
                    }
                    alert(result.message);
                });
            });
            $('#button_test_localStorage').click(function () {
                $.ajax({
                    url : '/validateJWT',
                    type: 'POST',
                    success : function (result) {
                        console.log(result);
                        if (200 === result.code) {
                            // 将 token 存放到 localStorage 中
                            // 可以在浏览器的开发者工具中的 Application 查看 LocalStorage
                            window.localStorage.token = result.token;
                        }
                        alert(result.message);
                    },
                    beforeSend : setHeader
                });
            });
        });

        /**
         * 将 Token令牌放入请求头的 Authorization 中
         * @param xhr
         */
        function setHeader(xhr) {
            xhr.setRequestHeader('Authorization', window.localStorage.token);
        }
    </script>
</body>
</html>
