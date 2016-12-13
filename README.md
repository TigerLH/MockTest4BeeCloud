# MockTest4BeeCloud
为客户端测试提供Mock服务

## Description

基于开源框架anyproxy搭建一套利于使用的Mock测试平台,Mock服务器的各种返回

代理框架：anyproxy

后端框架：Spring+mybatis+SpringMvc+pageHelper

前端框架：BootStrap+JQuery+Ajax

### HttpMock原理图

![image](https://github.com/TigerLH/MockTest4BeeCloud/tree/master/images/HttpMock.png)

### TboxMock原理图
![image](https://github.com/TigerLH/MockTest4BeeCloud/tree/master/images/TboxMock.png)

## 设计目的

for test:

Mock服务器的返回以满足以下类似测试场景的需求

1.服务器系统异常，客户端会怎么处理？

2.需要测试某个数值型数据在边界条件下客户端的响应，如何去构造这样的场景？

3.某个接口返回500,客户端有没有正常提示？

4.errorcode种类繁多，构建这样的条件好心累！！！

......

for dev:

前后端开发解耦

## 工具优点

1.可动态修改Mock状态及Mock值解决anyproxy修改rule文件后需要重启的问题

2.更友好的用户界面,可在线编辑新增mock及过滤规则

3.对于不需要Mock的请求,返回真实的服务器响应数据

4.无需修改客户端代码指向Mock服务




## 自动化部署

### Tomcat配置
添加具有角色管理器GUI和管理脚本的用户。
%TOMCAT7_PATH%/conf/tomcat-users.xml

	<?xml version='1.0' encoding='utf-8'?>
	<tomcat-users>
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="123456" roles="manager-gui,manager-script" />
	</tomcat-users>

### Maven 认证
添加Tomcat用户
%MAVEN_PATH%/conf/settings.xml

	<servers>
	<server>
		<id>TomcatServer</id>
			<username>admin</username>
			<password>123456</password>
		</server>
	</servers>
  
### pom.xml
	<plugin>
		<groupId>org.apache.tomcat.maven</groupId>
		<artifactId>tomcat7-maven-plugin</artifactId>
		<version>2.2</version>
		<configuration>
			<url>http://localhost:8080/manager/text</url>
			<server>TomcatServer</server>
			<path>/beecloud</path>
		</configuration>
	</plugin>
  
  
### 发布到Tomcat
mvn tomcat7:deploy

mvn tomcat7:undeploy

mvn tomcat7:redeploy



## HOW TO USE
### 运行anyproxy服务
anyproxy --rule rule_xx_xx_xx.js 端口默认为8001

### 运行Mock服务
mvn tomcat7:redeploy

### 持续集成
利用Jenkins一键启动

### 手机端连接代理
手机端wifi网络设置代理为启动的anyproxy服务地址:xxx.xxx.xxx.xxx:port

访问地址：http://${tomcat}/beecloud 

Mock服务中管理运行规则，修改返回值，进行测试

