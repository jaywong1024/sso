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
    <h3>测试</h3>
    <button id="create_uuid">创建UUID测试</button>
    <button id="crypt_by_DES">加密解密测试</button>
    <h3>登录测试</h3>
    <p>数据库后台只有一个用户，username: admin, password: 12345</p>
    <p>如果数据库解密并且验证用户名密码正确则返回登录成功，否者返回登录失败</p>
    username: <input type="text" id="username" value="admin">
    password: <input type="text" id="password" value="12345">
    <button id="docking">登录</button>

    <script>
        $('#create_uuid').click(function () {
            console.log(uuid());
        });
        $('#crypt_by_DES').click(function () {
            var data = '嘤嘤嘤';
            var key = uuid();
            console.log('明文数据: ' + data + ', 密钥: ' + key);
            var ciphertext = encryptByDES(data, key);
            console.log('加密后的密文数据: ' + ciphertext);
            var result = decryptByDES(ciphertext, key);
            console.log('密文再解密: ' + result);
        });
        $('#docking').click(useDefaultKey);

    //    使用随机秘钥向后端发送请求，发送请求时将秘钥放在请求头中
        function useUUID() {
            var data = $('#username').val() + ", " + $('#password').val();
        //    使用随机秘钥，发送请求时将秘钥放在请求头中
            var key = uuid();
            $.ajax({
                url: '/des',
                type: 'post',
                data:{
                //    传递密文数据
                    ciphertext: encryptByDES(data, key)
                }, success(result) {
                    console.log('打印服务端响应密文数据: ' + result.ciphertext);
                    console.log('解密后: ' + decryptByDES(result.ciphertext, key));
                },
            //    将秘钥写入请求头中
                beforeSend: setHeader(xhr, key)
            })
        }
        function useDefaultKey() {
            var data = $('#username').val() + ", " + $('#password').val();
        //    不使用随机秘钥，使用前后端双方约定好的秘钥
        //    var key = uuid();
            var key = 'huang-han-jie';
            $.ajax({
                url: '/des',
                type: 'post',
                data:{
                //    传递密文数据
                    ciphertext: encryptByDES(data, key)
                }, success(result) {
                    console.log('打印服务端响应密文数据: ' + result.ciphertext);
                    console.log('解密后: ' + decryptByDES(result.ciphertext, key));
                }
            })
        }
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
         * @param data 明文（数据源）
         * @param key 密钥
         * @returns {string}密文
         */
        function encryptByDES(data, key) {
        //    解析密钥为字节数据，将密钥转换为16进制数据
            var keyHex = CryptoJS.enc.Utf8.parse(key);
        //    创建 DES 加密工具（构建器）
            var encrypted = CryptoJS.DES.encrypt(data, keyHex, {
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
</body>
</html>
