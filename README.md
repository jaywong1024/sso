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

Session 跨域（domain）
---
### 简介
- 就是摒弃了系统（Tomcat）提供的 session，而使用自定义的类似 session 的机制来保存客户端数据的一种解决方案
### 方案
- 通过设置 cookie 的 domain 来实现 cookie 的跨域传递。在 cookie 中传递一个自定义的 session_id，这个 session_id 是客户端的唯一标记。将这个唯一标记作为 key，将客户端需要保存的数据作为 value，在服务区进行保存（数据库保存或者 NoSQL 保存）。这种机制就是 Session 的跨域解决
- 使用 cookie 跨域共享，是 session 跨域的一种解决方案。Cookie.setDomain() 为 cookie 设置了有效 域 范围，cookie.setPath() 为 cookie 设定有效 URL 范围。以上两个条件就实现了 cookie 跨域，这是在开发中性价比最高的跨域解决方案
### 测试
在 hosts 文件内映射一些项目所在的服务器 ip 地址作为测试是否跨域成功<br>
#### 例如：
    192.123.1.1 www.domain.com
    192.123.1.1 sso.domain.com
    192.123.1.1 test.domain.com
    192.123.1.1 domain.com
<br>
这些域名都可以通过跨域测试，因为在本模块中，session 跨域功能是通过设置 cookie 的 domain 和 path 来实现的<br>

Spring Session 共享（spring-session）
---

### 简介

- spring-session 技术是 Spring 提供的用于处理集群会话功效的解决方案
- spring-session 技术是将用户 session 数据保存到第三方存储容器中（MySQL、Redis 等）
- spring-session 技术是解决同域名下的多服务器集群 session 问题的，不能解决跨域 session 共享问题

### 实现

- 配置一个由 Spring 提供的 Filter（DelegatingFilterProxy），实现数据的拦截和保存，并转换为 spring-session 需要的会话对象
- 定义用于提供 HttpSession 数据持久化操作的 Bean 对象（JdbcHttpSessionConfiguration）
    - 对象定以后，可以配置数据库相关的操作设置，自动实现 HttpSession 数据的持久化操作（CRUD）
    - 这里的 Bean 对象不需要 id 和 name，因为 DelegatingFilterProxy 是 byType 的
- 必须提供一个数据库用于存储数据，存储数据的表格由 spring-session 提供，在 spring-session-jdbc.jar 包 /org/springframework/session/jdbc 文件夹内。MySQL 数据库就使用 schema-mysql.sql 创建表格
    - spring-session 表：保存客户端 session 对象的表格
    - spring-session-attributes 表：保存客户端 session 中 attribute 属性数据的表格

### 测试

在客户端向服务器发起请求和服务器响应请求时，向 Session 中写入或者获取 Attribute，倘若数据库中成功写入 Attribute 或者从数据库中获取的 Attribute 是与另一个系统同步的，那么 Spring Session 就算共享成功啦

Nginx Session 共享（关联）
---

### 方案

- 使用 nginx 中的 ip_hash 技术，将某个 ip 发来的请求定向到同一台服务器上，这样一来这个 ip 下的某个客户端和某一台服务器就能建立稳固的 session
- 因为仅仅能用 ip 这个因子来分配后端服务器，因此 ip_hash 是有缺陷的，不能在以下情况使用：
    - nginx 不是最前端的服务器：ip_hash 要求 nginx 一定是最前端的服务器，否则 nginx 得不到正确的 ip 地址，就不能根据 ip 作为因子进行 hash
    - nginx 的后端还有其他方式负载均衡：假如 nginx 后端还有其他的负载均衡，将请求通过另外的方式进行分流了，那么某个客户端的请求就不能定位在同一台 session 应用服务器上

### 正向代理

- 正向代理，意思是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端
- 客户端才能使用正向代理，例如：VPN

### 反向代理

- 在计算机网络中，反向代理是代理服务器的一种
- 服务器根据客户端的请求，从其关联的一组或多组后端服务器（如Web服务器）上获取资源，然后再将这些资源返回给客户端，客户端只会得知反向代理的IP地址，而不知道在代理服务器后面的服务器簇的存在

### Nginx 简介

- Nginx 是使用 C 语言开发的服务器，可以提供的的服务有
    - 静态 WEB 服务器（类似 Apache http server）
    - 电子邮件代理服务器
    - 反向代理服务器
    - 虚拟主机
- Nginx 服务器的特点
    - 应用体积非常的小
    - 对处理器和内存的占有很少
    - 并发能力、负载能力强
    
### Nginx 安装

#### 安装环境

    1. GCC：安装 nginx 需要将官网下载的源码进行编译，编译依赖 gcc 环境，如果没有 gcc 环境则需要安装 gcc
        # yum install -y gcc-c++
    2. PCRE：PRRE（Perl Compatible Regular Expressions）是一个 Perl 库，包括 perl 兼容正则表达式的库。nginx 的 http 模块使用 perl 来解析正则表达式，所以需要在服务器上安装 PCRE 库
        # yum install  -y pcre pcre-devel
    3. ZLIB：zlib 库提供了很多种压缩和解压缩，nginx 使用 zlib 对 http 包内容进行 gzip，所以需要在服务器上安装 zlib 库
        # yum install -y zlib zlib-devel
    4. OpenSSL：OpenSSL 是一个强大的安全套接字层密码库，囊括主要密码算法、常用的密钥和证书封装管理功能以及 SSL 协议，并提供丰富的应用程序供测试或其他目的使用。nginx 不仅支持 http 协议，还支持 https 协议（即在 SSL 协议上传输 http），所以需要在服务器上安装 OpenSSL
        # yum install -y openssl openss-devel

#### 编译和安装

	1. 官网下载 nginx 源码压缩包 http://nginx.org/en/download.html
	2. 解压缩源码压缩包 # tar -zxvf nginx-x.x.x.tar.gz
	3. 进入解压好的文件夹中 # cd nginx-x.x.x
	4. 执行 configure 来实现 nginx 的配置，可以通过 ./configure --help 来查看详细参数，以下是常用配置信息：
		a. --prefix=/usr/local/src/nginx-x.x.x ：安装位置目录
		b. --pid-path=/var/run/nginx/nginx.pid：启动时进程描述文件保存的位置和进程文件名
		c. --lock-path=/var/lock/nginx.lock：锁定文件
		d. --error-log-path=/var/log/nginx/error.log：错误日志
		e. --http-log-path=/var/log/nginx/access.log：访问日志
		f. --with-http_gzip_static_module：压缩和解压缩模式
		g. --http-client-body-temp-path=/home/temp/nginx/client：客户端请求体的临时文件的目录
		h. --http-proxy--temp-path=/home/temp/nginx/proxy：存储从代理服务器接收到的数据的临时文件
		i. --http-fastcgi-temp-path=/home/temp/nginx/fastcgi：存储从FastCGI的服务器接收到的数据的临时文件
		j. --http-uwsgi-temp-path=/home/temp/nginx/uwsgi：储从uwsgi服务器接收到的数据的临时文件
		k. --http-scgi-temp-path=/home/temp/nginx/scgi：存储从SCGI服务器接收到的数据的临时文件
		l. 注意：nginx 可以创建临时目录，但不可以创建临时目录的父目录。需要有服务器权限的管理员进行父目录的创建，可以使用 -p 递归地创建目录 # mkdir -p /home/temp/nginx
	5. 编译：将源代码编译成可执行文件 # make
    6. 安装：将编译完成后的可执行文件运行并安装，形成一个可运行应用程序 #make install
    
#### 启动和停止

	1. Nginx 在安装成功后，在安装位置下有三个文件夹
		a. sbin 文件夹存放可执行文件
		b. html 文件夹存放 nginx 提供的静态页面
		c. conf 文件夹存放配置文件
	2. 启动 nginx：# sbin/nginx 或者在 sbin 目录下 # ./nginx 
		a. 在启动 nginx 时可以使用 -c 加载指定的配置文件，比如 # sbin/nginx -c /home/temp/nginx-temp.conf
		b. 如果不指定则加载默认的 conf/nginx.con 文件（此文件可在在编译安装 nginx 前执行 configure 来配置文件位置）
	3. 查询 nginx 进程：# ps aux|grep nginx
	4. 停止 nginx：# sbin/nginx -s stop 或者 # sbin/nginx -s quit
		a. stop：快速停止，直接杀死进程
		b. quit：等待 nginx 的进程任务处理完毕后停止
		
### 实现（配置 upstream 模块）
- 先在安装目录 /nginx 打开 nginx 配置文件进行编辑 # vim conf/nginx.conf
- 然后在 http{} 标签内添加 upstream 模块，upstream 默认算法是权重轮巡 wwr（weighted round-robin），这里为了解决 session 共享问题我们需要使用 ip_hash 模式分配

#### 例如：

    // 添加 upstream 模块
    upstream iphash.test.com {
	    server 192.168.1.100:8080;
	    server 192.168.1.101:8080;
	    ip_hash; //如果不添加 ip_hash 则使用默认算法 wwr
    }
    // 在 server{} 中将 server_name 属性修改为 iphash.test.com
    server {
        server_name=iphash.test.com;
    }
    // 在 server{} 内的 location / {} 中添加 proxy_pass http://iphash.test.com
    server {
        location / {
            proxy_pass http://iphash.test.com;
        }
    }

### 测试

客户端输入 http://iphash.test.com 对服务器发起请求，如果响应的页面总是同一个服务器上的页面则代表 ip_hash 成功，此 ip 地址下的客户端已经和其中的一个服务器关联成功，session 绑定在这个服务器上

Token 机制
---

### 传统身份认证

- HTTP 是一种没有状态的协议，也就是它并不知道发送此请求访问应用的是谁。如果把用户看成是客户端，客户端使用账号和密码通过了身份验证，但是因为HTTP请求不保存任何状态信息，所以这个客户端再次发送请求的时候，还要验证一遍身份
- 解决方案：当用户请求登录时，如果身份验证没有问题登录通过，就在服务端生成一条记录，这个记录里可以说明一下登录的用户是谁，然后把这条记录的 ID 号发给客户端，客户端收到这个 ID 号保存在 Cookie 中。下次这个用户再向服务端发起请求的时候，可以带着这个 Cookie，这样服务端回去验证一下这个 Cookie 内的信息，在服务器中寻找对应的记录，如果找到了对应的记录则代表用户通过了身份验证，就把用户请求的数据返回给客户端
- 以上解决方案说的就是 Session，服务端需要存储用户进行登录而产生的 Session，这些 Session 可能会存储在内存、磁盘或者数据库里。我们可能需要在服务端定期地去清理过期的 Session
- 这种认证会引发一些问题：
    - Session：每次认证用户发起请求时，服务器需要去创建一个记录来存储信息。当越来越多用户发起请求时，内存的开销会越来越大
    - 可扩展性：在服务端的内存中使用 Session 存储登录信息，伴随而来的是可扩展性问题。因为使用 Session 来存储登录信息是有一个具体的物理信息的，虽然可是使用 nginx 来解决客户端关联服务端问题，但也不能解决扩展性问题
    - CORS（跨域资源共享）：当需要服务器的数据跨多台移动设备上使用时，跨域资源共享会是一个会是一个很头疼的问题。在使用 AJAX 抓取另一个域的资源时，就可能出现禁止请求的情况
    - CSRF（跨站请求伪造）：用户在访问银行网站时，很容易受到跨站请求伪造的攻击，并且能被利用其访问其他网站
- 在这些问题中，可扩展性时最突出的。因此我们有必要去寻求一种更有行之有效的方法

### Token 身份认证

使用基于 Token 的身份验证方法，在服务端不需要存储用户的登录记录。以下是 Token 身份验证的大致流程：
- 客户端使用账号和密码请求登录
- 服务端收到请求，去验证账号和密码
- 验证成功后，服务端会签发一个 Token，再把这个 Token 发送给客户端
- 客户端收到 Token 后可以把它存储起来。比如放在 Cookie 里或者 Local Storage 里
- 客户端每次向服务端请求资源时都需要带着服务端签发的 Token
- 服务端收到请求，然后去验证客户端请求里面带着的 Token，如果验证成功，就向客户端返回请求的数据

JSON WEB TOKEN（JWT）机制
---

### JWT 简介

	1. JWT 是一种紧凑且自包含的，用于在多方传递 JSON 对象的技术
		a. 紧凑：数据小，可以通过 URL、POST 参数、请求头发送，而且因为数据较小所以传输速度快
		b. 自包含：使用 payload 数据块记录用户必要且不隐私的数据，可有效减少数据库访问次数，提高代码性能
	2. 传递的数据可以使用数字签名来增加其安全性，可以使用 HMAC 加密算法或者 RSA 公钥/私钥 加密方式
	3. JWT 一般用于处理用户身份认证或者数据信息交换
		a. 用户信息认证：一旦用户登录后，每个后续的请求都必须包含 JWT，允许用户访问该令牌允许的路由、服务和资源。单点登录是当今广泛使用 JWT 的一项功能，因为 JWT 的体积很小，而且可以轻松的跨不同的域使用
		b. 数据信息交换：JWT 是一种非常方便的多方传递数据的载体，因为其可以使用数据签名来保证数据的有效性和安全性
    
### JWT 数据结构

	1. JWT 的数据结构由三部分组成，分别是 Header 和 Payload 还有 Signature，它们之间使用 "." 分隔开
	2. Header 头信息数据结构：{"alg" : "加密算法名称", "typ" : "JWT"}
		a. alg 是加密算法名称，例如：HMAC、SHA256 和 RSA 等
		b. typ 是 Token 类型，这里定义为 JWT
	3. Payload 有效荷载：payload 数据块一般用于记录 Token 的具体内容。它分为三部分，分别是 已注册信息（registered claims）、公开数据（public claims）、私有数据（public claims）
		a. 已注册信息：就是在 JWT 中已经注册的标准字段，例如：
			i. iss：Issuer，发行者
			ii. sub：Subject，主题
			iii. aud：Audience，受众
			iv. exp：Expiration time，过期时间
			v. nbf：Not before，不早于
			vi. iat：Issued at，发行时间
			vii. jti：JWT ID
		b. 公开数据：公开数据部分一般都会在 JWT 注册表中添加定义，这样就可以利用 JWT 检测数据的有效性，还可以避免和已注册的信息产生冲突
		c. 私有数据：和公开数据一样可以由程序员任意定义，但也要避免和已注册信息产生冲突
		d. 注意：即使 JWT 有签名加密机制，但是 Payload 内容都是明文记录，除非记录的是加密数据，否则不排除泄露隐私数据的可能性。不推荐在 Payload 中记录任何敏感数据
	4. Signature 签名：这是一个由开发者提供的信息，先将加密后的 Header 和 Payload 使用 "." 连接起来，然后再次进行加密就可以得到最终结果。Signature 是服务器验证传递的数据安全有效的标准

### JWT 执行流程

![JWT 执行流程](https://raw.githubusercontent.com/ChinesePowerful/sso/master/IMG/JWT%20%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B.png)

基于 JWT 机制的单点登录（jwt）
---

### 实现

[查看代码](https://github.com/ChinesePowerful/sso/tree/master/jwt)

### 注意

- 使用 JWT 实现单点登录时，需要注意 Token 的有效性。Token 是保存在客户端的令牌数据，如果永久有效，则有被劫持的可能
- Token 在设计的时候，可以考虑一次性或一段时间内有效。如果设置有效时长，则需要考虑是否要刷新 Token 的有效期问题

### Token 的保存位置

- 使用 JWT 技术生成的 Token，客户端在保存的时候可以考虑 Cookie 或 LocalStorage
- Cookie 保存方式可以实现跨域传递数据，LocalStorage 是域私有的本地存储，无法实现跨域
- 建议仅使用 Cookie 做数据的存储，而不是数据的传递。在传递 Token令牌时建议将其放入 Header 中的 Authorization 中

### WebStorage

- WebStorage 可保存的数据容量为 5M，且只能存储字符串数据
- WebStorage 分为 LocalStorage 和 SessionStorage
- LocalStorage 的生命周期是永久的，关闭页面或浏览器之后 LocalStorage 中的数据也不会消失。LocalStorage除非主动删除数据，否则数据是永久保存的
- SessionStorage 是会话相关的本地存储单元，生命周期是仅在当前会话有效
- SessionStorage 引入了一个“浏览器窗口”的概念，SessionStorage 是在同源的窗口中始终存在的数据，只要这个浏览器窗口没有关闭，即使刷新页面或者进入同源的另一个页面，数据依然存在。但是 SessionStorage 在关闭了浏览器窗口后就会被销毁。同时独立的打开同一个窗口同一个页面，SessionStorage 也是不一样的

Restful 接口设计
---