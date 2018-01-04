# REST发布模式使用向导

[回到主页](Vesta.html)

使用中有任何问题和建议，请联系罗伯特，微信：[13436881186]()。

**Vesta发号器**是一个通用的发号器，它不但可以嵌入在原生Java程序中，还可以作为Restful服务进行发布，你只需要简单的几个步骤就可以成功搭建Vesta的Rest服务，并且在任何语言中都可以使用HTTP协议来获取全局唯一的ID。

-------------------

## 安装与启动

** 1. 下载最新版本的REST发布模式的发布包**

- 点击下载：

>[vesta-rest-netty-0.0.1-bin.tar.gz](../bin/vesta-rest-netty-0.0.1-bin.tar.gz)

- 如果你通过源代码方式安装Vesta的发布包到你的Maven私服，你可以直接从你的Maven私服下载此安装包：

>wget http://ip:port/nexus/content/groups/public/com/robert/vesta/vesta-rest-netty/0.0.1/vesta-rest-netty-0.0.1-bin.tar.gz

** 2. 解压发布包到任意目录**

>tar xzvf vesta-rest-netty-0.0.1-bin.tar.gz

** 3. 解压后更改属性文件**

- 属性文件：

>vesta-rest-netty-0.0.1/conf/vesta-rest-netty.properties

- 文件内容：

>vesta.machine=1022
vesta.genMethod=2
vesta.type=0

- 这里，机器ID为1022, 如果你有多台机器，递减机器ID，同一服务中机器ID不能重复。
— 这里，genMethod为2表示使用嵌入发布模式
— 这里，type为0, 表示最大峰值型，如果想要使用最小粒度型，则设置为1

** 4. REST发布模式的默认端口为8088,你可以通过更改启动文件来更改端口号,这里以10010为例**

- 启动文件：

>vesta-rest-netty/target/vesta-rest-netty-0.0.1/bin/server.sh

- 文件内容：

>port=10010


** 5. 修改启动脚本，并且赋予执行权限**

- 进入目录：

>cd vesta-rest-netty-0.0.1/bin

- 执行命令：

>chmod 755 *

** 6. 启动服务**

- 进入目录：

>cd vesta-rest-netty-0.0.1/bin

- 执行命令：

>./start.sh

** 7. 如果看到如下消息，服务启动成功**

>apppath: /home/robert/vesta/vesta-rest-netty-0.0.1
Vesta Rest Netty Server is started.

## 测试Rest服务

** 1. 通过URL访问产生一个ID**

- 命令：

>curl http://localhost:10010/genid

- 结果：

>**1138729511026688**

** 2. 把产生的ID进行反解**

- 命令：

>curl http://localhost:10010/expid?id=1138729511026688

- 结果：

>**{"genMethod":0,"machine":1,"seq":0,"time":12235264,"type":0,"version":0}**


JSON字符串显示的是反解的ID的各个组成部分的数值。

** 3. 对产生的日期进行反解**

- 命令：

>curl http://localhost:10010/transtime?time=12235264

- 结果：

>**Fri May 22 14:41:04 CST 2015**

** 4. 使用反解的数据伪造ID**

- 命令：

>http://localhost:8080/makeid?machine=1021&seq=0&time=94990103&genMethod=2&type=0&version=0

- 结果：

>**2305844108284681216**

## FAQ

**1.为什么有两个REST安装包？**

>**vesta-rest-0.0.1.tar.gz**和**vesta-rest-netty-0.0.1.tar.gz**都是Vesta的REST发布模式的服务器安装包，前者使用Spring Boot实现，内嵌Tomcat服务器，使用三层的线程池，Boss,Select和Executor线程池，由于发号器是大量的短小操作，没有后端的IO和网络IO等操作，三层线程池导致严重的线程环境切换，并且Tomcat的Executor线程池是动态扩容的，线程池默认最小线程数量为25个和最大为200个，大量的线程严重消耗系统资源，降低了系统的整体效率，鉴于这种情况，我们使用Netty实现了轻量级的Restful服务,Netty使用2层的线程池，Boss线程池只有一个线程，用于Accept NIO的请求并建立SocketChannel，Worker线程池默认为CPU内核的二倍，用于对SocketChannel的读写操作，2层的线程池并且使用少量的固定线程(5个)，大大的提高的系统的效率，经过压测证明Netty的效率是内嵌Tomcat的两倍，在2核心的笔记本上可答道11000 QPS。

**2.本文是以Netty的实现为例，如何搭建基于Spring Boot(Tomcat)的发号器的REST服务？**

>我们不推荐这样做，但是我们同样提供了Spring Boot的实现，除了安装包的文件名称不一样, 搭建Spring Boot的服务步骤与搭建Netty服务基本相同。

**3.Tomcat和Netty的性能相差多少？**

>Netty的效率是内嵌Tomcat服务器的两倍，在两核心2.4G I3 CPU上压测，Tomcat为5000 QPS， 而Netty为11000 QPS，细节请参考<a href="统一发号器(Vesta) - 性能压测报告.html" target="_blank">统一发号器(Vesta) - 性能压测报告</a>。

**4. REST发布模式支持Windows吗？**

> REST发布模式只支持Linux。
