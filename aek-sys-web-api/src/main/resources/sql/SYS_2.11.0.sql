
#1.添加维修配置，报价申请，报价审批菜单及权限

#122
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '5', '维修配置', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#123
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('122', '5', '维修配置', 'REP_REPAIR_CONFIG', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#124
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '5', '工作流配置', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);

#125
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '5', '报价申请', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#126
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('125', '5', '报价申请', 'REP_REPAIR_PRICE_APPLY', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#127
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '5', '报价审批', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#128
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('127', '5', '报价审批', 'REP_REPAIR_PRICE_CHECK', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);


#2.
call add_role_permission(5,122);
call add_role_permission_preset(5,122);
call add_role_permission(5,123);
call add_role_permission_preset(5,123);
call add_role_permission(5,124);
call add_role_permission_preset(5,124);
call add_role_permission(5,125);
call add_role_permission_preset(5,125);
call add_role_permission(5,126);
call add_role_permission_preset(5,126);
call add_role_permission(5,127);
call add_role_permission_preset(5,127);
call add_role_permission(5,128);
call add_role_permission_preset(5,128);


#3.给巡检管理模块添加权限时，需给预设角色机构管理员及巡检管理员角色赋予该权限且启用。
select * from sys_role_permission_preset where preset_role_id in(1,3) and module_id=5 and permission_id in(122,123,124,125,126,127,128) ;

#4.预设角色为机构管理员与维修管理员的角色权限，需启用新增权限
select * from sys_role_permission where  module_id=5 and permission_id in(122,123,124,125,126,127,128) and role_id in (select id from sys_role where preset_id in (1,3));
