-- ----------------------------
-- 1、部门表
-- ----------------------------
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept
(
    id          BIGINT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '部门id',
    parent_id   BIGINT(20) DEFAULT 0 COMMENT '父部门id',
    ancestors   VARCHAR(50) COMMENT '祖级列表',
    dept_name   VARCHAR(30) COMMENT '部门名称',
    order_num   INT(4)     DEFAULT 0 COMMENT '显示顺序',
    leader      VARCHAR(20) COMMENT '负责人',
    phone       VARCHAR(11) COMMENT '联系电话',
    email       VARCHAR(50) COMMENT '邮箱',
    status      TINYINT(1) DEFAULT 2 COMMENT '部门状态: 0-删除,1-停用,2-正常',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '部门表';


-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
INSERT INTO sys_dept
VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());
INSERT INTO sys_dept
VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', 2, SYSDATE(), SYSDATE());


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    id          BIGINT(20)  NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    dept_id     BIGINT(20) DEFAULT NULL COMMENT '部门ID',
    username    VARCHAR(30) NOT NULL COMMENT '登录账号',
    nickname    VARCHAR(30) COMMENT '用户昵称',
    email       VARCHAR(50) COMMENT '用户邮箱',
    phone       VARCHAR(11) COMMENT '手机号码',
    sex         TINYINT(1) DEFAULT '0' COMMENT '性别: 0-男,1-女,2-未知',
    avatar      VARCHAR(100) COMMENT '头像路径',
    password    VARCHAR(50) COMMENT '密码',
    salt        VARCHAR(20) COMMENT '盐加密',
    status      TINYINT(1) DEFAULT 2 COMMENT '帐号状态: 0-删除,1-停用,2-正常',
    remark      VARCHAR(500) COMMENT '备注',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = innodb
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户信息表';


-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
INSERT INTO sys_user
VALUES (1, 103, 'admin', '若依', 'ry@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934',
        '111111', '1', '', SYSDATE(), SYSDATE());
INSERT INTO sys_user
VALUES (2, 105, 'ry', '若依', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36',
        '222222', '1', '', SYSDATE(), SYSDATE());


-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
DROP TABLE IF EXISTS sys_post;
CREATE TABLE sys_post
(
    id          BIGINT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
    post_code   VARCHAR(64) NOT NULL COMMENT '岗位编码',
    post_name   VARCHAR(50) NOT NULL COMMENT '岗位名称',
    order_num   INT(4)     DEFAULT 100 COMMENT '显示顺序',
    status      TINYINT(1) DEFAULT 1 COMMENT '状态:0-停用,1-正常',
    remark      VARCHAR(500) COMMENT '备注',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
INSERT INTO sys_post
VALUES (1, 'ceo', '董事长', 1, 1, NULL, SYSDATE(), SYSDATE());
INSERT INTO sys_post
VALUES (2, 'se', '项目经理', 2, 1, NULL, SYSDATE(), SYSDATE());
INSERT INTO sys_post
VALUES (3, 'hr', '人力资源', 3, 1, NULL, SYSDATE(), SYSDATE());
INSERT INTO sys_post
VALUES (4, 'user', '普通员工', 4, 1, NULL, SYSDATE(), SYSDATE());


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role
(
    id          BIGINT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    name        VARCHAR(30) NOT NULL COMMENT '角色名称',
    data_scope  TINYINT(1) DEFAULT 1 COMMENT '数据范围: 1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限',
    status      TINYINT(1) DEFAULT 2 COMMENT '角色状态: 2-正常,1-冻结,0-删除',
    order_num   INT(4)     DEFAULT 100 COMMENT '显示顺序',
    remark      VARCHAR(500) COMMENT '备注',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
INSERT INTO sys_role
VALUES ('1', '超级管理员', 1, 1, 0, '超级管理员', SYSDATE(), SYSDATE());
INSERT INTO sys_role
VALUES ('2', '普通角色', 2, 2, 0, '普通角色', SYSDATE(), SYSDATE());


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
-- auto-generated definition
CREATE TABLE sys_menu
(
    id          BIGINT AUTO_INCREMENT COMMENT '菜单ID'
        PRIMARY KEY,
    menu_name   VARCHAR(50)                            NOT NULL COMMENT '菜单名称',
    parent_id   BIGINT       DEFAULT 0                 NULL COMMENT '父菜单ID',
    order_num   INT(4)       DEFAULT 0                 NULL COMMENT '显示顺序',
    url         VARCHAR(200) DEFAULT '#'               NULL COMMENT '请求地址',
    open_type   TINYINT(1)                             NULL COMMENT '打开方式: 1-页签 2-新窗口',
    menu_type   TINYINT(1)   DEFAULT 1                 NULL COMMENT '菜单类型: 1-目录 2-菜单 3-按钮）',
    visible     TINYINT(1)   DEFAULT 1                 NULL COMMENT '菜单状态: 0-隐藏 1-显示 ',
    is_refresh  TINYINT(1)   DEFAULT 0                 NULL COMMENT '是否刷新: 0-不刷新 1-刷新',
    perms       VARCHAR(100)                           NULL COMMENT '权限标识',
    icon        VARCHAR(100) DEFAULT '#'               NULL COMMENT '菜单图标',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)
    COMMENT '菜单权限表' CHARSET = utf8mb4;

INSERT INTO sys_menu  VALUES (1, '系统管理', 0, 1, '#', null, 1, 0, 1, '', 'fa fa-gear', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (2, '系统监控', 0, 2, '#', null, 1, 0, 1, '', 'fa fa-video-camera', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (3, '系统工具', 0, 3, '#', null, 1, 0, 1, '', 'fa fa-bars', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (4, '若依官网', 0, 4, 'http://ruoyi.vip', 2, 2, 0, 1, '', 'fa fa-location-arrow', '2023-04-03 09:28:09', '2023-04-03 09:37:33');
INSERT INTO sys_menu  VALUES (100, '用户管理', 1, 1, '/system/user', null, 2, 0, 1, 'system:user:view', 'fa fa-user-o', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (101, '角色管理', 1, 2, '/system/role', null, 2, 0, 1, 'system:role:view', 'fa fa-user-secret', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (102, '菜单管理', 1, 3, '/system/menu', null, 2, 0, 1, 'system:menu:view', 'fa fa-th-list', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (103, '部门管理', 1, 4, '/system/dept', null, 2, 0, 1, 'system:dept:view', 'fa fa-outdent', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (104, '岗位管理', 1, 5, '/system/post', null, 2, 0, 1, 'system:post:view', 'fa fa-address-card-o', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (105, '字典管理', 1, 6, '/system/dict', null, 2, 0, 1, 'system:dict:view', 'fa fa-bookmark-o', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (106, '参数设置', 1, 7, '/system/config', null, 2, 0, 1, 'system:config:view', 'fa fa-sun-o', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (107, '通知公告', 1, 8, '/system/notice', null, 2, 0, 1, 'system:notice:view', 'fa fa-bullhorn', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (108, '日志管理', 1, 9, '#', null, 1, 0, 1, '', 'fa fa-pencil-square-o', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (109, '在线用户', 2, 1, '/monitor/online', null, 2, 0, 1, 'monitor:online:view', 'fa fa-user-circle', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (110, '定时任务', 2, 2, '/monitor/job', null, 2, 0, 1, 'monitor:job:view', 'fa fa-tasks', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (111, '数据监控', 2, 3, '/monitor/data', null, 2, 0, 1, 'monitor:data:view', 'fa fa-bug', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (112, '服务监控', 2, 4, '/monitor/server', null, 2, 0, 1, 'monitor:server:view', 'fa fa-server', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (113, '缓存监控', 2, 5, '/monitor/cache', null, 2, 0, 1, 'monitor:cache:view', 'fa fa-cube', '2023-04-03 09:28:09', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (114, '表单构建', 3, 1, '/tool/build', null, 2, 0, 1, 'tool:build:view', 'fa fa-wpforms', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (115, '代码生成', 3, 2, '/tool/gen', null, 2, 0, 1, 'tool:gen:view', 'fa fa-code', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (116, '系统接口', 3, 3, '/tool/swagger', null, 2, 0, 1, 'tool:swagger:view', 'fa fa-gg', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (500, '操作日志', 108, 1, '/monitor/operlog', null, 2, 0, 1, 'monitor:operlog:view', 'fa fa-address-book', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (501, '登录日志', 108, 2, '/monitor/logininfor', null, 2, 0, 1, 'monitor:logininfor:view', 'fa fa-file-image-o', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1000, '用户查询', 100, 1, '#', null, 3, 0, 1, 'system:user:list', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1001, '用户新增', 100, 2, '#', null, 3, 0, 1, 'system:user:add', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1002, '用户修改', 100, 3, '#', null, 3, 0, 1, 'system:user:edit', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1003, '用户删除', 100, 4, '#', null, 3, 0, 1, 'system:user:remove', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1004, '用户导出', 100, 5, '#', null, 3, 0, 1, 'system:user:export', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1005, '用户导入', 100, 6, '#', null, 3, 0, 1, 'system:user:import', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1006, '重置密码', 100, 7, '#', null, 3, 0, 1, 'system:user:resetPwd', '#', '2023-04-03 09:28:10', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1007, '角色查询', 101, 1, '#', null, 3, 0, 1, 'system:role:list', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1008, '角色新增', 101, 2, '#', null, 3, 0, 1, 'system:role:add', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1009, '角色修改', 101, 3, '#', null, 3, 0, 1, 'system:role:edit', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1010, '角色删除', 101, 4, '#', null, 3, 0, 1, 'system:role:remove', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1011, '角色导出', 101, 5, '#', null, 3, 0, 1, 'system:role:export', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1012, '菜单查询', 102, 1, '#', null, 3, 0, 1, 'system:menu:list', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1013, '菜单新增', 102, 2, '#', null, 3, 0, 1, 'system:menu:add', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1014, '菜单修改', 102, 3, '#', null, 3, 0, 1, 'system:menu:edit', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1015, '菜单删除', 102, 4, '#', null, 3, 0, 1, 'system:menu:remove', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1016, '部门查询', 103, 1, '#', null, 3, 0, 1, 'system:dept:list', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1017, '部门新增', 103, 2, '#', null, 3, 0, 1, 'system:dept:add', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1018, '部门修改', 103, 3, '#', null, 3, 0, 1, 'system:dept:edit', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1019, '部门删除', 103, 4, '#', null, 3, 0, 1, 'system:dept:remove', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1020, '岗位查询', 104, 1, '#', null, 3, 0, 1, 'system:post:list', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1021, '岗位新增', 104, 2, '#', null, 3, 0, 1, 'system:post:add', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1022, '岗位修改', 104, 3, '#', null, 3, 0, 1, 'system:post:edit', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1023, '岗位删除', 104, 4, '#', null, 3, 0, 1, 'system:post:remove', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1024, '岗位导出', 104, 5, '#', null, 3, 0, 1, 'system:post:export', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1025, '字典查询', 105, 1, '#', null, 3, 0, 1, 'system:dict:list', '#', '2023-04-03 09:28:11', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1026, '字典新增', 105, 2, '#', null, 3, 0, 1, 'system:dict:add', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1027, '字典修改', 105, 3, '#', null, 3, 0, 1, 'system:dict:edit', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1028, '字典删除', 105, 4, '#', null, 3, 0, 1, 'system:dict:remove', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1029, '字典导出', 105, 5, '#', null, 3, 0, 1, 'system:dict:export', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1030, '参数查询', 106, 1, '#', null, 3, 0, 1, 'system:config:list', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1031, '参数新增', 106, 2, '#', null, 3, 0, 1, 'system:config:add', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1032, '参数修改', 106, 3, '#', null, 3, 0, 1, 'system:config:edit', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1033, '参数删除', 106, 4, '#', null, 3, 0, 1, 'system:config:remove', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1034, '参数导出', 106, 5, '#', null, 3, 0, 1, 'system:config:export', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1035, '公告查询', 107, 1, '#', null, 3, 0, 1, 'system:notice:list', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1036, '公告新增', 107, 2, '#', null, 3, 0, 1, 'system:notice:add', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1037, '公告修改', 107, 3, '#', null, 3, 0, 1, 'system:notice:edit', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1038, '公告删除', 107, 4, '#', null, 3, 0, 1, 'system:notice:remove', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1039, '操作查询', 500, 1, '#', null, 3, 0, 1, 'monitor:operlog:list', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1040, '操作删除', 500, 2, '#', null, 3, 0, 1, 'monitor:operlog:remove', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1041, '详细信息', 500, 3, '#', null, 3, 0, 1, 'monitor:operlog:detail', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1042, '日志导出', 500, 4, '#', null, 3, 0, 1, 'monitor:operlog:export', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1043, '登录查询', 501, 1, '#', null, 3, 0, 1, 'monitor:logininfor:list', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1044, '登录删除', 501, 2, '#', null, 3, 0, 1, 'monitor:logininfor:remove', '#', '2023-04-03 09:28:12', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1045, '日志导出', 501, 3, '#', null, 3, 0, 1, 'monitor:logininfor:export', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1046, '账户解锁', 501, 4, '#', null, 3, 0, 1, 'monitor:logininfor:unlock', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1047, '在线查询', 109, 1, '#', null, 3, 0, 1, 'monitor:online:list', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1048, '批量强退', 109, 2, '#', null, 3, 0, 1, 'monitor:online:batchForceLogout', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1049, '单条强退', 109, 3, '#', null, 3, 0, 1, 'monitor:online:forceLogout', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1050, '任务查询', 110, 1, '#', null, 3, 0, 1, 'monitor:job:list', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1051, '任务新增', 110, 2, '#', null, 3, 0, 1, 'monitor:job:add', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1052, '任务修改', 110, 3, '#', null, 3, 0, 1, 'monitor:job:edit', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1053, '任务删除', 110, 4, '#', null, 3, 0, 1, 'monitor:job:remove', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1054, '状态修改', 110, 5, '#', null, 3, 0, 1, 'monitor:job:changeStatus', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1055, '任务详细', 110, 6, '#', null, 3, 0, 1, 'monitor:job:detail', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1056, '任务导出', 110, 7, '#', null, 3, 0, 1, 'monitor:job:export', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1057, '生成查询', 115, 1, '#', null, 3, 0, 1, 'tool:gen:list', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1058, '生成修改', 115, 2, '#', null, 3, 0, 1, 'tool:gen:edit', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1059, '生成删除', 115, 3, '#', null, 3, 0, 1, 'tool:gen:remove', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1060, '预览代码', 115, 4, '#', null, 3, 0, 1, 'tool:gen:preview', '#', '2023-04-03 09:28:13', '2023-04-03 09:37:17');
INSERT INTO sys_menu  VALUES (1061, '生成代码', 115, 5, '#', null, 3, 0, 1, 'tool:gen:code', '#', '2023-04-03 09:28:14', '2023-04-03 09:37:17');



-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE = innodb COMMENT = '用户角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
INSERT INTO sys_user_role
VALUES ('1', '1');
INSERT INTO sys_user_role
VALUES ('2', '2');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu
(
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    menu_id BIGINT(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE = innodb COMMENT = '角色菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO sys_role_menu
VALUES ('2', '1');
INSERT INTO sys_role_menu
VALUES ('2', '2');
INSERT INTO sys_role_menu
VALUES ('2', '3');
INSERT INTO sys_role_menu
VALUES ('2', '4');
INSERT INTO sys_role_menu
VALUES ('2', '100');
INSERT INTO sys_role_menu
VALUES ('2', '101');
INSERT INTO sys_role_menu
VALUES ('2', '102');
INSERT INTO sys_role_menu
VALUES ('2', '103');
INSERT INTO sys_role_menu
VALUES ('2', '104');
INSERT INTO sys_role_menu
VALUES ('2', '105');
INSERT INTO sys_role_menu
VALUES ('2', '106');
INSERT INTO sys_role_menu
VALUES ('2', '107');
INSERT INTO sys_role_menu
VALUES ('2', '108');
INSERT INTO sys_role_menu
VALUES ('2', '109');
INSERT INTO sys_role_menu
VALUES ('2', '110');
INSERT INTO sys_role_menu
VALUES ('2', '111');
INSERT INTO sys_role_menu
VALUES ('2', '112');
INSERT INTO sys_role_menu
VALUES ('2', '113');
INSERT INTO sys_role_menu
VALUES ('2', '114');
INSERT INTO sys_role_menu
VALUES ('2', '115');
INSERT INTO sys_role_menu
VALUES ('2', '116');
INSERT INTO sys_role_menu
VALUES ('2', '500');
INSERT INTO sys_role_menu
VALUES ('2', '501');
INSERT INTO sys_role_menu
VALUES ('2', '1000');
INSERT INTO sys_role_menu
VALUES ('2', '1001');
INSERT INTO sys_role_menu
VALUES ('2', '1002');
INSERT INTO sys_role_menu
VALUES ('2', '1003');
INSERT INTO sys_role_menu
VALUES ('2', '1004');
INSERT INTO sys_role_menu
VALUES ('2', '1005');
INSERT INTO sys_role_menu
VALUES ('2', '1006');
INSERT INTO sys_role_menu
VALUES ('2', '1007');
INSERT INTO sys_role_menu
VALUES ('2', '1008');
INSERT INTO sys_role_menu
VALUES ('2', '1009');
INSERT INTO sys_role_menu
VALUES ('2', '1010');
INSERT INTO sys_role_menu
VALUES ('2', '1011');
INSERT INTO sys_role_menu
VALUES ('2', '1012');
INSERT INTO sys_role_menu
VALUES ('2', '1013');
INSERT INTO sys_role_menu
VALUES ('2', '1014');
INSERT INTO sys_role_menu
VALUES ('2', '1015');
INSERT INTO sys_role_menu
VALUES ('2', '1016');
INSERT INTO sys_role_menu
VALUES ('2', '1017');
INSERT INTO sys_role_menu
VALUES ('2', '1018');
INSERT INTO sys_role_menu
VALUES ('2', '1019');
INSERT INTO sys_role_menu
VALUES ('2', '1020');
INSERT INTO sys_role_menu
VALUES ('2', '1021');
INSERT INTO sys_role_menu
VALUES ('2', '1022');
INSERT INTO sys_role_menu
VALUES ('2', '1023');
INSERT INTO sys_role_menu
VALUES ('2', '1024');
INSERT INTO sys_role_menu
VALUES ('2', '1025');
INSERT INTO sys_role_menu
VALUES ('2', '1026');
INSERT INTO sys_role_menu
VALUES ('2', '1027');
INSERT INTO sys_role_menu
VALUES ('2', '1028');
INSERT INTO sys_role_menu
VALUES ('2', '1029');
INSERT INTO sys_role_menu
VALUES ('2', '1030');
INSERT INTO sys_role_menu
VALUES ('2', '1031');
INSERT INTO sys_role_menu
VALUES ('2', '1032');
INSERT INTO sys_role_menu
VALUES ('2', '1033');
INSERT INTO sys_role_menu
VALUES ('2', '1034');
INSERT INTO sys_role_menu
VALUES ('2', '1035');
INSERT INTO sys_role_menu
VALUES ('2', '1036');
INSERT INTO sys_role_menu
VALUES ('2', '1037');
INSERT INTO sys_role_menu
VALUES ('2', '1038');
INSERT INTO sys_role_menu
VALUES ('2', '1039');
INSERT INTO sys_role_menu
VALUES ('2', '1040');
INSERT INTO sys_role_menu
VALUES ('2', '1041');
INSERT INTO sys_role_menu
VALUES ('2', '1042');
INSERT INTO sys_role_menu
VALUES ('2', '1043');
INSERT INTO sys_role_menu
VALUES ('2', '1044');
INSERT INTO sys_role_menu
VALUES ('2', '1045');
INSERT INTO sys_role_menu
VALUES ('2', '1046');
INSERT INTO sys_role_menu
VALUES ('2', '1047');
INSERT INTO sys_role_menu
VALUES ('2', '1048');
INSERT INTO sys_role_menu
VALUES ('2', '1049');
INSERT INTO sys_role_menu
VALUES ('2', '1050');
INSERT INTO sys_role_menu
VALUES ('2', '1051');
INSERT INTO sys_role_menu
VALUES ('2', '1052');
INSERT INTO sys_role_menu
VALUES ('2', '1053');
INSERT INTO sys_role_menu
VALUES ('2', '1054');
INSERT INTO sys_role_menu
VALUES ('2', '1055');
INSERT INTO sys_role_menu
VALUES ('2', '1056');
INSERT INTO sys_role_menu
VALUES ('2', '1057');
INSERT INTO sys_role_menu
VALUES ('2', '1058');
INSERT INTO sys_role_menu
VALUES ('2', '1059');
INSERT INTO sys_role_menu
VALUES ('2', '1060');
INSERT INTO sys_role_menu
VALUES ('2', '1061');

-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
DROP TABLE IF EXISTS sys_role_dept;
CREATE TABLE sys_role_dept
(
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    dept_id BIGINT(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (role_id, dept_id)
) ENGINE = innodb COMMENT = '角色部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
INSERT INTO sys_role_dept
VALUES ('2', '100');
INSERT INTO sys_role_dept
VALUES ('2', '101');
INSERT INTO sys_role_dept
VALUES ('2', '105');

-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
DROP TABLE IF EXISTS sys_user_post;
CREATE TABLE sys_user_post
(
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    post_id BIGINT(20) NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (user_id, post_id)
) ENGINE = innodb COMMENT = '用户岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
INSERT INTO sys_user_post
VALUES ('1', '1');
INSERT INTO sys_user_post
VALUES ('2', '2');



-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details`
(
    `client_id`               VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端ID',
    `resource_ids`            VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '资源列表',
    `client_secret`           VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '客户端密钥',
    `scope`                   VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '域',
    `authorized_grant_types`  VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '认证类型',
    `web_server_redirect_uri` VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '重定向地址',
    `authorities`             VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '角色列表',
    `token_live_time`         VARCHAR(52) COMMENT '令牌有效期',
    `refresh_token_live_time` VARCHAR(52) COMMENT '刷新令牌有效期',
    `additional_information`  VARCHAR(4096) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '令牌扩展字段JSON',
    `auto_approve`            VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '是否自动放行',
    create_time               TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`client_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details`
VALUES ('app', NULL, 'app', 'server', 'app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL);
INSERT INTO `sys_oauth_client_details`
VALUES ('daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL);
INSERT INTO `sys_oauth_client_details`
VALUES ('gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL);
INSERT INTO `sys_oauth_client_details`
VALUES ('pig', NULL, 'pig', 'server', 'password,app,refresh_token,authorization_code,client_credentials',
        'https://pigx.vip', NULL, NULL, NULL, NULL, 'true', NULL, NULL);
INSERT INTO `sys_oauth_client_details`
VALUES ('test', NULL, 'test', 'server', 'password,app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL);
INSERT INTO `sys_oauth_client_details`
VALUES ('client', NULL, 'client', 'server', 'client_credentials', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL);
COMMIT;
