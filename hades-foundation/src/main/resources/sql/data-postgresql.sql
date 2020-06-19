/*
 * Copyright (c) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Project Name: hades-platform
 * Module Name: hades-foundation
 * File Name: data-postgresql.sql
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

INSERT INTO "sys_user"("uid", "available", "state", "password", "salt", "user_name", "create_time", "update_time", "employeeid", "openid", "ranking", "reversion") VALUES ('2', 't', 0, '$2a$10$fi5ecIcM3hy9RQwE0x78oeyNecPFiUgi0PnhESeENjX3G4CBvYOLO', '429294af5ba02f51db734ee614c86b55', 'administrator', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "sys_user"("uid", "available", "state", "password", "salt", "user_name", "create_time", "update_time", "employeeid", "openid", "ranking", "reversion") VALUES ('1', 't', 1, '$2a$10$fi5ecIcM3hy9RQwE0x78oeyNecPFiUgi0PnhESeENjX3G4CBvYOLO', 'c37552aa1ff9164da4b97ff4c4a74eac', 'system', NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO sys_role(rid, available, state, description, role_name) VALUES ('1', 't', 0, '超级管理员', 'system');
INSERT INTO sys_role(rid, available, state, description, role_name) VALUES ('2', 't', 1, '系统管理员', 'administrator');

INSERT INTO sys_permission(pid, available, state, menu_class, permission, permission_name, resource_type, url, parent_id) VALUES ('1', 't', 1, 'home', 'dashboard', '仪表盘', 'menu', NULL, NULL);
INSERT INTO sys_permission(pid, available, state, menu_class, permission, permission_name, resource_type, url, parent_id) VALUES ('2', 't', 1, 'assignment', 'system:view', '系统管理', 'menu', NULL, NULL);
INSERT INTO sys_permission(pid, available, state, menu_class, permission, permission_name, resource_type, url, parent_id) VALUES ('3', 't', 1, 'fa fa-caret-right', 'user:view', '用户管理', 'menu', '/system/user/index', '2');
INSERT INTO sys_permission(pid, available, state, menu_class, permission, permission_name, resource_type, url, parent_id) VALUES ('4', 't', 1, 'fa fa-caret-right', 'role:view', '角色管理', 'menu', '/system/role/index', '2');
INSERT INTO sys_permission(pid, available, state, menu_class, permission, permission_name, resource_type, url, parent_id) VALUES ('5', 't', 1, 'fa fa-caret-right', 'permission:view', '权限管理', 'menu', '/system/permission/index', '2');
INSERT INTO sys_permission(pid, available, state, menu_class, permission, permission_name, resource_type, url, parent_id) VALUES ('6', 't', 1, 'fa fa-caret-right', 'user:list', '用户列表', 'auth', '/system/user/list', '3');

INSERT INTO sys_user_role(uid, rid) VALUES ('1', '1');
INSERT INTO sys_user_role(uid, rid) VALUES ('2', '2');

INSERT INTO sys_role_permission(rid, pid) VALUES ('1', '1');
INSERT INTO sys_role_permission(rid, pid) VALUES ('1', '2');
INSERT INTO sys_role_permission(rid, pid) VALUES ('1', '3');
INSERT INTO sys_role_permission(rid, pid) VALUES ('1', '4');
INSERT INTO sys_role_permission(rid, pid) VALUES ('1', '5');
INSERT INTO sys_role_permission(rid, pid) VALUES ('1', '6');
INSERT INTO sys_role_permission(rid, pid) VALUES ('2', '1');
