#修改机构表，添加当前机构与上级行政机构建立关系的时间
alter table sys_tenant add manage_tenant_time datetime COMMENT '机构与上级行政单位建立关系的时间'  after manage_tenant_id;
UPDATE sys_tenant SET manage_tenant_time = create_time;
COMMIT;


#添加问卷模块sql
INSERT INTO `sys_module` 
(`id`, `name`, `version_number`, `module_type`, `url`, `price_policy_id`, `create_by`, `create_time`, `update_by`, `update_time`,`enable`, `del_flag`, `custom_data`, `description`) VALUES 
('13', '问卷调查', '1.0.0', '2', 'http://feat.aek.com:8080/diaowen/', NULL, '1', now(), '1', now(), true, false, '{\"tenantType\": [0, 1, 2], \"defaultExist\": false}', '实用、强大的问卷调查系统');
COMMIT;

#创建处理字符串分隔，获取分隔后数量
SET GLOBAL log_bin_trust_function_creators=TRUE;
CREATE FUNCTION `splitResultNum`(`inputStr` varchar(1000),`seperatorStr` varchar(50)) RETURNS int(100)
BEGIN
	#####返回替换前长度减去用''替换后的长度得到seperatorStr的个数。
	return (length(inputStr) - length(replace(inputStr,seperatorStr,'')))+1; 
END;

#创建添加模块存储过程，解决新增模块相关数据处理
CREATE PROCEDURE `add_module_procedure`(IN moduleId BIGINT)
BEGIN

	DECLARE _id bigint;
  DECLARE _name VARCHAR(20);
	DECLARE _version_number varchar(20);
  DECLARE _module_type tinyint(4);
  DECLARE _url varchar(50);
	DECLARE _price_policy_id bigint(20);
	DECLARE _create_by bigint(20);
	DECLARE _create_time datetime;
	DECLARE _update_by bigint(20);
	DECLARE _update_time datetime;
	DECLARE _enable bit(1);
	DECLARE _del_flag bit(1);
	DECLARE _custom_data json;
	DECLARE _description varchar(255);
	DECLARE _defaultExist varchar(20);
  DECLARE _tenantType varchar(20);
	DECLARE _delflag BIT(1);
	DECLARE _isEnable BIT(1);

	DECLARE i INT(8) DEFAULT 0;

	DECLARE tenantTypeNum INT(8) DEFAULT 0;

	DECLARE temp_tenant_type VARCHAR(10);
	
	#获取新建模块数据
	SELECT id,name,version_number,module_type,url,price_policy_id,create_by,create_time,update_by,update_time,`enable`,del_flag,custom_data,description 
	 INTO _id,_name,_version_number,_module_type,_url,_price_policy_id,_create_by,_create_time,_update_by,_update_time,_enable,_del_flag,_custom_data,_description
	FROM sys_module WHERE id=moduleId;
	
	
	SELECT REPLACE(REPLACE(json_extract(_custom_data,'$.tenantType'),'[',''),']','') INTO _tenantType;
	SELECT json_extract(_custom_data,'$.defaultExist') INTO _defaultExist;
	SELECT splitResultNum(_tenantType,',') INTO tenantTypeNum;

	IF _defaultExist='true'
	THEN
		SET _delflag=false;
		SET _isEnable=true;
	ELSE
		SET _delflag=true;
		SET _isEnable=false;
	END IF;
	
	SELECT _defaultExist,_delflag,_tenantType,tenantTypeNum;

	START TRANSACTION;
  REPEAT SET i = i + 1; 
	  SET temp_tenant_type = SUBSTRING_INDEX(SUBSTRING_INDEX(_tenantType, ',', i), ',', -1);
		BEGIN
			DECLARE done INT DEFAULT 0;
			DECLARE tenantId bigint;
			DECLARE rs_cursor CURSOR FOR( SELECT id FROM sys_tenant WHERE tenant_type=temp_tenant_type);
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
			OPEN rs_cursor;
				rs_loop:LOOP
					FETCH rs_cursor into tenantId;
					IF done = 1 THEN  
				    LEAVE rs_loop;  
					END IF; 
					INSERT INTO sys_tenant_module(tenant_id,module_id,name,version_number,module_type,url,release_time,module_source,create_time,update_time,enable,del_flag,description)
																	VALUES(tenantId,_id,_name,_version_number,_module_type,_url,now(),1,now(),now(),_isEnable,_delflag,_description);
          SET done = 0;
				END LOOP rs_loop;
			CLOSE rs_cursor;
		END;
  UNTIL i >= tenantTypeNum  END REPEAT;
	COMMIT;
END;

#调用存储过程处理数据
CALL add_module_procedure(13);