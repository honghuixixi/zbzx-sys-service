#用户表增加登录名
ALTER TABLE `sys_user` ADD COLUMN `login_name` varchar(40) DEFAULT NULL COMMENT '登录名' AFTER `registration_ip`; 

 