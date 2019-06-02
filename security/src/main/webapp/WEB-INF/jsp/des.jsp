<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DES加密</title>
    <script type="text/javascript" src="webjars/jquery/3.3.1-1/jquery.js"></script>
    <script type="text/javascript" src="/js/core.js"></script>
    <script type="text/javascript" src="/js/cipher-core.js"></script>
    <script type="text/javascript" src="/js/enc-base64.js"></script>
    <script type="text/javascript" src="/js/mode-ecb.js"></script>
    <script type="text/javascript" src="/js/tripledes.js"></script>
    <style>
        *{
            text-align: center;
        }
    </style>
</head>
<body>

    <h3>服务端只有一个用户（username:admin, password:12345）</h3>
    username: <input type="text" id="username" name="username" value="admin">
    password: <input type="text" id="password" name="password" value="12345">
    <button id="login">登录</button>
    <script>

    //    与服务端对应的密钥，一般存放在数据库或本地文件中
        var KEY = 'huang-han-jie';

        /**
         * 创建一个 uuid
         * @returns uuid
         */
        function uuid() {
            var temp = [];
        //    16进制数字
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                temp[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            temp[14] = 4;
            temp[19] = hexDigits.substr((temp[19] & 0x3) | 0x8, 1);
            temp[8] = temp[13] = temp[18] = temp[23] = "-";
            var uuid = temp.join("");
            return uuid;

        }

        /**
         * 加密函数
         * @param plaintext 明文
         * @param key 密钥
         * @returns {string}密文
         */
        function encryptByDES(plaintext, key) {
        //    解析密钥为字节数据，将密钥转换为16进制数据
            var keyHex = CryptoJS.enc.Utf8.parse(key);
        //    创建 DES 加密工具（构建器）
            var encrypted = CryptoJS.DES.encrypt(plaintext, keyHex, {
            //    加密模式
                mode: CryptoJS.mode.ECB,
            //    加密的填充模式
                padding: CryptoJS.pad.Pkcs7
            });
        //    加密，并获取加密后的密文数据
            return encrypted.toString();
        }

        /**
         * 解密函数
         * @param ciphertext 密文数据
         * @param key 密钥
         * @returns {string}明文数据
         */
        function decryptByDES(ciphertext, key) {
        //    解析密钥为字节数据，将密钥转换为16进制数据
            var keyHex = CryptoJS.enc.Utf8.parse(key);
        //    创建 DES 解密工具
            var decrypted = CryptoJS.DES.decrypt({
            //    将密文解析成可解密的字节数据
                ciphertext: CryptoJS.enc.Hex.parse(ciphertext)
            }, keyHex, {
                //    加密模式
                mode: CryptoJS.mode.ECB,
                //    加密的填充模式
                padding: CryptoJS.pad.Pkcs7
            });
        //    解密，并获取解密后的明文数据
            return decrypted.toString(CryptoJS.enc.Utf8);
        }

        /**
         * 将 key 放入请求头
         * @param xhr XmlHttpRequest
         * @param key 秘钥
         */
        function setHeader(xhr, key) {
            xhr.setRequestHeader('key', key);
        }

    </script>
    <script>
        $('#login').click(function () {
            var plaintext = $('#username').val() + "," + $('#password').val();
            $.ajax({
                url: '/des',
                type: 'post',
                data: {
                    ciphertext: encryptByDES(plaintext, KEY)
                }, success(result) {
                    alert(decryptByDES(result, KEY));
                }
            })
        });
    </script>
</body>
</html>
