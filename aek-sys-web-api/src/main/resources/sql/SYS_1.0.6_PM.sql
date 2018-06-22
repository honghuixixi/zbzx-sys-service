#添加PM管理模块
#1.更新之前的字段管理为PM管理，添加描述信息，启用未删除，并指定爱怡康，医疗机构可拥有PM管理模块
update sys_module set `enable`=true ,name='PM管理', url='pm.menu.plan',custom_data='{"tenantType": [0,1], "defaultExist": false}',description='PM流程精细化管理' where id=7;
commit;

#2.更新字段管理预设角色数据										
update sys_role_preset set name='PM管理模块管理员',enable=true,descript='PM管理模块所有可用权限' where id=9;
commit;

#2.删除原来字段管理模块管理相应模块数据
delete from sys_role_permission_preset where preset_role_id=9;
delete from sys_role_permission where module_id=7;
delete from sys_role_user where role_id in(select id from sys_role where preset_id=9);
delete from sys_role where preset_id=9;
delete from sys_tenant_module where module_id=7;
commit;

#3.调用存储过程，初始化机构与PM管理模块关系
call add_module_procedure(7);

#4.插入PM管理菜单及其权限
#PM计划ID=98
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '7', 'PM计划', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#99
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('98', '7', '查看PM计划', 'PM_PLAN_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#100
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('98', '7', '新建/编辑PM计划', 'PM_PLAN_NEW_EDIT', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#101
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('98', '7', '停用/启用PM计划', 'PM_PLAN_DISABLE_ENABLE', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#PM实施ID=102
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '7', 'PM实施', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#103
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('102', '7', 'PM实施', 'PM_PLAN_IMPLEMENT', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#PM模板管理ID=104
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '7', 'PM模板管理', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#105
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('104', '7', '查看模板', 'PM_TEMPLATE_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#106
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('104', '7', '新建模板', 'PM_TEMPLATE_NEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#107
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('104', '7', '编辑模板', 'PM_TEMPLATE_EDIT', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#108
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('104', '7', '停用/启用模板', 'PM_TEMPLATE_DISABLE_ENABLE', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#109
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('104', '7', '删除模板', 'PM_TEMPLATE_DELETE', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#PM报告查询110
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '7', 'PM报告', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#111
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('110', '7', '查看PM报告', 'PM_REPORT_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#98~111
#5.（1）给所有拥有该模块的机构角色赋予新增权限，默认不勾选，（2）给预设角色添加新增权限
call add_role_permission(7,98);
call add_role_permission_preset(7,98);
call add_role_permission(7,99);
call add_role_permission_preset(7,99);
call add_role_permission(7,100);
call add_role_permission_preset(7,100);
call add_role_permission(7,101);
call add_role_permission_preset(7,101);
call add_role_permission(7,102);
call add_role_permission_preset(7,102);
call add_role_permission(7,103);
call add_role_permission_preset(7,103);
call add_role_permission(7,104);
call add_role_permission_preset(7,104);
call add_role_permission(7,105);
call add_role_permission_preset(7,105);
call add_role_permission(7,106);
call add_role_permission_preset(7,106);
call add_role_permission(7,107);
call add_role_permission_preset(7,107);
call add_role_permission(7,108);
call add_role_permission_preset(7,108);
call add_role_permission(7,109);
call add_role_permission_preset(7,109);
call add_role_permission(7,110);
call add_role_permission_preset(7,110);
call add_role_permission(7,111);
call add_role_permission_preset(7,111);

#6.调用存储过程，前提：先插入预设角色信息以及给预设角色分配权限，预设角色ID=9
call add_preset_role_procedure_01(9,'');

#7.给PM管理模块添加权限时，需给预设角色机构管理员及PM管理员角色赋予该权限且启用。
update sys_role_permission_preset set del_flag=false,enable=true where preset_role_id in(1,9) and module_id=7 and permission_id in(98,99,100,101,102,103,104,105,106,107,108,109,110,111) ;
commit;

#8.预设角色为机构管理员与PM管理员的角色权限，需启用新增权限
update sys_role_permission set del_flag=false,enable=true where module_id=7 and permission_id in(98,99,100,101,102,103,104,105,106,107,108,109,110,111) and role_id in (select id from sys_role where preset_id in (1,9));
commit;

#9.默认将pm管理模块分配给爱怡康用户
update sys_tenant_module set enable=true,del_flag=false where module_id=7 and tenant_id=1;
commit;





