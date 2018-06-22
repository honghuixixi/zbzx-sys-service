
#1.添加巡检报告查询菜单及查看巡检报告权限
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '6', '巡检报告查询', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('120', '6', '查看巡检报告', 'QC_REPORT_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#2.
call add_role_permission(6,120);
call add_role_permission_preset(6,120);
call add_role_permission(6,121);
call add_role_permission_preset(6,121);


#3.给巡检管理模块添加权限时，需给预设角色机构管理员及巡检管理员角色赋予该权限且启用。
select * from sys_role_permission_preset where preset_role_id in(1,4) and module_id=6 and permission_id in(120,121) ;

#4.预设角色为机构管理员与维修管理员的角色权限，需启用新增权限
select * from sys_role_permission where  module_id=6 and permission_id in(120,121) and role_id in (select id from sys_role where preset_id in (1,4));
