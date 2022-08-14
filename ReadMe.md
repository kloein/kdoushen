# 1、如何让项目运行起来？

## 1.1、创建数据库与表

以MySQL为例，创建以下表

用户表

```SQL
CREATE TABLE `t_user`(
	`uid` BIGINT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL    
)
```

用户信息表

```SQL
CREATE TABLE `t_user_msg`(
	`uid` BIGINT PRIMARY KEY,
  `username` VARCHAR(255) NOT NULL,
  `follow_count` BIGINT NOT NULL,
	`follower_count` BIGINT NOT NULL
)
```

视频信息表

```SQL
CREATE TABLE `t_video`(
    `vid` BIGINT PRIMARY KEY,
    `uid` BIGINT NOT NULL,
  	`play_url` VARCHAR(255) NOT NULL,
    `cover_url` VARCHAR(255) NOT NULL,
    `publish_time` timestamp NOT NULL,
    `title` VARCHAR(255) NOT NULL
)
```

点赞表

```SQL
CREATE TABLE `t_like`(
	`id` BIGINT PRIMARY KEY,
    `uid` BIGINT NOT NULL,
    `vid` BIGINT NOT NULL,
    `cancel` INT NOT NULL DEFAULT 0
)
```

评论表

```SQL
CREATE TABLE `t_comment`(
	`id` BIGINT PRIMARY KEY,
	`uid` BIGINT NOT NULL,
	`vid`	BIGINT NOT NULL,
	`comment_text` VARCHAR(255),
	`comment_time` TIMESTAMP NOT NULL,
	`cancel` INT NOT NULL
)
```

## 1.2、开启redis

默认端口为6379，如果改为其他，请在配置文件中修改

## 1.3、开启ActiveMQ

本项目使用ActiveMQ作为消息队列，请开启你的ActiveMQ服务并且在配置文件中修改

## 1.4、修改配置文件

根据application-example.yml中的提示修改你的配置，并且在application.yml文件中选择你的配置文件

# 2、功能介绍

项目中实现了以下功能

* 视频流推送:从数据库查询最新发布的视频并返回，使用多线程优化查询速度
* 用户登录与注册：将用户账号密码写入数据库，并且使用MD5对密码进行加密
* 用户信息获取：从数据库获取用户信息，如果redis中有该用户信息，优先从redis中获取
* 视频投稿：使用ffmpeg获取视频封面，并将视频与封面保存项目路径下，将视频与封面信息存入数据库
* 发布列表获取：根据用户id从数据库中查询改用户发布过的视频，如果redis中有该用户发布列表，优先从redis中获取
* 点赞：点赞操作将会被传送到消息队列中，由消费者将其写入数据库
* 点赞列表：从数据库中查询用户点赞过的视频
* 评论：评论操作将会被传送到消息队列中，由消费者将其写入数据库
* 评论列表：根据视频id从数据库中查询该视频的评论，如果redis中有该用视频评论，优先从redis中获取

接口详情可以参阅 [字节跳动青训营：抖音项目](https://bytedance.feishu.cn/docx/doxcnbgkMy2J0Y3E6ihqrvtHXPg)

# 3、使用技术介绍

* 语言：java

* 使用框架：SpringBoot、SpringMVC、MybatisPlus
* 中间件：ActiveMQ,redis
* 数据库：MySQL
* 其他：ffmpeg、protobuf

# 4、展望

* 多线程：部分操作可以通过多线程进行优化
* 分布式：在高并发环境下，需要搭建分布式服务来保证稳定性、时效性等
* 推荐算法：本项目中仅使用最新视频推荐，通过大数据分析能够实现用户视频推荐的定制化，因此，项目中也保留了Strategy接口以供拓展
* 微服务：可以通过服务拆分，结合分布式系统，实现服务的解耦以及颗粒化

# 5、致谢

[字节跳动后端青训营](https://youthcamp.bytedance.com/)

# 