#清洗数据库医疗类别
update sys_tenant set custom_data = JSON_SET(custom_data,'$.category',1)  where custom_data->'$.category' in(2,3,4,5,6,7,8,9,10,11,12,13,14,15);
commit;
#以上已更新到生产库
##############################################################################################