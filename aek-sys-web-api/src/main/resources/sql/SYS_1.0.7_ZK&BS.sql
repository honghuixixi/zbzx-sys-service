#插入转科、报损权限码
#112
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('0', '4', '转科管理', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#113
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('112', '4', '查看转科列表', 'ASS_ASSETS_ZK_LIST', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#114
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('112', '4', '申请转科', 'ASS_ASSETS_ZK_APPLY', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#115
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('112', '4', '审核转科', 'ASS_ASSETS_ZK_CHECK', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);

#116
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('0', '4', '报损管理', NULL, b'1', NULL, NULL, now(), now(), b'1', NULL, NULL);
#117
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('116', '4', '查看报损列表', 'ASS_ASSETS_DISCARD_LIST', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#118
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('116', '4', '申请报损', 'ASS_ASSETS_DISCARD_APPLY', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);
#119
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
VALUES ('116', '4', '审核报损', 'ASS_ASSETS_DISCARD_CHECK', b'0', NULL, NULL, now(), now(), b'1', NULL, NULL);


#112~119
call add_role_permission(4,112);
call add_role_permission_preset2(2,4,112);
call add_role_permission_preset2(1,4,112);
call add_role_permission(4,113);
call add_role_permission_preset2(2,4,113);
call add_role_permission_preset2(1,4,113);
call add_role_permission(4,114);
call add_role_permission_preset2(2,4,114);
call add_role_permission_preset2(1,4,114);
call add_role_permission(4,115);
call add_role_permission_preset2(2,4,115);
call add_role_permission_preset2(1,4,115);
call add_role_permission(4,116);
call add_role_permission_preset2(2,4,116);
call add_role_permission_preset2(1,4,116);
call add_role_permission(4,117);
call add_role_permission_preset2(2,4,117);
call add_role_permission_preset2(1,4,117);
call add_role_permission(4,118);
call add_role_permission_preset2(2,4,118);
call add_role_permission_preset2(1,4,118);
call add_role_permission(4,119);
call add_role_permission_preset2(2,4,119);
call add_role_permission_preset2(1,4,119);

#7.给资产管理模块添加权限时，需给预设角色机构管理员及资产管理员角色赋予该权限且启用。
update sys_role_permission_preset set del_flag=false,enable=true where preset_role_id in(1,2) and module_id=4 and permission_id in(112,113,114,115,116,117,118,119) ;
commit;

#8.预设角色为机构管理员与资产管理员的角色权限，需启用新增权限
update sys_role_permission set del_flag=false,enable=true where module_id=4 and permission_id in(112,113,114,115,116,117,118,119) and role_id in (select id from sys_role where preset_id in (1,2));
commit;


#######################清理预设角色对应的无用权限数据####################################
#资产模块管理员
delete from sys_role_permission_preset where  preset_role_id=2 and module_id !=4  order by module_id;
#维修管理管理员
delete from sys_role_permission_preset where  preset_role_id=3 and module_id !=5  order by module_id;
#巡检管理员
delete  from sys_role_permission_preset where  preset_role_id=4 and module_id !=6  order by module_id;
#用户管理员
delete  from sys_role_permission_preset where  preset_role_id=5 and module_id !=1  order by module_id;
#角色管理员
delete  from sys_role_permission_preset where  preset_role_id=6 and module_id !=2  order by module_id;
#监管机构管理员
delete  from sys_role_permission_preset where  preset_role_id=7 and module_id !=12  order by module_id;
#监管机构管理员
delete  from sys_role_permission_preset where  preset_role_id=8 and module_id !=3  order by module_id;
#PM模块管理员
delete from sys_role_permission_preset where  preset_role_id=9 and module_id !=7  order by module_id;
#字典管理管理员
delete  from sys_role_permission_preset where  preset_role_id=10 and module_id !=8  order by module_id;
#供应商管理管理员
delete  from sys_role_permission_preset where  preset_role_id=11 and module_id !=10  order by module_id;





