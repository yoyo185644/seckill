--数据库初始化脚本
--创建数据库
CREATE DATABASE seckill;
--使用数据库
use seckill;
--创建秒杀库存表
CREATE TABLE seckill(
    `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    `name` varchar(120) NOT NULL  COMMENT ' 商品名称 ',
    `number` int NOT NULL  COMMENT ' 库存数量 ',
    `start_time` timestamp NOT NULL COMMENT ' 秒杀开启时间 ',
    `end_time` timestamp NOT NULL COMMENT ' 秒杀结束时间 ',
    `create_time` timestamp NOT NULL DEFAULT  CURRENT_TIMESTAMP  COMMENT ' 创建时间 ',
    PRIMARY KEY (seckill_id),
    key idx_start_time(start_time),
    key idx_end_time(end_time),
    key idx_cereate_time(create_time)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET=utf8 COMMENT="秒杀库存表";

--初始化数据
insert into seckill(name,number,start_time,end_time)
values
    ('100元秒杀iPhone6',100,'2020-03-28 00:00:00','2020-03-29 00:00:00'),
    ('100元秒杀iPhone7',200,'2020-03-28 00:00:00','2020-03-29 00:00:00'),
    ('100元秒杀iPhone8',300,'2020-03-28 00:00:00','2020-03-29 00:00:00'),
    ('100元秒杀iPhoneX',400,'2020-03-28 00:00:00','2020-03-29 00:00:00'),
    ('100元秒杀iPhoneXS',500,'2020-03-28 00:00:00','2020-03-29 00:00:00');

--秒杀成功明细表
create table success_killed(
    `seckill_id` bigint NOT NULL  COMMENT '秒杀商品ID',
    `user_phone` bigint NOT NULL  COMMENT '用户手机号',
    `state` tinyint NOT NULL DEFAULT  -1 COMMENT '状态标志：-1：无效 0：成功 1：已付款',
    `create_time` timestamp NOT NULL DEFAULT  CURRENT_TIMESTAMP  COMMENT ' 创建时间 ',
    PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
    KEY idx_create_time(create_time)
)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT='秒杀成功明细表';

--连接数据库控制台
mysql -uroot -p77777777

--为什么手写DDL
--记录每次的上线DDL
--上线V1.1
ALTER TABLE seckill;
DROP INDEX idx_start_time;

--上线V1.2
insert into seckill(name,number,start_time,end_time)
values
    ('100元秒杀ViVO',100,'2020-03-31 00:00:00','2020-04-01 00:00:00'),
    ('100元秒杀ViVO',100,'2020-04-01 00:00:00','2020-04-02 00:00:00'),
    ('100元秒杀OPPO',200,'2020-04-02 00:00:00','2020-04-03 00:00:00');
