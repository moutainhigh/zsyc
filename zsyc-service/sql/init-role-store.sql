/**
将所有店铺赋予一角色
 */
set @role_id=1;
delete from sys_role_store where role_id = @role_id;
insert into sys_role_store(store_id,role_id,create_time)
select id store_id, @role_id role_id, now() create_time from store_info;