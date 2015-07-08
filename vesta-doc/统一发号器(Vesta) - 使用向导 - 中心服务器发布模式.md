# 中心服务器发布模式使用向导

[回到主页](Vesta.html)

使用中有任何问题和建议，请联系罗伯特，微信：[13436881186]()。

**Vesta发号器**是一个通用的发号器，它除了支持嵌入发布模式和REST发布模式，还支持中心服务器发布模式。中心服务器通过Dubbo导出RPC服务，任何Java客户端可以通过Dubbo RPC导入中心服务器导出的发号器服务，也就是说，Java客户端可以透明的来调用Vesta RPC服务器提供的发号器服务，这就像调用本地API一样简单和方便。

-------------------

## 服务器安装与启动

** 1. 下载最新版本的REST发布模式的发布包**

- 点击下载：

>[vesta-server-0.0.1-bin.tar.gz](../bin/vesta-server-0.0.1-bin.tar.gz)

- 如果你通过源代码方式安装Vesta的发布包到你的Maven私服，你可以直接从你的Maven私服下载此安装包：

>wget http://ip:port/nexus/content/groups/public/com/robert/vesta/vesta-server/0.0.1/vesta-server-0.0.1-bin.tar.gz

** 2. 解压发布包到任意目录**

>tar xzvf vesta-server-0.0.1-bin.tar.gz

** 3. 解压后更改属性文件**

- 属性文件：

>/vesta-server-0.0.1/conf/vesta-server.properties

- 文件内容：

>vesta.service.register.center=multicast://224.5.6.7:1234
vesta.service.port=20880
vesta.machine=1023
vesta.genMethod=1
vesta.type=0

- 注意：

>-- 中心服务器发布模式的服务器RPC导出的默认端口为20880，注册中心使用内网广播，你可以通过更改属性文件来更改端口号和广播地址，或者也可以使用Zookeeper作为注册中心。
 -- 这里，机器ID为1023, 如果你有多台机器，递减机器ID，同一服务中机器ID不能重复。
 -- 这里，产生方法genMethod为1，表示是中心服务器发布模式
 -- 这里，type为0, 表示最大峰值型，如果想要使用最小粒度型，则设置为1
 -- 本向导为了简单起见，则不修改任何默认的配置属性, 例如：IP和端口等。

** 4. 修改启动脚本，并且赋予执行权限**

- 进入目录：

>cd vesta-server-0.0.1/bin

- 执行命令：

>chmod 755 *

** 5. 启动服务**

- 进入目录：

>cd vesta-server-0.0.1/bin

- 执行命令：

>./start.sh

** 6. 如果看到如下消息，服务启动成功**

>apppath: /home/robert/vesta/vesta-server-0.0.1
Vesta RPC Server is started.

## 客户端导入服务

** 1. 创建示例客户端Maven项目**

> 在Eclipse开发环境或者任何其他开发环境，创建一个Maven项目**vesta-sample-client**。

** 2. 在Maven的pom文件中添加对Vesta RPC客户端包的依赖**

>``` xml
		<dependency>
			<groupId>com.robert.vesta</groupId>
			<artifactId>vesta-client</artifactId>
			<version>0.0.1</version>
		</dependency>
```

** 3. 添加Vesta RPC客户端的属性文件**

- 添加属性文件：

>**src/main/resources/spring/vesta-client.properties**

- 文件内容：

>vesta.service.register.center=multicast://224.5.6.7:1234

- 这里以网络广播注册中心为例，如果你在服务器中配置的Zookeeper的注册中心，这里需要替换为Zookeeper的注册中心地址。

** 4. 添加Vesta RPC客户端的Spring环境文件**

- 添加Spring环境文件：

>**src/main/resources/spring/vesta-service-sample.xml**

- 文件内容：
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
        xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
	    http://code.alibabatech.com/schema/dubbo 
            http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

	<bean
		class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="locations" value="classpath:spring/vesta-client.properties" />
	</bean>

	<import resource="classpath:spring/vesta-client.xml" />
</beans>
```

** 5. 编写Java类取得发号器服务并调用服务**

>``` java
package org.vesta.sample.client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;
public class EmbedSample {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/vesta-client-sample.xml");
		IdService idService = (IdService) ac.getBean("idService");
        // 调用服务
		long id = idService.genId();
		Id ido = idService.expId(id);
        // 输出结果
		System.out.println(id + ":" + ido);
	}
}
```

** 6. 程序输出**

>**1138565321850880**:[seq=0,time=12078681,machine=1,genMethod=0,type=0,version=0]

>这里，冒号前面是生成的ID，后面中括号显示的是反解的ID的组成部分的数值。

## 示例源代码下载

>[vesta-sample-client-0.0.1-sources.jar](../bin/vesta-sample-client-0.0.1-sources.jar)

## FAQ

**1. 已存项目如何导入RPC服务？**

>从上面的示例，我们看到，RPC服务是通过Spring环境导出的，像其他主流项目一样，如果你的项目中同样使用Spring环境，你只需在你的Spring环境中导入Vesta RPC服务的IdService的Bean即可。
```xml
<import resource="classpath:spring/vesta-client.xml" />
```
同时，在你Spring环境使用的属性文件中增加机器ID的配置即可。
```properties
vesta.service.register.center=multicast://224.5.6.7:1234
```

**2. 既然我们有嵌入发布模式，为什么我们开发了中心服务器发布模式？**

> 嵌入发布模式的每台机器需要消耗一个机器ID，根据设计，机器ID占用10个位，也就是最多能有1024个节点，这样如果一个服务超过了1024个节点，将不能支持嵌入发布模式，这样我们需要使用中心服务器模式。

**3. 中心服务器发布模式支持Windows吗？**

> 中心服务器发布模式只支持Linux。

