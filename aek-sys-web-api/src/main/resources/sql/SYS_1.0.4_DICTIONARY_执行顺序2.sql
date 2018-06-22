
#==========================创建字典表结构，初始化数据====================================
DROP TABLE IF EXISTS `base_dict`;
CREATE TABLE `base_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '字典父ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字典表名称',
  `value` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '默认值',
  `cascade_status` tinyint(4) DEFAULT NULL COMMENT '级联状态(1=级联，2=独立)',
  `manage_type` tinyint(4) DEFAULT NULL COMMENT '管理类型(1=基础，2=自定义)',
  `depend_type` tinyint(4) DEFAULT NULL COMMENT '依赖类型(1=系统依赖，2=自定义依赖，3=无依赖)',
  `del_flag` bit(1) DEFAULT NULL COMMENT '删除标记(0=未删除，1=删除)',
  `enable` bit(1) DEFAULT NULL COMMENT '启用标记(0=禁用，1=启用)',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

DROP TABLE IF EXISTS `base_dict_value`;
CREATE TABLE `base_dict_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_id` bigint(20) DEFAULT NULL COMMENT '字典表ID',
  `cascade_dict_id` bigint(20) DEFAULT NULL COMMENT '级联字典ID，只有当前字典状态为可级联才存在',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字典值',
  `def_flag` bit(1) DEFAULT NULL COMMENT '是否默认(1=是 /0=否)',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字段值表';

#插入系统依赖字典
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '是否', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '单位', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '账簿类型', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '固定资产核算类别', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '无形资产核算类别', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '管理级别', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '计量类别', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '用途', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);
INSERT INTO `base_dict` (`parent_id`, `tenant_id`, `name`, `value`, `cascade_status`, `manage_type`, `depend_type`, `del_flag`, `enable`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
 VALUES (NULL, NULL, '设备来源', NULL, 2, 1, 1, false, true, NULL, now(), NULL, now(),NULL);

#==========================创建字典表结构，初始化数据====================================


#1.更新字典表管理模块描述及指定哪些类型的机构可以拥有该模块
update sys_module set `enable`=true , custom_data='{"tenantType": [0,1,2,3], "defaultExist": false}',url='dictionary.menu.table',description='字典表信息集中管理' where id=8;
commit;

#2.删除字典表管理相应模块数据
delete from sys_role_permission_preset where preset_role_id=10;
delete from sys_role_permission where module_id=8;
delete from sys_role_user where role_id in(select id from sys_role where preset_id=10);
delete from sys_role where preset_id=10;
delete from sys_tenant_module where module_id=8;
commit;

#3.调用存储过程，初始化机构模块关系
call add_module_procedure(8);



#4.更新字典表管理预设角色数据										
update sys_role_preset set tenant_type='0,1,2,3',enable=true,remark='所有机构类型共有' where id=10;
commit;

#6.添加字典表管理权限
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`,  `enable`,  `description`, `custom_data`) 
											VALUES ('0', '8', '查看字典表', 'SYS_DICT_LIST_VIEW', b'0', NULL, NULL, now(), now(), b'1',  '包含查看字典表及字典表对应的字典值', NULL);
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`,  `enable`, `description`, `custom_data`) 
											VALUES ('0', '8', '管理字典表', 'SYS_DICT_MANAGE', b'0', NULL, NULL, now(),now(),  b'1',  '包含查看、新建、管理、编辑、删除字典表及字典值', NULL);
										
#7.（1）给所有拥有该模块的机构角色赋予新增权限，默认不勾选，（2）给预设角色添加新增权限
#7.执行请求接口:调用之前先得插入预设角色 
#1.http://localhost:8090/sys/permission/add/8/83
#2.http://localhost:8090/sys/permission/add/8/84

#8.调用存储过程，前提：先插入预设角色信息以及给预设角色分配权限
CREATE PROCEDURE `add_preset_role_procedure_0123`(IN `input_preset_role_id` bigint,IN input_tenant_type varchar(50))
BEGIN

	DECLARE _id bigint;
  DECLARE _name VARCHAR(50);
	DECLARE _code varchar(50);
  DECLARE _data_scope tinyint(4);
  DECLARE _descript varchar(200);
	DECLARE _enable bit(1);
	DECLARE _del_flag bit(1);
  DECLARE _tenant_type varchar(20);
	
	DECLARE done INT DEFAULT 0;
	DECLARE tenantId bigint;
	DECLARE roleId bigint;

	DECLARE preset_role_permission_id bigint;
	DECLARE preset_role_module_id bigint;
	DECLARE preset_role_enable bit(1);
	DECLARE preset_role_del_flag bit(1);

	
	DECLARE rs_cursor CURSOR FOR(select id from sys_tenant where tenant_type in(0,1,2,3));
	
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	START TRANSACTION;
	
	#获取新增预设角色数据
	SELECT id,name,code,data_scope,descript,tenant_type,`enable`,`del_flag`  
	INTO _id,_name,_code,_data_scope,_descript,_tenant_type,_enable,_del_flag
	FROM sys_role_preset WHERE id=input_preset_role_id;

	OPEN rs_cursor;
		rs_loop:LOOP
			FETCH rs_cursor into tenantId;
			IF done = 1 THEN  
				LEAVE rs_loop;  
			END IF; 
			select tenantId;	
			#------------------------------------------------------------------------
			#插入数据到角色表中
			#------------------------------------------------------------------------
			INSERT INTO sys_role(`tenant_id`, `name`, `code`, `data_scope`, `preset_id`, `descript`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `enable`, `custom_data`) 
										VALUES (tenantId, _name, _code, _data_scope, _id, _descript, 1, now(), NULL, now(), _del_flag, _enable, NULL);
			
			select id into roleId from sys_role where name=_name and tenant_id=tenantId and preset_id=_id limit 1;
			
			#------------------------------------------------------------------------
			#插入数据到角色权限表中
			#从sys_role_permission_preset表中查询出当前预设角色所拥有的权限
			#------------------------------------------------------------------------
			BEGIN
					DECLARE rs_cursor_2 CURSOR FOR(select permission_id,module_id,`enable`,`del_flag` from sys_role_permission_preset where preset_role_id=_id);
					OPEN rs_cursor_2;
						rs_loop1:LOOP
							FETCH rs_cursor_2 into preset_role_permission_id,preset_role_module_id,preset_role_enable,preset_role_del_flag;
							IF done = 1 THEN  
								LEAVE rs_loop1;  
							END IF; 
							INSERT INTO sys_role_permission(`tenant_id`, `role_id`, `permission_id`, `module_id`, `del_flag`, `enable`, `create_time`, `update_time`) 
											VALUES (tenantId, roleId, preset_role_permission_id, preset_role_module_id, preset_role_del_flag, preset_role_enable, now(), now());
							set done = 0;
						END LOOP rs_loop1;
					CLOSE rs_cursor_2;
			END;
			
			SET done = 0;
		END LOOP rs_loop;
	CLOSE rs_cursor;

	COMMIT;

END

call add_preset_role_procedure_0123(10,'');

#12.给字典管理模块添加权限时，需给预设角色机构管理员及字典管理员角色赋予该权限且启用。
select * from sys_role_permission_preset where preset_role_id in(1,10) and module_id=8 and permission_id in(83,84) ;

#13.预设角色为机构管理员与维修管理员的角色权限，需启用新增权限
select * from sys_role_permission where  module_id=8 and permission_id in(83,84) and role_id in (select id from sys_role where preset_id in (1,10));
											
											
											