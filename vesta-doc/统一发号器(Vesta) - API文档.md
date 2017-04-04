# API文档

[回到主页](Vesta.html)

使用中有任何问题和建议，请联系罗伯特，微信：[13436881186]()。

**Vesta发号器**提供Java服务的原生API和Restful服务API，前者应用在嵌入发布模式和中心服务器发布模式的客户端，而后者应用在REST发布模式中，本节将简单介绍Java服务的原生API和Restful服务的API的使用方法。

-------------------

## Restful API文档

### 1. 产生ID

- 描述：根据系统时间产生一个全局唯一的ID并且在方法体内返回。
- 路径：/genid
- 参数：N/A
- 非空参数：N/A
- 示例：http://localhost:8080/genid
- 结果：3456526092514361344

### 2. 反解ID

- 描述：对产生的ID进行反解，在响应体内返回反解的JSON字符串。
- 路径：/expid
- 参数：id=？
- 非空参数：id
- 示例：http://localhost:8080/expid?id=3456526092514361344
- 结果：{"genMethod":2,"machine":1022,"seq":0,"time":12758739,"type":0,"version":0}

### 3. 翻译时间

- 描述：把长整型的时间转化成可读的格式。
- 路径：/transtime
- 参数：time=?
- 非空参数：time
- 示例：http://localhost:8080/transtime?time=12758739
- 结果：Thu May 28 16:05:39 CST 2015

### 4. 制造ID

- 描述：通过给定的ID元素制造ID。
- 路径：/makeid
- 参数：genMethod=?&machine=?&seq=?&time=?&type=?&version=?
- 非空参数：time,seq
- 示例：http://localhost:8080/makeid?genMethod=2&machine=1022&seq=0&time=12758739&type=0&version=0
- 结果：3456526092514361344

## Java API文档

### 1. 产生ID

- 描述：根据系统时间产生一个全局唯一的ID并且在方法体内返回。
- 类：IdService
- 方法：genId
- 参数：N/A
- 返回类型：long
- 示例：long id = idService.genId();

### 2. 反解ID

- 描述：对产生的ID进行反解，在响应体内返回反解的JSON字符串。
- 类：IdService
- 方法：expId
- 参数：long id
- 返回类型：Id
- 示例：Id id = idService.expId(3456526092514361344);

### 3. 翻译时间

- 描述：把长整型的时间转化成可读的格式。
- 类：IdService
- 方法：transTime
- 参数：long time
- 返回类型：Date
- 示例：Date date = idService.transTime(12758739);

### 4. 制造ID(1)

- 描述：通过给定的ID元素制造ID。
- 类：IdService
- 方法：makeId
- 参数：long time, long seq
- 返回类型：long
- 示例：long id = idService.makeId(12758739, 0);

### 5. 制造ID(2)

- 描述：通过给定的ID元素制造ID。
- 类：IdService
- 方法：makeId
- 参数：long machine, long time, long seq
- 返回类型：long
- 示例：long id = idService.makeId(1, 12758739, 0);

### 6. 制造ID(3)

- 描述：通过给定的ID元素制造ID。
- 类：IdService
- 方法：makeId
- 参数：long genMethod, long machine, long time, long seq
- 返回类型：long
- 示例：long id = idService.makeId(0, 1, 12758739, 0);

### 7. 制造ID(4)

- 描述：通过给定的ID元素制造ID。
- 类：IdService
- 方法：makeId
- 参数：long type, long genMethod, long machine, long time, long seq
- 返回类型：long
- 示例：long id = idService.makeId(0, 2, 1, 12758739, 0);

### 8. 制造ID(5)

- 描述：通过给定的ID元素制造ID。
- 类：IdService
- 方法：makeId
- 参数：long version, long type, long genMethod, long machine, long time, long seq
- 返回类型：long
- 示例：long id = idService.makeId(0, 0, 2, 1, 12758739, 0);
