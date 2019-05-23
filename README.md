# sso
sso study<br>

## domain

在 hosts 文件内映射一些项目所在的服务器 ip 地址作为测试是否跨域成功<br>
例如：<br>
    192.123.1.1 www.domain.com<br>
    192.123.1.1 sso.domain.com<br>
    192.123.1.1 test.domain.com<br>
    192.123.1.1 domain.com<br>
    <br>
这些都可以通过测试，因为在本项目的 session 跨域功能是通过 cookie 的 domain 和 path 来实现的
