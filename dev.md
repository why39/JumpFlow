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

- - -

Neo4j 可视化：

[https://github.com/jexp/cy2neo](https://github.com/jexp/cy2neo)
- - -

Neo4j Tips

1. 查询某节点下游所有节点（不包含该节点）

```
MATCH (n:Task) WHERE n.name="185017" WITH n 
MATCH p = (n) - [*] -> (m:Task)
RETURN m
```

2. 查询某节点上游所有节点（不包含该节点）

```
MATCH (n:Task) WHERE n.name="187509" WITH n 
MATCH p = (m:Task) - [*] ->(n)
RETURN m
```

3. 清空关系和节点
MATCH (n)-[r]-()
DELETE n,r

match (n)
delete n
- - -

开发新模型

1. com.hxy.modules.demo 目录下的:

- XXXEntity
- XXXDao
- XXXService,XXXServiceImpl
- XXXController

2. WEB-INF/demo 目录下的:

- xxx.jsp : 实例主页
- xxxEdit.jsp : 新建实例页面
- xxxActInfo.jsp : 审批环节的页面

- - -

docker install fastdfs : https://blog.csdn.net/Odyssues_lee/article/details/80863189

运行tracker：

```
docker run -dti --network=host --name tracker -v /var/fdfs/tracker:/var/fdfs delron/fastdfs tracker
```

运行storage：

```
docker run -dti --network=host --name storage -e TRACKER_SERVER=127.0.0.1:22122 -v /var/fdfs/storage:/var/fdfs delron/fastdfs storage
```


配置下载url
statics/js/oss/oss.js editLink()

- - -
Activiti

1. 控制组件的显示：statics/plugins/process/editor-app/stencil-controller.js

- - -
操作说明：

1. 节点设置第一个审批人要和当前部署模型的人一样，不然不能提交实例。
2. 业务树德每个流程key都不能一样。