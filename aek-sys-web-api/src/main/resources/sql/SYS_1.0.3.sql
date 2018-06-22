#新增打印申请单权限
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
                      VALUES ('26', '5', '打印申请单', 'REP_APPLY_PRINT_NEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#新增接单权限
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
                      VALUES ('26', '5', '接单', 'REP_APPLY_TAKE_NEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);


#查看申请/接单/验收单详情
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
                      VALUES ('26', '5', '查看申请/接单/验收单详情', 'REP_APPLY_TAKE_CHECK_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#执行请求接口
#1.http://localhost:8090/sys/permission/add/5/75
#2.http://localhost:8090/sys/permission/add/5/76
#3.http://localhost:8090/sys/permission/add/5/77

#1.给维修模块添加权限时，需给预设角色机构管理员及维修管理员角色赋予该权限且启用。
select * from sys_role_permission_preset where preset_role_id in(1,3) and module_id=5 and permission_id in(75,76,77) ;

#2.预设角色为机构管理员与维修管理员的角色权限，需启用新增权限
select * from sys_role_permission where  module_id=5 and permission_id in(75,76,77) and role_id in (select id from sys_role where preset_id in (1,3));