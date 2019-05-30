<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DES加密</title>
    <script type="text/javascript" src="webjars/jquery/3.3.1-1/jquery.js"></script>
    <script type="text/javascript" src="/js/core.js"></script>
    <script type="text/javascript" src="/js/cipher-core.js"></script>
    <script type="text/javascript" src="/js/mode-ecb.js"></script>
    <script type="text/javascript" src="/js/tripledes.js"></script>
    <style>
        *{
            text-align: center;
        }
    </style>
</head>
<body>
    <button id="create_uuid">创建UUID</button>
    <script>
        $('#create_uuid').click(function () {
            console.log(uuid());
        });
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
         * @returns 密文
         */
        function encryptByDES(data, key) {
        //    解析密钥为字节数据，将密钥转换为16进制数据
            var keyHex = CryptoJS.enc.Utf8.parse(key);
        //    创建 DES 加密工具（构建器）
            var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
            //    加密模式
                mode: CryptoJS.mode.ECB,
            //    加密的填充模式
                padding: CryptoJS.pad.Pkcs7
            });
        //    加密，并获取加密后的密文数据
            return encrypted.toString();
        }
        function decryptByDES(encryptedText, key) {
        //    解析密钥为字节数据，将密钥转换为16进制数据
            var keyHex = CryptoJS.enc.Utf8.parse(key);
        //    创建 DES 解密工具
            var decrypted = CryptoJS.DES.decrypt({
                ciphertext: CryptoJS.enc.Base64.parse(encryptedText)
            }, keyHex, {
                //    加密模式
                mode: CryptoJS.mode.ECB,
                //    加密的填充模式
                padding: CryptoJS.pad.Pkcs7
            })
        }
    </script>
</body>
</html>
