- 基于maven，Spring Boot，Shiro，Redis，Ehcache，Thymeleaf，Postgresql的后台动态权限管理脚手架。

### 主要实现
- Spring Boot 和 Shiro 的整合
- 使用Spring Boot Data Jpa进行数据访问
- 基于Redis的多节点集群Session共享
- 实现Redis和Caffeine多级缓存
- 节点数据层使用Ehcache进行数据缓存
- 基于URL的权限管理
- 包含单位、部门、人员的基础管理
- 采用Bcrypt替代hash + salt的密码验证模式
- 集成flowable-modeler作为工作流编辑器
- 支持微信小程序JWT登录及API验证
- 集成腾讯云短信
- 前后台及数据库敏感数据AES加解密
- 配置文件敏感信息jasypt加密
- 支持多数据库（AES加解密视图，以及flowable用户体系视图需要修改）
- Spring Boot 多环境切换
- 开发模式P6Spy数据输出

### 运行方法
1. 安装数据库
2. 将配置文件中spring.jpa.hibernate.ddl-auto 修改为create。（其它SQL文件，见hades-foundation/src/resources/sql）
3. hades-platform中， mvn clean install
4. 运行: http://localhost:9999
5. 开发环境配置加密，可以在hades-application的HadesWebApplicationTests进行测试和修改

### 默认账户
- 前台 administrator 123456
- 后台 system 123456

### 界面预览
![登录界面](https://images.gitee.com/uploads/images/2020/0316/182215_68f07c3d_751495.png "微信截图_20200316181847.png")
![主界面](https://images.gitee.com/uploads/images/2020/0316/182659_10c995be_751495.png "微信截图_20200316181932.png")

### 核心依赖
依赖 | 版本
---|---
Spring Boot | 2.1.9.RELEASE
Shiro | 1.5.2
Flowable | 6.4.2
weixin-java-miniapp | 3.3.5.B
Postgresql | > 10 

### 开发方法
1. 直接在现有工程中修改（不推荐）
2. 在本工程中 mvn clean install，然后新建一个spring boot工程，在pom.xml中增加依赖即可
```xml
        <dependency>
            <groupId>cn.com.felix</groupId>
            <artifactId>hades-foundation</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>cn.com.felix</groupId>
            <artifactId>hades-bpmn</artifactId>
            <version>1.0.0</version>
        </dependency>
```

### 模块说明
hades-platform<br/>
  |- hades-application 工程运行和实验模块<br/>
  |- hades-dependencies 依赖管理<br/>
  |- hades-common 通用代码模块<br/>
  |- hades-data 数据相关通用代码模块<br/>
  |- hades-kernel 核心配置模块<br/>
  |- hades-foundation 应用相关代码模块<br/>
  |- hades-bpmn flowable-modeler集成模块<br/>
 
### 交流方式
1. 欢迎提交[issue](https://gitee.com/pointer_v/hades-multi-module-scaffold/issues)， 请写清楚遇到问题的原因、开发环境、重现步骤
2. QQ：113797117
3. mailto:pointer_v@qq.com
4. CSDN: https://blog.csdn.net/Pointer_v

### License
( Apache License v2.0 )

> 后续视情况，逐步开放Security版以及微服务版

 
 
 





