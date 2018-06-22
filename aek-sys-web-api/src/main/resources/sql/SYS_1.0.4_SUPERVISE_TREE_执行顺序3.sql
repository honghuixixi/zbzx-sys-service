#1.创建监管树模块
INSERT INTO `sys_module` (`name`, `version_number`, `module_type`, `url`, `price_policy_id`, `create_by`, `create_time`, `update_by`, `update_time`, `enable`, `del_flag`, `custom_data`, `description`) 
VALUES ('监管树', '1.0.0', '1', 'supervisetree', NULL, '1', now(), '1', now(), b'1', b'0', '{\"tenantType\": [0, 2], \"defaultExist\": false}', '省市区监管、医疗机构信息汇总查询');

#2.删除监管树模块之前数据
delete from sys_tenant_module where module_id=13;
commit;

#3.调用存储过程，初始化机构与监管树模块关系sys_tenant_module
call add_module_procedure(13);
											
											