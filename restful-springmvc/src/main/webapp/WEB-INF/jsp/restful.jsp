<%--
  Created by IntelliJ IDEA.
  User: jiqunsoftpc4
  Date: 2019/5/28
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Restful 风格</title>
    <script src="webjars/jquery/3.3.1-1/jquery.js"></script>
    <style>
        *{
            text-align: center;
        }
    </style>
</head>
<body>

    <h3>用户数据</h3>
    <p id="users"></p>
    <h3>增删改查操作</h3>
    id: <input type="text" name="id" id="id">
    username: <input type="text" name="username" id="username">
    <br><br>
    <button id="insert">新增</button>
    <button id="delete">删除</button>
    <button id="update">修改</button>
    <button id="select">查询</button>

    <script>
        let id, username;
        $(function () {
            sel();
            $('#insert').click(function () {
                regain();
                ins(id, username);
                sel();
            });
            $('#delete').click(function () {
                regain();
                del(id);
                sel();
            });
            $('#update').click(function () {
                regain();
                upd(id, username);
                sel();
            });
            $('#select').click(function () {
                regain();
                sel(id);
                sel();
            });
        });
        function regain() {
            id = $('#id').val();
            username = $('#username').val();
        }
        function ins(id, username) {
            // 使用 post 请求新增用户信息
            $.ajax({
                url: '/user',
                type: 'post',
                data: {
                    id: id,
                    username: username
                }, success(result) {
                    alert(result);
                }
            });
            // alert('insert');
        }
        function del(id) {
            // 使用 delete 请求删除用户信息
            $.ajax({
                url: '/user?id=' + id,
                type: 'delete',
                success(result) {
                    alert(result);
                }
            });
            // alert('delete');
        }
        function upd(id, username) {
            // 使用 put 请求更新用户信息
            $.ajax({
                url: '/user',
                type: 'put',
                data: JSON.stringify({
                    id: id,
                    username: username
                }), success(result) {
                    alert(result);
                }
            });
            // alert('update');
        }
        function sel(id) {
            // 使用 get 请求获取用户信息，使用 restful 风格将要查询的用户 id 写入 url 中
            $.ajax({
                url: '/user/' + id,
                type: 'get',
                success(result) {
                    alert(result);
                }
            });
            // alert('select');
        }
        function sel() {
            $.ajax({
                url: '/user',
                type: 'get',
                success(result) {
                    $('#users').empty();
                    $('#users').html(result);
                }
            });
        }
    </script>

</body>
</html>
