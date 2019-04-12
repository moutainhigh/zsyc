/**
将所有权限赋予一角色
 */
set @role_id=1;
delete from sys_role_permission where role_id = @role_id;
insert into sys_role_permission(permission_id,role_id,create_time)
select id permission_id, @role_id role_id, now() create_time from sys_permission;