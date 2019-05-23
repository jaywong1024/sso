[单点登录](https://github.com/ChinesePowerful/sso/tree/master)
=======
**单点登录 Single Sign On，在一个多系统共存的环境下，用户在一处登录后，就不用在其他系统再次登录，也就是说用户的一次登录就能得到其他所有系统的信任**<br>
**实现单点登录要解决如何生产和存储“信任”，以及其他系统如何验证这个信任的有效性**

域和跨域
---
### 域

- 在应用模型中，一个完整的，有独立访问路径的功能集合成为一个域
- 百度称为一个应用或系统，百度下有若干的域，如：
    - 搜索引擎（www.baidu.com）
    - 百度贴吧（tieba.baidu.com）
    - 百度知道（zhidao.baidu.com）等
- 域的划分：以 ip 地址、端口、域名、主机名为标准，实现划分
- 域信息，有时也称为多级域名
### 跨域
- 客户端请求的时候，请求的服务器不是同一个 ip 地址、端口、域名、主机名的时候，都称为跨域

domain 模块
---
### Session 跨域
- 就是摒弃了系统（Tomcat）提供的 session，而使用自定义的类似 session 的机制来保存客户端数据的一种解决方案
### 方案
- 通过设置 cookie 的 domain 来实现 cookie 的跨域传递。在 cookie 中传递一个自定义的 session_id，这个 session_id 是客户端的唯一标记。将这个唯一标记作为 key，将客户端需要保存的数据作为 value，在服务区进行保存（数据库保存或者 NoSQL 保存）。这种机制就是 Session 的跨域解决
- 使用 cookie 跨域共享，是 session 跨域的一种解决方案。Cookie.setDomain() 为 cookie 设置了有效 域 范围，cookie.setPath() 为 cookie 设定有效 URL 范围。以上两个条件就实现了 cookie 跨域，这是在开发中性价比最高的跨域解决方案
### 测试
在 hosts 文件内映射一些项目所在的服务器 ip 地址作为测试是否跨域成功<br><br>
#### 例如：<br>
- 192.123.1.1 www.domain.com<br>
- 192.123.1.1 sso.domain.com<br>
- 192.123.1.1 test.domain.com<br>
- 192.123.1.1 domain.com<br>
<br>
这些都可以通过跨域测试，因为在本模块中，session 跨域功能是通过 cookie 的 domain 和 path 来实现的<br>

spring-session 模块
---