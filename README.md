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

Nginx Session 共享
---

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
    
- 正向代理
    - 正向代理，意思是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端
    - 客户端才能使用正向代理，例如：VPN

- 反向代理
    - 在计算机网络中，反向代理是代理服务器的一种
    - 服务器根据客户端的请求，从其关联的一组或多组后端服务器（如Web服务器）上获取资源，然后再将这些资源返回给客户端，客户端只会得知反向代理的IP地址，而不知道在代理服务器后面的服务器簇的存在
    
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
		
### 配置