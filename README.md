# sso
单点登录 Single Sign On，在一个多系统共存的环境下，用户在一处登录后，就不用在其他系统再次登录，也就是说用户的一次登录就能得到其他所有系统的信任<br><br>
实现单点登录要解决如何生产和存储“信任”，以及其他系统如何验证这个信任的有效性

##domain

在 hosts 文件内映射一些项目所在的服务器 ip 地址作为测试是否跨域成功<br>
例如：<br>
    192.123.1.1 www.domain.com<br>
    192.123.1.1 sso.domain.com<br>
    192.123.1.1 test.domain.com<br>
    192.123.1.1 domain.com<br>
    <br>
这些都可以通过测试，因为在本项目的 session 跨域功能是通过 cookie 的 domain 和 path 来实现的
