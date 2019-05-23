# sso
单点登录学习记录

###域
	1. 在应用模型中，一个完整的，有独立访问路径的功能集合成为一个域
	2. 百度称为一个应用或系统，百度下有若干与域，如：
		a. 搜索引擎（www.baidu.com）
		b. 百度贴吧（tieba.baidu.com）
		c. 百度知道（zhidao.baidu.com）等
	3. 域的划分：以 ip 地址、端口、域名、主机名为标准，实现划分
	4. 域信息，有时也称为多级域名

###跨域
客户端请求的时候，请求的服务器不是同一个 ip 地址、端口、域名、主机名的时候，都称为跨域

##domain

在 hosts 文件内映射一些项目所在的服务器 ip 地址作为测试是否跨域成功<br>
例如：<br>
    192.123.1.1 www.domain.com<br>
    192.123.1.1 sso.domain.com<br>
    192.123.1.1 test.domain.com<br>
    192.123.1.1 domain.com<br>
    <br>
这些都可以通过测试，因为在本项目的 session 跨域功能是通过 cookie 的 domain 和 path 来实现的
