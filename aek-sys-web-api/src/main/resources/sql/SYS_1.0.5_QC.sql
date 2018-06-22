#添加巡检模块
#1.更新之前的质控管理为巡检管理，添加描述信息，启用未删除，并指定爱怡康，医疗机构可拥有巡检模块
update sys_module set `enable`=true ,name='巡检管理', url='inspection.plan.list',custom_data='{"tenantType": [0,1], "defaultExist": false}',description='巡检流程精细化管理' where id=6;
commit;

#2.更新巡检管理预设角色数据										
update sys_role_preset set name='巡检管理模块管理员',enable=true,descript='巡检管理模块所有可用权限' where id=4;
commit;

#2.删除巡检模块管理相应模块数据
delete from sys_role_permission_preset where preset_role_id=4;
delete from sys_role_permission where module_id=6;
delete from sys_role_user where role_id in(select id from sys_role where preset_id=4);
delete from sys_role where preset_id=4;
delete from sys_tenant_module where module_id=6;
commit;

#3.调用存储过程，初始化机构与巡检管理模块关系
call add_module_procedure(6);

#4.插入巡检计划菜单及其权限
#巡检计划ID=86
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '6', '巡检计划', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#87
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('86', '6', '查看巡检计划', 'QC_PLAN_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#88
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('86', '6', '新建巡检计划', 'QC_PLAN_NEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#89
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('86', '6', '停用巡检计划', 'QC_PLAN_DISABLE', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#巡检实施ID=90
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '6', '巡检实施', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#91
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('90', '6', '巡检实施', 'QC_PLAN_IMPLEMENT', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#巡检模板管理ID=92
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('0', '6', '巡检模板管理', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#93
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('92', '6', '查看模板', 'QC_TEMPLATE_VIEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#94
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('92', '6', '新建模板', 'QC_TEMPLATE_NEW', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#95
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('92', '6', '编辑模板', 'QC_TEMPLATE_EDIT', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#96
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('92', '6', '停用/启用模板', 'QC_TEMPLATE_DISABLE_ENABLE', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#97
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) VALUES ('92', '6', '删除模板', 'QC_TEMPLATE_DELETE', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#86~97
#5.（1）给所有拥有该模块的机构角色赋予新增权限，默认不勾选，（2）给预设角色添加新增权限
#5.执行请求接口:调用之前先得插入预设角色 
#1.http://localhost:8090/sys/permission/add/6/86
#2.http://localhost:8090/sys/permission/add/6/87
#3.http://localhost:8090/sys/permission/add/6/88
#4.http://localhost:8090/sys/permission/add/6/89
#5.http://localhost:8090/sys/permission/add/6/90
#6.http://localhost:8090/sys/permission/add/6/91
#7.http://localhost:8090/sys/permission/add/6/92
#8.http://localhost:8090/sys/permission/add/6/93
#9.http://localhost:8090/sys/permission/add/6/94
#10.http://localhost:8090/sys/permission/add/6/95
#11.http://localhost:8090/sys/permission/add/6/96
#12.http://localhost:8090/sys/permission/add/6/97

call add_role_permission(6,86);
call add_role_permission_preset(6,86);
call add_role_permission(6,87);
call add_role_permission_preset(6,87);
call add_role_permission(6,88);
call add_role_permission_preset(6,88);
call add_role_permission(6,89);
call add_role_permission_preset(6,89);
call add_role_permission(6,90);
call add_role_permission_preset(6,90);
call add_role_permission(6,91);
call add_role_permission_preset(6,91);
call add_role_permission(6,92);
call add_role_permission_preset(6,92);
call add_role_permission(6,93);
call add_role_permission_preset(6,93);
call add_role_permission(6,94);
call add_role_permission_preset(6,94);
call add_role_permission(6,95);
call add_role_permission_preset(6,95);
call add_role_permission(6,96);
call add_role_permission_preset(6,96);
call add_role_permission(6,97);
call add_role_permission_preset(6,97);

#6.调用存储过程，前提：先插入预设角色信息以及给预设角色分配权限，预设角色ID=4
call add_preset_role_procedure_01(4,'');

#7.给巡检管理模块添加权限时，需给预设角色机构管理员及巡检管理员角色赋予该权限且启用。
select * from sys_role_permission_preset where preset_role_id in(1,4) and module_id=6 and permission_id in(86,87,88,89,90,91,92,93,94,95,96,97) ;

#8.预设角色为机构管理员与维修管理员的角色权限，需启用新增权限
select * from sys_role_permission where  module_id=6 and permission_id in(86,87,88,89,90,91,92,93,94,95,96,97) and role_id in (select id from sys_role where preset_id in (1,4));






