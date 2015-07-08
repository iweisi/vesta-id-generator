# 嵌入发布模式使用向导

[回到主页](Vesta.html)

使用中有任何问题和建议，请联系罗伯特，微信：[13436881186]()。

**Vesta发号器**的最简单也是最常用的发布模式为嵌入式发布模式，正如嵌入式发布模式的名字一样，它是将发号器服务嵌入到业务项目中，并且提供JVM进程内的本地服务，其效率最高，配置最简单，使用最方便的一种方法。

-------------------

## 新项目使用向导

** 1. 创建示例Maven项目**

>在Eclipse开发环境或者任何其他开发环境，创建一个Maven项目**vesta-sample-embed**。

** 2. 增加依赖的嵌入模式的Jar包**

- 如果你通过源代码方式安装Vesta的发布包到你的Maven私服，在Maven的pom文件中添加对Vesta发号器服务包的依赖。

>```xml
		<dependency>
			<groupId>com.robert.vesta</groupId>
			<artifactId>vesta-service</artifactId>
			<version>0.0.1</version>
		</dependency>
```

- 如果你没有Maven私服，你可以点击下载依赖的Jar包，并且添加到当前项目的类路径中。

>[vesta-service-0.0.1.jar](../lib/vesta-service-0.0.1.jar)

** 3. 添加Vesta发号器的属性文件**

- 添加属性文件：

>**src/main/resources/spring/vesta-service.properties**

- 文件内容：

>``` bash
		vesta.machine=1
        vesta.genMethod=0
        vesta.type=0
```

- 注意：

>-- 这里，机器ID为1, 如果你有多台机器，递增机器ID，同一服务中机器ID不能重复。
-- 这里，生成方式genMethod为0表示使用嵌入发布模式
-- 这里，type为0, 表示最大峰值型，如果想要使用最小粒度型，则设置为1

** 4. 添加Vesta发号器的Spring环境文件**

- 添加Spring环境文件：

>**src/main/resources/spring/vesta-service-sample.xml**

- 文件内容：

>``` xml
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
		<property name="locations" value="classpath:spring/vesta-service.properties" />
	</bean>
	<import resource="classpath:spring/vesta-service.xml" />
</beans>
```

** 5. 编写Java类取得发号器服务并调用服务**

>``` java
package org.vesta.sample.embed;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;
public class EmbedSample {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/vesta-service-sample.xml");
		IdService idService = (IdService) ac.getBean("idService");
        // 从这里调用服务
		long id = idService.genId();
		Id ido = idService.explainId(id);
        // 输出
		System.out.println(id + ":" + ido);
	}
}
```

** 6. 程序输出**

- 输出

>**1138565321850880**:[seq=0,time=12078681,machine=1,genMethod=0,type=0,version=0]

- 这里，冒号前面是生成的ID，后面中括号显示的是反解的ID的组成部分的数值。

## 已存项目集成向导

1. 从上面的示例，我们看到，Vesta发号器的服务是通过Spring环境导出的，像其他主流项目一样，如果你的项目中同样使用Spring环境，你只需在你的Spring环境中导入Vesta服务的IdService的Bean即可。

>```xml
<import resource="classpath:spring/vesta-service.xml" />
```

2. 同时，在你Spring环境使用的属性文件中增加机器ID的配置即可。

>```xml
vesta.machine=1
```

## 示例代码下载

>[vesta-sample-embed-0.0.1-sources.jar](../src/vesta-sample-embed-0.0.1-sources.jar)
