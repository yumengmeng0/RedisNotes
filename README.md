# Redis

# 1. 概述

## 1.1 互联网架构的演变历程
* 第1阶段
  * 数据访问量不大
![alt 第1阶段](images/第1阶段.png)
* 第2阶段
  * 数据访问量大，使用缓存技术
  * 不同业务访问不同的数据库
![alt 第2阶段](images/第2阶段.png)
* 第3阶段
  * 主从读写分离
![alt 第3阶段](images/第3阶段.png)
* 第4阶段
  * MyISAM使用表锁，所以并发性能特别差
  * 分库分表开始流行
  * MySQL集群
![alt 第4阶段](images/第4阶段.png)

## 1.2 Redis入门
* 互联网需求三高：
  * 高并发、高可扩、高性能
* Redis是一种运行速度快、并发能力强，运行在内存上的NoSQL（not only sql）数据库
* 较传统数据库的优势
  * NoSQL不需要事先为存储的数据建立字段，随时可以存储自定义的数据格式
  * 关系型数据库，增删字段是一件非常麻烦的事
* Redis使用场景
  * 缓存
  * 排行榜
  * 计算器/限速器
  * 好友关系
  * 简单消息队列
  * Session共享

## 1.3 Redis/Memcache/MongoDB
* NoSQL数据库的代表

### 1.3.1 Redis和Memcache
* 都是内存数据库，Memcache还可以缓存图片、视频
* Memcache数据结构单一（k-v），Redis提供list、set、hash等数据结构，有效减少网络IO的次数
* 虚拟内存：当Redis物理内存用完时，可以将很久没用到的value交换到磁盘
* 存储数据安全，：Memcache没有持久化机制，Redis定期保存到磁盘
* 灾难恢复：Memcache挂掉后数据不可恢复，Redis可以通过RBD或AOF恢复

### 1.3.2 Redis和MongoDB
* Redis和MongoDB是协作共存的关系

## 1.4 分布式数据库和CAP原理

### 1.4.1 CAP简介
* 传统的关系型数据库具备ACID:
  * A 原子性
  * C 一致性 
  * I 独立性
  * D 持久性

* 分布式数据库的CAP：
  * Consistency：强一致性
    * 所有节点在同一时间的数据完全一致
  * Availability：高可用性
    * 服务一直可用
  * Partition tolerance：分区容错性
    * 分布式系统遇到某个节点或网络分区故障，仍然能够对外提供满足一致性和可用性的服务
### 1.4.2 CAP理论
* CAP理论提出是针对分布式数据库环境的，P属性是必须具备的
* 因为P是必须的，需要选择的是A和C

### 1.4.3 CAP总结
* 分区是常态，不可避免，三者不能共存
* 可用性和一致性是互斥的
  * 一致性高，可用性低，一致性低，可用性高
* 根据CAP原理将NoSQL数据库分成满足：
  * CA-单点集群，满足一致性，可用性的系统，通常在可扩展上不太强大
  * CP
  * AP

# 2. Redis的下载和安装
## 2.1 [下载](https://redis.com/)
https://redis.com/

## 2.2 安装
```shell
# 解压
tar zxvf redis*.tar.gz
# 安装gcc
yum -y install gcc
# 进去redis目录编译
make
# 安装目录PREFIX
make install PREFIX=/opt/redis-7.0.5/
# 安装后在 /opt/redis-7.0.5/bin
# 启动redis 
/opt/redis-7.0.5/bin/redis-server

```
## 2.3 操作
### 2.3.1 后台方式运行
```shell
# redis后台启动，修改redis.conf选项为：daemonize yes
# 根据配置文件启动
./redis-server ../redis.conf
```

### 2.3.2 关闭
```shell
# 单实例关闭
redis-cli shutdown
# 多实例关闭
redis-cli -p 6379 shutdown
```
### 2.3.3 常用操作
```shell
# 查看6379端口是否在监听
netstat -lntp|grep 6379
# 检测后台进程是否存在
ps -ef|grep redis
```

### 2.3.4 连接并测试
```shell
./redis-cli
ping
```
### 2.3.5 HelloWorld
```shell
# set key value [NX|XX] [GET] [EX seconds|PX milliseconds|EXAT unix-time-seconds|PXAT unix-time-milliseconds|KEEPTTL]
set k1 china
get k1
```

### 2.3.6 测试性能
```shell
redis-benchmark
# ctrl+c 退出
```
  ====== PING_INLINE ======                                                   
  100000 requests completed in 1.32 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1
  host configuration "save": 3600 1 300 100 60 10000
  host configuration "appendonly": no
  multi-thread: no
  
  Latency by percentile distribution:
  0.000% <= 0.255 milliseconds (cumulative count 9)
  50.000% <= 0.479 milliseconds (cumulative count 51872)
  75.000% <= 0.559 milliseconds (cumulative count 76681)
  87.500% <= 0.599 milliseconds (cumulative count 88033)
  93.750% <= 0.631 milliseconds (cumulative count 95093)
  96.875% <= 0.647 milliseconds (cumulative count 97037)
  98.438% <= 0.671 milliseconds (cumulative count 98459)
  99.219% <= 0.711 milliseconds (cumulative count 99258)
  99.609% <= 0.767 milliseconds (cumulative count 99615)
  99.805% <= 0.879 milliseconds (cumulative count 99807)
  99.902% <= 1.351 milliseconds (cumulative count 99903)
  99.951% <= 1.639 milliseconds (cumulative count 99952)
  99.976% <= 1.783 milliseconds (cumulative count 99976)
  99.988% <= 1.895 milliseconds (cumulative count 99988)
  99.994% <= 1.951 milliseconds (cumulative count 99994)
  99.997% <= 1.975 milliseconds (cumulative count 99997)
  99.998% <= 1.991 milliseconds (cumulative count 99999)
  99.999% <= 2.007 milliseconds (cumulative count 100000)
  100.000% <= 2.007 milliseconds (cumulative count 100000)
  
  Cumulative distribution of latencies:
  0.000% <= 0.103 milliseconds (cumulative count 0)
  3.664% <= 0.303 milliseconds (cumulative count 3664)
  29.324% <= 0.407 milliseconds (cumulative count 29324)
  59.495% <= 0.503 milliseconds (cumulative count 59495)
  90.124% <= 0.607 milliseconds (cumulative count 90124)
  99.169% <= 0.703 milliseconds (cumulative count 99169)
  99.716% <= 0.807 milliseconds (cumulative count 99716)
  99.829% <= 0.903 milliseconds (cumulative count 99829)
  99.881% <= 1.007 milliseconds (cumulative count 99881)
  99.900% <= 1.103 milliseconds (cumulative count 99900)
  99.912% <= 1.407 milliseconds (cumulative count 99912)
  99.926% <= 1.503 milliseconds (cumulative count 99926)
  99.944% <= 1.607 milliseconds (cumulative count 99944)
  99.965% <= 1.703 milliseconds (cumulative count 99965)
  99.977% <= 1.807 milliseconds (cumulative count 99977)
  99.989% <= 1.903 milliseconds (cumulative count 99989)
  100.000% <= 2.007 milliseconds (cumulative count 100000)
  
  Summary:
  throughput summary: 75528.70 requests per second
  latency summary (msec):
  avg       min       p50       p95       p99       max
  0.475     0.248     0.479     0.631     0.695     2.007

### 2.3.7 默认16个数据库
```shell
# 配置文件，默认数据库数量16（0-15）
# databases 16
# 切换数据库16
select 15 

```

### 2.3.8 数据库键数量
```shell
redis-cli

dbsize
```

### 2.3.9 清空数据库
```shell
# 当前
flushdb
# 所有
flushall
```

### 2.3.10 模糊查询（key）
```shell
# * 任意数量字符
key *
# ? 单个字符
# [] 某个一个字符
key r[ea]dis

# 查看是哪个数据库默认是0不显示序号
127.0.0.1:6379> SELECT 1
OK
127.0.0.1:6379[1]> SELECT 0
OK
127.0.0.1:6379> 
```

### 2.3.11 键(key)
```shell
# 判断某个键是否存在
exists key
# 移动键到其他库
move key db
# 查看键还有多久过期（-1 永不过期，-2已过期）time to live
ttl key
(integer) -1
# 为键设置过期时间
expire key 秒
# 查看数据类型
type key
string

```
  127.0.0.1:6379> exists k1
  (integer) 1 # 存在
  127.0.0.1:6379> exists y1
  (integer) 0 # 不存在


# 3. 使用Redis

## 3.1 五大数据类型
文档：http://redisdoc.com




### NOTES
```shell
# 查看linux版本
cat /proc/version
uname -a

```
  aliyun
  Linux aliyun 5.10.23-5.al8.x86_64 #1 SMP Fri Apr 23 16:56:08 CST 2021 x86_64 x86_64 x86_64 GNU/Linux
  Linux version 5.10.23-5.al8.x86_64 (mockbuild@x86-006.build.alibaba.eu6) (gcc (GCC) 10.2.1 20200825 (Alibaba 10.2.1-3 2.30), GNU ld version 2.35-12.2.al8) #1 SMP Fri Apr 23 16:56:08 CST 2021

  tecent
  Linux version 5.4.0-126-generic (buildd@lcy02-amd64-072) (gcc version 9.4.0 (Ubuntu 9.4.0-1ubuntu1~20.04.1)) #142-Ubuntu SMP Fri Aug 26 12:12:57 UTC 2022
  Linux VM-4-11-ubuntu 5.4.0-126-generic #142-Ubuntu SMP Fri Aug 26 12:12:57 UTC 2022 x86_64 x86_64 x86_64 GNU/Linux

  阿里云是龙蜥发行版，编译redis5.0.4出错：collect2: error: ld returned 1 exit status，安装最新redis7没问题，
  腾讯云Ubuntu编译和安装redis5.0.4成功

```shell
make 
# 出错：
#collect2: error: ld returned 1 exit status
#make[1]: *** [Makefile:219: redis-server] Error 1
#make[1]: Leaving directory '/opt/redis/src'
#make: *** [Makefile:6: all] Error 2

cd src && make all

# 出错
#collect2: error: ld returned 1 exit status
#make: *** [Makefile:219: redis-server] Error 1

# 修改.make-settings
vi .make-settings
-march=


cc1: error: CPU you selected does not support x86-64 instruction set
make: *** [Makefile:248: adlist.o] Error 1
# 查看CPU信息
cat /proc/cpuinfo
# 阿里云是e5 2682 v4
# 腾讯云 Xeon(R) Gold 6133 CPU @ 2.50GHz


```

