#1.创建供应商证件表
CREATE TABLE `sys_tenant_credentials` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `type` int(11) DEFAULT NULL COMMENT '证件类型[1=组织机构代码证,2=营业执照证,3=医疗器械经营许可证,4=税务登记证]',
  `code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '证件编号',
  `image_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL COMMENT '有效时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='供应商证件表';



#2.更新模块表中 供应商可拥有成员用户及角色管理模块
update sys_module set custom_data='{"tenantType": [0, 1, 2, 3], "defaultExist": true}' where id=1 or id=2;
commit;

#3.更新供应商模块描述及指定哪些类型的机构可以拥有该模块
update sys_module set `enable`=true , custom_data='{"tenantType": [0], "defaultExist": false}',description='供应商信息集中管理',url='supplier.list' where id=10;
commit;

#4.删除之前数据
delete from sys_tenant_module where module_id=10;
commit;

#5.调用存储过程，初始化机构模块关系
call add_module_procedure(10);

#6.添加供应商管理权限
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`,  `enable`,  `description`, `custom_data`) 
											VALUES ('0', '10', '查看供应商列表', 'SYS_SUPPLIER_TENANT_LIST_VIEW', b'0', NULL, NULL, now(), now(), b'1',  NULL, NULL);
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`,  `enable`, `description`, `custom_data`) 
											VALUES ('0', '10', '查看供应商详情', 'SYS_SUPPLIER_TENANT_DETAILED_VIEW', b'0', NULL, NULL, now(),now(),  b'1',  NULL, NULL);
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`,  `enable`, `description`, `custom_data`) 
											VALUES ('0', '10', '新建供应商信息', 'SYS_SUPPLIER_TENANT_NEW', b'0', NULL, NULL, now(), now(), b'1',  NULL, NULL);
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`,  `update_time`,  `enable`,  `description`, `custom_data`) 
											VALUES ('0', '10', '管理供应商信息', 'SYS_SUPPLIER_TENANT_MANAGE', b'0', NULL, NULL, now(), now(),  b'1', NULL, NULL);
INSERT INTO `sys_permission` (`parent_id`, `module_id`, `name`, `code`, `menu_flag`, `url`, `target`, `create_time`, `update_time`, `enable`, `description`, `custom_data`) 
											VALUES ('0', '10', '审核供应商', 'SYS_SUPPLIER_TENANT_VERIFY', b'0', NULL, NULL, now(), now(), b'1',  NULL, NULL);

#7.添加供应商管理员预设角色
INSERT INTO `sys_role_preset` (`name`, `code`, `data_scope`, `descript`, `tenant_type`, `create_by`, `create_time`, `update_by`, `update_time`, `enable`, `del_flag`, `remark`) 
VALUES ('供应商模块管理员', NULL, '1', '供应商模块所有可用权限', '0,3', NULL, now(), NULL, now(), b'1', b'0', 'AEK,供应商所拥有');

#8.更新机构预设管理员角色可用的机构类型加上供应商
update sys_role_preset set tenant_type='0,1,2,3' where id=1;
commit;

#9.执行请求接口:调用之前先得插入预设角色 
#1.http://localhost:8090/sys/permission/add/10/78
#2.http://localhost:8090/sys/permission/add/10/79
#3.http://localhost:8090/sys/permission/add/10/80	
#4.http://localhost:8090/sys/permission/add/10/81	
#5.http://localhost:8090/sys/permission/add/10/82

#10.创建存储过程
CREATE PROCEDURE `add_preset_role_procedure_03`(IN `input_preset_role_id` bigint,IN input_tenant_type varchar(50))
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

	DECLARE rs_cursor CURSOR FOR(select id from sys_tenant where tenant_type in(0,3));

	
	
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
							#select preset_role_enable,preset_role_del_flag;
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

#11.调用存储过程，前提：先插入预设角色信息以及给预设角色分配权限
call add_preset_role_procedure_03(11,'');

#12.给维修模块添加权限时，需给预设角色机构管理员及维修管理员角色赋予该权限且启用。
select * from sys_role_permission_preset where preset_role_id in(1,11) and module_id=10 and permission_id in(78,79,80,81,82) ;

#13.预设角色为机构管理员与维修管理员的角色权限，需启用新增权限
select * from sys_role_permission where  module_id=10 and permission_id in(78,79,80,81,82) and role_id in (select id from sys_role where preset_id in (1,11));
											
											
											