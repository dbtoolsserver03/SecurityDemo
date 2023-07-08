create database `security` character set utf8mb4;

 -- 用户表 共有多个用户
 CREATE TABLE `user`
 (
     `id`                    int(11) NOT NULL AUTO_INCREMENT,
     `username`              varchar(32)  DEFAULT NULL,
     `password`              varchar(255) DEFAULT NULL,
     `enabled`               tinyint(1) DEFAULT NULL,
     `accountNonExpired`     tinyint(1) DEFAULT NULL,
     `accountNonLocked`      tinyint(1) DEFAULT NULL,
     `credentialsNonExpired` tinyint(1) DEFAULT NULL,
     PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
 ​
 -- 角色表 共有多个角色，其中，_zh表示中文
 CREATE TABLE `role`
 (
     `id`      int(11) NOT NULL AUTO_INCREMENT,
     `name`    varchar(32) DEFAULT NULL,
     `name_zh` varchar(32) DEFAULT NULL,
     PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

 -- 用户角色关系表：用户与角色为多对多关系，需要建立中间表
 CREATE TABLE `user_role`
 (
     `id`  int(11) NOT NULL AUTO_INCREMENT,
     `uid` int(11) DEFAULT NULL,
     `rid` int(11) DEFAULT NULL,
     PRIMARY KEY (`id`),
     KEY   `uid` (`uid`),
     KEY   `rid` (`rid`)
 ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;







 -- 插入用户数据
 BEGIN;
   INSERT INTO `user`
   VALUES (1, 'root', '{noop}123', 1, 1, 1, 1);
   INSERT INTO `user`
   VALUES (2, 'admin', '{noop}123', 1, 1, 1, 1);
   INSERT INTO `user`
   VALUES (3, 'blr', '{noop}123', 1, 1, 1, 1);
 COMMIT;
 ​
 -- 插入角色数据 授权时ROLE_进行处理，RBAC
 BEGIN;
   INSERT INTO `role`
   VALUES (1, 'ROLE_product', '商品管理员');
   INSERT INTO `role`
   VALUES (2, 'ROLE_admin', '系统管理员');
   INSERT INTO `role`
   VALUES (3, 'ROLE_user', '用户管理员');
 COMMIT;
 ​
 -- 插入用户角色数据
 BEGIN;
   INSERT INTO `user_role`
   VALUES (1, 1, 1);
   INSERT INTO `user_role`
   VALUES (2, 1, 2);
   INSERT INTO `user_role`
   VALUES (3, 2, 2);
   INSERT INTO `user_role`
   VALUES (4, 3, 3);
 COMMIT;












