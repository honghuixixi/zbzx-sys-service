
 #添加管理机构ID字段
ALTER TABLE `sys_tenant`
ADD COLUMN `manage_tenant_id`  bigint(20) NULL DEFAULT NULL COMMENT '管理机构ID，租户类型为监管' AFTER `parent_ids`;


#新增[新增资产台账]权限码
INSERT INTO `sys_permission` (`parent_id`,`module_id`,`name`,`code`,`menu_flag`,`url`,`target`,`create_time`,`update_time`,`enable`,`description`,`custom_data`)
VALUES (14,4,'新增资产台账','ASS_ASSETS_NEW',false,NULL,NULL,now(),now(),true,NULL,NULL);

#新增监管机构
INSERT INTO `sys_tenant` (`name`,`license`,`license_img_url`,`province`,`city`,`county`,
	`parent_id`,`parent_ids`,`manage_tenant_id`,`commercial_use`,`trial`,`tenant_type`,`sub_tenant_limit`,`sub_tenant`,
	`user_limit`,`expire_time`,`logo`,`origin`,`audit_status`,`admin_id`,`notify`,`token_max_expire`,`create_by`,`create_time`,`update_by`,
	`update_time`,`enable`,`del_flag`,`custom_data`
) VALUES ('国资委','',NULL,'北京','北京市','朝阳区','1','0,1','1','0','0','2','0','0','0',NULL,NULL,'1',
		'2','2',true,NULL,'1',now(),'1',now(),true,false,'{}');
		
#将机构表租户类型添加系统机构类型
ALTER TABLE sys_tenant  MODIFY COLUMN tenant_type tinyint(4) COMMENT '租户类型[0=AEK系统,1=医疗机构,2=监管机构,3=设备供应商,4=设备维修商,5=配件供应商]';
		


#预设角色新增字段，预设角色机构类型，某些预设角色不是所有机构共有
ALTER TABLE `sys_role_preset`
ADD COLUMN `tenant_type`  varchar(20) NULL COMMENT '当前预设角色所赋予的机构类型 以逗号间隔\"1,3,4\"' AFTER `descript`;

#模块添加描述
alter table sys_module add description varchar(255) DEFAULT NULL COMMENT '描述';
commit;

alter table sys_tenant_module add description varchar(255) DEFAULT NULL COMMENT '描述';
commit;


update sys_tenant_module set  description='轻松管理部门、成员信息' where module_id=1;
update sys_tenant_module set  description='角色权限尽在掌握' where module_id=2;
update sys_tenant_module set  description='可对下级医疗机构进行集中管理' where module_id=3;
update sys_tenant_module set  description='对设备进行全生命周期管理' where module_id=4;
update sys_tenant_module set  description='维修流程精细可视化管理' where module_id=5;
update sys_tenant_module set  description='可对下级监管机构进行集中管理' where module_id=12;
commit;

ALTER TABLE `sys_user`
MODIFY COLUMN `creator_name`  varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称' AFTER `last_client_type`;



