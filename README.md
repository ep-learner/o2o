# Readme

## 核心技术栈

前端：SUI Mobile

后端：SSM框架/ Thumbnailator / Kaptcha 

数据库：Mysql，主从同步，读写分离

缓存：Redis

部署：阿里云，腾讯云



## 主从数据库

主库：116.62.102.69:3306

从库：122.51.12.140:3306

1、数据库备份

主库中进行数据库的备份

```
mysqldump -uroot –p -P3306 --databases o2o > dump.sql
```

从库进行数据库恢复

```
mysql -uroot -p -P3306 < dump.sql
```



2、主库的配置

```
vi /etc/my.cnf;
##添加如下信息
server-id=200                     #主服务器的ID
innodb_flush_log_at_trx_commit=2  # 事务处理时写log的速度 
sync_binlog=1                     #开启binlog日志同步功能
log-bin=mysql-bin-200             #binlog日志文件名
binlog-do-db=o2o                  #只同步o2o （如果没有此项，表示同步所有的库）
##
```



重启主库

```sh
systemctl restart mysqld.service
mysql -uroot –p
grant replication slave on *.* to 'slave'@'122.51.12.140' identified by '123456';
flush privileges;
```



3、从库的配置

```
vi /etc/my.cnf
##添加如下信息
server-id=201  #设置主服务器的ID
sync_binlog=1  #开启binlog日志同步功能
log-bin=mysql-bin-201  #binlog日志文件名
innodb_flush_log_at_trx_commit=2
```



主库查看文件同步起点

```sql
change master to
master_host='116.62.102.69',   ##访问的数据库地址
master_port=3306,             ##访问的数据库端口号
master_user='slave',            ##访问的数据库时从库用户名
master_password='123456',    ##访问的数据库的密码
master_log_file='mysql-bin-100.000001', master_log_pos=0;
```



启动slave

```
start slave;
show slave status \G;
```



## 读写分离

重写拦截方法，运行时根据SQL类型动态获取数据源实现读写分离。

动态数据源

```java
package com.o2o.dao.split;
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDbType();
    }
}
```



DynamicDataSourceHolder

```java
private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
public static String getDbType(){
    String db = contextHolder.get();
    if(db==null){ db = DB_MASTER; }//默认访问主库
    return db;
}
```



DynamicDataSourceInterceptor

实现mybatis插件的拦截器接口，重写了拦截的逻辑

```java
public Object intercept(Invocation invocation) throws Throwable {
    String dbType = DynamicDataSourceHolder.DB_MASTER;
    //根据访问类型设置该字符串的值 严格控制写请求由主库执行,详见源码
    DynamicDataSourceHolder.setDbType(dbType);
    return invocation.proceed();
}
public Object plugin(Object o) {
    if (o instanceof Executor){
        return Plugin.wrap(o,this);
    }//如果线程属于Executor接口的实例则把intercept添加进线程中
    else return o;
}
public void setProperties(Properties properties) { }
```



spring-dao.xml：使用懒加载数据源代理动态决定使用的数据源

```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy">
   <property name="targetDataSource">
      <ref bean="dynamicDataSource" />
   </property>
</bean>
```



Mybatis-config.xml:添加拦截器插件

```xml
<plugin interceptor="com.o2o.dao.split.DynamicDataSourceInterceptor"></plugin>
```





## 实体类

| 实体类          | 实体类含义 |
| --------------- | ---------- |
| Area            | 地域信息   |
| Shop            | 店铺       |
| ShopCategory    | 店铺类别   |
| Product         | 产品       |
| ProductCategory | 产品类别   |
| ProductImg      | 详情图     |
| LocalAuth       | 本地账号   |
| PersonInfo      | 用户       |
| HeadLine        | 头条       |

![1584021639080](https://github.com/ep-learner/o2o/blob/master/pic.png)

## 主要功能

1、用户系统：提供用户注册，登录和密码修改模块，多次验证失败需要输入验证码；



2、店家系统：

1）登录之后用户可以注册店铺，新注册的店铺需要后台审核通过才允许修改；

2）店铺管理页面：可以进行店铺信息更新，管理店铺经营的商品类别，以及进行商品管理。



3、展示系统：

1）首页展示：头条信息和一级店铺类别；

2）店铺查询：可根据店铺类别，店铺名称，店铺所处区域组合查询

3）店铺商品展示：显示店铺缩略图，简介，创建时间，电话等信息；可根据商品类别，商品名组合查询商品

4）商品展示界面：显示商品详情图和描述信息



## 项目地址
http://116.62.102.69/o2o/front/index
PS：考虑使用体验，建议在移动端访问。

[参考链接:慕课网](https://coding.imooc.com/class/144.html)
