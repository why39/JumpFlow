**项目说明** 
- hxyFrame-activiti-boot是一个快速开发的工作流框架，采用流行的框架springBoot+mybatis+shiro+redis开发,实现了权限管理（菜单权限、数据权限）,activiti工作流程引擎，完善的代码生成器
感兴趣可以Watch、Start持续关注项目最新状态，加入QQ群：210315502 解决各种疑难杂证，丰富的学习资源。


**项目功能** 
- 权限管理：采用Shiro实现功能权限和机构部门的数据控件权限，可控件菜单权限、按钮权限、机构部门权限（数据权限）
- 工作流程引擎：采用主流的activiti流程引擎,在原基础上扩展了动态添加审批人员范围选择、会签节点的动态设置、排它路由条件设置、
              节点可编辑字段设置、节点执行后回调函数、办理任务、驳回到发起人从新发起、退回到上一步、自由跳转、转办等功能。在开发的过程中，
              只需要简单业务流程树，尊守一些规则就可以很方便的使用流程，后面还考虑加入自定义表单，使开发变的更加简单。
- 缓存：使用redis整合shiro自定义sessionDao实现分布式集群共享session
- quartz定时任务：可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- app接口：基于Json web token (JWT)认证用户信息,使用swagger生成一个具有互动性的api文档控制台。
- 页面交互使用了vue+html和最普通的jsp+jstl标签，两种交互都写了相应的模板，可以选择适合的交互方式。
- 完善的代码生成机制，可在线生成entity、xml、dao、service、html、js、sql代码,可快速开发基本功能代码，能把更多的精力放在问题难点。
- 采用layer友好的弹框，和layerUI相对漂亮的界面，让管理系统系统看起来稍微好看点。

**项目结构** 

![项目结构](http://osaowv4s0.bkt.clouddn.com/upload/20171227/804cbeaebf394964890e67fb7899e469 "项目结构")


**项目信息** 
- 提供了SpringMVC版hxyFrame，获取【[SpringMVC](https://gitee.com/soEasyCode/hxyFrame)】
- 提供了SpringBoot基础版hxyFrame-base-boot，获取【[SpringBoot基础版](https://gitee.com/soEasyCode/hxyFrame-base-boot)】
- 项目demo地址(测试系统性能有限,如访问速度较慢,还请耐心等待)：http://118.24.146.49:8080/frame-admin 帐户/密码:hxy/a 
- 项目开发文档：https://pan.baidu.com/s/1i5oymod
- oschina仓库：https://gitee.com/soEasyCode/hxyFrame-activiti-boot
- github仓库：https://github.com/huangxianyuan/hxyFrame-activiti-boot
- 交流QQ群：210315502
- 麻烦帮忙Watch、Star项目，这样才能关注到项目的最新更新，谢谢亲的支持！

 **技术选型：**
  
- 核心框架：Spring Boot 1.5.8.RELEASE
- 工作流引擎：Activiti 5.22.0
- 缓存：redis 3.07
- 权限框架：Apache Shiro 1.3
- 持久层框架：MyBatis 3.3
- 数据库：mysql 5.7
- 定时器：Quartz 2.2.3
- 前端页面：Vue2.x、jstl、bootstrap、layer、layerUI


 **软件环境** 
- JDK1.8
- MySQL5.7.17
- Maven3.0
- Tomcat7.0
- redis 3.07

 **本地部署**
- 创建数据库hxyframe_activiti，数据库编码为UTF-8,项目sql/db/hxyframe.sql脚本
- 修改application-dev.yml文件，更改MySQL账号和密码
- 启动redis服务,可在官方群下载。
- 项目访问路径：http://localhost:8083/hxyActiviti/


 **捐赠测试**
- 开源不易，维护更新更不容易，如果项目帮助到了你，给作者一点动力吧，项目才能更加完善强大。请捐赠时请备注捐赠信息！谢谢支持
 ![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20170829/59da2d5b80464f67a035f529b6eb0dad "图片说明")


**项目图片：**

![图片说明]( http://osaowv4s0.bkt.clouddn.com/upload/20171214/ea35e2f3e3d2417d911e0245e8f59f8c "图片说明")
![图片说明]( http://osaowv4s0.bkt.clouddn.com/upload/20171214/a5763c56c6dc474b918992867ccc60c3 "图片说明")
![图片说明]( http://osaowv4s0.bkt.clouddn.com/upload/20171214/f44860f9d19141b882c73daabadafbd6 "图片说明")
![图片说明]( http://osaowv4s0.bkt.clouddn.com/upload/20171214/bd5165dc6df14497a508023b28b237f3 "图片说明")
![图片说明]( http://osaowv4s0.bkt.clouddn.com/upload/20171214/9f8564d5e11e4c1f9c9e5cf52ad2bd50 "图片说明")
![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20171214/6d4eaab348f7483f9af7661e4c1890b7 "图片说明")
![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20171214/61b2c782a5d747cd922be922e97f9b1d "图片说明")
![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20171214/6dca83d4070649ecb5468baea4819433 "图片说明")
![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20171214/afc3db6415eb463186358920a7dc0240 "图片说明")
![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20171214/a49f42f188a14b4aac9ac405d26dfa03 "图片说明")
![图片说明](http://osaowv4s0.bkt.clouddn.com/upload/20171214/4d2e611ac4694dcea1b3597c3de58b1d "图片说明")



