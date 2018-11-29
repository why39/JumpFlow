 **软件环境** 
- JDK1.8 (需要自行安装)
- MySQL5.7.17 (需要自行安装)
- Maven3.0 (需要自行安装)
- Tomcat7.0 (需要自行安装)
- redis 3.07 (需要自行安装)

 **本地部署**
- 创建数据库hxyframe_activiti，数据库编码为UTF-8,项目sql/db/hxyframe.sql脚本

```
CREATE DATABASE hxyframe_activiti DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
> source sql/db/hxyframe.sql;
```
- 修改application-dev.yml文件，更改MySQL账号和密码
- 启动redis服务,可在官方群下载。
- 项目访问路径：http://localhost:8080/hxyActiviti/index.html
