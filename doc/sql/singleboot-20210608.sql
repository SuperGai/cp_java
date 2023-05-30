/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : singleboot

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 08/06/2021 11:02:34
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '接口名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '接口路径',
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '所属模块 从字典表取值',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统API接口' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_api
-- ----------------------------
INSERT INTO `sys_api` VALUES ('1', 1, '2020-08-20 16:39:27', '2020-10-12 11:40:09', '菜单下拉框列表', '/sys/menu/dropdownList', '1');
INSERT INTO `sys_api` VALUES ('10', 1, '2020-08-25 17:59:34', '2021-01-18 15:43:13', '根据id删除字典记录', '/sys/dict/delete', '3');
INSERT INTO `sys_api` VALUES ('12', 1, '2020-08-25 18:00:26', '2020-10-12 10:55:08', '获取字典详情', '/sys/dict/detail', '3');
INSERT INTO `sys_api` VALUES ('13', 1, '2020-08-25 18:00:58', '2020-10-12 10:55:10', '分页查询字典', '/sys/dict/listPage', '3');
INSERT INTO `sys_api` VALUES ('17', 1, '2020-10-10 10:02:09', '2020-10-12 10:55:47', '分页查询字典类型', '/sys/dict/type/listPage', '4');
INSERT INTO `sys_api` VALUES ('18', 1, '2020-10-10 10:03:53', '2020-10-12 10:56:23', '字典类型下拉框列表', '/sys/dict/type/dropdownList', '1');
INSERT INTO `sys_api` VALUES ('19', 1, '2020-10-10 10:04:58', '2020-10-12 10:55:48', '获取字典类型详情', '/sys/dict/type/detail', '4');
INSERT INTO `sys_api` VALUES ('2', 1, '2020-08-20 16:44:52', '2020-10-12 10:54:01', '分页查询菜单', '/sys/menu/listPage', '2');
INSERT INTO `sys_api` VALUES ('20', 1, '2020-10-10 10:05:46', '2020-10-12 10:55:50', '添加字典类型记录', '/sys/dict/type/add', '4');
INSERT INTO `sys_api` VALUES ('21', 1, '2020-10-10 10:06:39', '2020-10-12 10:55:52', '更新字典类型记录', '/sys/dict/type/update', '4');
INSERT INTO `sys_api` VALUES ('22', 1, '2020-10-10 10:08:07', '2021-01-18 15:43:09', '根据id删除字典类型记录', '/sys/dict/type/delete', '4');
INSERT INTO `sys_api` VALUES ('25', 1, '2020-10-10 14:44:31', '2020-10-12 10:56:38', '添加角色', '/sys/role/add', '6');
INSERT INTO `sys_api` VALUES ('26', 1, '2020-10-10 14:44:51', '2020-10-12 10:56:39', '更新角色', '/sys/role/update', '6');
INSERT INTO `sys_api` VALUES ('27', 1, '2020-10-10 14:45:28', '2021-01-18 15:43:08', '根据id删除角色', '/sys/role/delete', '6');
INSERT INTO `sys_api` VALUES ('27f2146d37bf260ead74ea0d2965b201', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '批量删除微信小程序用户', '/wxMiniAppUser/batchDeleteByCodeList', 'WxMiniAppUser_manage');
INSERT INTO `sys_api` VALUES ('28', 1, '2020-10-10 15:15:50', '2020-10-12 10:56:43', '分页查询角色', '/sys/role/listPage', '6');
INSERT INTO `sys_api` VALUES ('29', 1, '2020-10-10 15:16:37', '2020-10-12 10:56:43', '查看角色详情', '/sys/role/detail', '6');
INSERT INTO `sys_api` VALUES ('3', 1, '2020-08-20 16:45:30', '2020-10-12 10:53:49', '获取菜单详情', '/sys/menu/detail', '2');
INSERT INTO `sys_api` VALUES ('30', 1, '2020-10-10 15:17:30', '2020-10-12 10:57:04', '添加功能', '/sys/function/add', '7');
INSERT INTO `sys_api` VALUES ('31', 1, '2020-10-10 15:17:47', '2020-10-12 10:57:04', '更新功能', '/sys/function/update', '7');
INSERT INTO `sys_api` VALUES ('32', 1, '2020-10-10 15:18:07', '2020-10-12 10:57:04', '查看功能详情', '/sys/function/detail', '7');
INSERT INTO `sys_api` VALUES ('33', 1, '2020-10-10 15:18:49', '2021-01-18 15:43:06', '根据id删除功能', '/sys/function/delete', '7');
INSERT INTO `sys_api` VALUES ('34', 1, '2020-10-10 15:19:29', '2020-10-12 10:57:04', '分页查询功能', '/sys/function/listPage', '7');
INSERT INTO `sys_api` VALUES ('343fe86b824c44c89d388a32621cb599', 1, '2021-01-20 02:53:38', '2021-01-20 02:53:38', '批量删除功能', '/sys/function/batchDelete', '7');
INSERT INTO `sys_api` VALUES ('35', 1, '2020-10-10 15:20:35', '2020-10-12 10:57:21', '功能下拉框列表', '/sys/function/dropdownList', '1');
INSERT INTO `sys_api` VALUES ('36', 1, '2020-10-10 15:22:31', '2020-10-12 10:57:26', '分页查询接口', '/sys/api/listPage', '8');
INSERT INTO `sys_api` VALUES ('37', 1, '2020-10-10 15:22:58', '2020-10-12 10:57:26', '添加接口', '/sys/api/add', '8');
INSERT INTO `sys_api` VALUES ('38', 1, '2020-10-10 15:23:16', '2020-10-12 10:57:26', '更新接口', '/sys/api/update', '8');
INSERT INTO `sys_api` VALUES ('39', 1, '2020-10-10 15:23:36', '2021-01-18 15:42:57', '根据id删除接口', '/sys/api/delete', '8');
INSERT INTO `sys_api` VALUES ('396f3c5d818f22baa68b1eae2219422e', 1, '2021-01-22 08:23:10', '2021-01-22 08:23:10', '角色下拉框列表', '/sys/role/dropdownList', '1');
INSERT INTO `sys_api` VALUES ('39d2bf20a058310d0e5926604b2e65f1', 1, '2021-01-20 02:36:33', '2021-01-20 02:36:33', '批量删除菜单', '/sys/menu/batchDelete', '2');
INSERT INTO `sys_api` VALUES ('3e9cbe14b083c2d18b3811d06e55e4af', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:01', '分页查询部门', '/testDept/listPage', 'TestDept_manage');
INSERT INTO `sys_api` VALUES ('4', 1, '2020-08-20 16:46:03', '2020-10-12 10:54:17', '添加菜单记录', '/sys/menu/add', '2');
INSERT INTO `sys_api` VALUES ('40', 1, '2020-10-10 15:24:51', '2020-10-12 10:57:26', '查看接口详情', '/sys/api/detail', '8');
INSERT INTO `sys_api` VALUES ('41', 1, '2020-10-10 15:26:08', '2020-10-12 10:57:37', '添加用户', '/sys/user/add', '5');
INSERT INTO `sys_api` VALUES ('42', 1, '2020-10-10 15:26:33', '2021-01-18 15:43:00', '根据id删除用户', '/sys/user/delete', '5');
INSERT INTO `sys_api` VALUES ('43', 1, '2020-10-10 15:27:51', '2020-10-12 10:57:37', '更新用户', '/sys/user/update', '5');
INSERT INTO `sys_api` VALUES ('44', 1, '2020-10-10 15:28:45', '2020-10-12 10:57:37', '分页查询用户', '/sys/user/listPage', '5');
INSERT INTO `sys_api` VALUES ('45', 1, '2020-10-10 16:55:26', '2020-10-12 10:57:37', '获取用户详情', '/sys/user/detail', '5');
INSERT INTO `sys_api` VALUES ('46', 1, '2020-10-10 17:03:44', '2020-10-12 10:57:07', '获取功能接口树', '/sys/function/api/tree', '7');
INSERT INTO `sys_api` VALUES ('47', 1, '2020-10-10 17:04:16', '2020-10-12 10:57:08', '更新功能接口树', '/sys/function/api/tree/update', '7');
INSERT INTO `sys_api` VALUES ('48', 1, '2020-10-10 17:27:42', '2020-10-12 10:57:40', '字典下拉框', '/sys/dict/dropdownList', '1');
INSERT INTO `sys_api` VALUES ('49', 1, '2020-10-10 17:34:55', '2020-10-12 10:57:50', '更新角色菜单树', '/sys/role/menu/tree/update', '6');
INSERT INTO `sys_api` VALUES ('494f1ed40203a71d21363d4944c889e0', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:01', '根据唯一标识删除部门', '/testDept/deleteByCode', 'TestDept_manage');
INSERT INTO `sys_api` VALUES ('5', 1, '2020-08-20 16:47:09', '2020-10-12 10:54:21', '更新菜单记录', '/sys/menu/update', '2');
INSERT INTO `sys_api` VALUES ('50', 1, '2020-10-10 17:35:21', '2020-10-12 10:57:50', '更新角色功能树', '/sys/role/function/tree/update', '6');
INSERT INTO `sys_api` VALUES ('51', 1, '2020-10-10 18:05:29', '2020-10-12 10:57:50', '获取角色菜单树', '/sys/role/menu/tree', '6');
INSERT INTO `sys_api` VALUES ('52', 1, '2020-10-10 18:06:14', '2020-10-12 10:57:50', '获取角色功能树', '/sys/role/function/tree', '6');
INSERT INTO `sys_api` VALUES ('5f705745a8efca9366c7a233eb53e86f', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:02', '分页查询人员', '/hj/testPerson/listPage', 'TestPerson_manage');
INSERT INTO `sys_api` VALUES ('6', 1, '2020-08-20 16:47:35', '2021-01-18 15:43:04', '根据id删除菜单记录', '/sys/menu/delete', '2');
INSERT INTO `sys_api` VALUES ('67739974d1b476d7213d6615d4293485', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '查看微信小程序用户详情', '/wxMiniAppUser/detail', 'WxMiniAppUser_manage');
INSERT INTO `sys_api` VALUES ('6d304e8fefaa468a3b66a98e8a44e8ee', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '分页查询微信小程序用户', '/wxMiniAppUser/listPage', 'WxMiniAppUser_manage');
INSERT INTO `sys_api` VALUES ('73ab249a404f94a44f521fdc1c120696', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:03', '更新人员', '/hj/testPerson/update', 'TestPerson_manage');
INSERT INTO `sys_api` VALUES ('788b120a48c4f0b74656c6c4af4aaf4c', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:03', '查看人员详情', '/hj/testPerson/detail', 'TestPerson_manage');
INSERT INTO `sys_api` VALUES ('7fadb5d098ecc580c585f4b71bb2d520', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:04', '新增人员', '/hj/testPerson/add', 'TestPerson_manage');
INSERT INTO `sys_api` VALUES ('8', 1, '2020-08-25 17:58:03', '2020-10-12 10:54:35', '添加字典记录', '/sys/dict/add', '3');
INSERT INTO `sys_api` VALUES ('81ca2fc5e0a42d38254e232bcbc67bf3', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '更新微信小程序用户', '/wxMiniAppUser/update', 'WxMiniAppUser_manage');
INSERT INTO `sys_api` VALUES ('8b2c43c9ae19e2bc37ca025b96e56c20', 1, '2021-01-18 07:56:56', '2021-01-18 07:56:56', '批量删除字典', '/sys/dict/batchDelete', '3');
INSERT INTO `sys_api` VALUES ('9', 1, '2020-08-25 17:59:12', '2020-10-12 10:55:04', '更新字典记录', '/sys/dict/update', '3');
INSERT INTO `sys_api` VALUES ('9a844f8c7fc63b978cd62b91efc849e3', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '新增微信小程序用户', '/wxMiniAppUser/add', 'WxMiniAppUser_manage');
INSERT INTO `sys_api` VALUES ('a3a4901b862a3ecccde35b6bd4fc0951', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:05', '批量删除部门', '/testDept/batchDeleteByCodeList', 'TestDept_manage');
INSERT INTO `sys_api` VALUES ('a3f516caafb31762f7aab10ea9551cd3', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '根据唯一标识删除微信小程序用户', '/wxMiniAppUser/deleteByCode', 'WxMiniAppUser_manage');
INSERT INTO `sys_api` VALUES ('b30622e2eb1060b3ab1a3780d70d06cb', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:06', '根据唯一标识删除人员', '/hj/testPerson/deleteByCode', 'TestPerson_manage');
INSERT INTO `sys_api` VALUES ('b5f560ba86f675b0e885d2ed345afc19', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:07', '批量删除人员', '/hj/testPerson/batchDeleteByCodeList', 'TestPerson_manage');
INSERT INTO `sys_api` VALUES ('c8e03adb530acf14649233b14f4c5c33', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:07', '查看部门详情', '/testDept/detail', 'TestDept_manage');
INSERT INTO `sys_api` VALUES ('d8fa984d6b02c9f57be9ef6788de62ec', 1, '2021-01-18 07:39:58', '2021-01-18 15:43:32', '批量删除角色记录', '/sys/role/batchDelete', '6');
INSERT INTO `sys_api` VALUES ('d925149b4389d8324ba186e2ae9fa420', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:11', '更新部门', '/testDept/update', 'TestDept_manage');
INSERT INTO `sys_api` VALUES ('de79cf2e0f980497d8c8a3ff4c2faf8c', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:08', '新增部门', '/testDept/add', 'TestDept_manage');
INSERT INTO `sys_api` VALUES ('e57b786e332214664bcbce43fa85368f', 1, '2021-01-20 02:16:12', '2021-01-20 02:16:12', '批量删除字典类型', '/sys/dict/type/batchDelete', '4');
INSERT INTO `sys_api` VALUES ('e620a9dac57eb07721577f51353ce3ac', 1, '2021-01-22 01:45:25', '2021-01-22 01:45:25', '批量删除接口', '/sys/api/batchDelete', '8');
INSERT INTO `sys_api` VALUES ('e9672ef999e72419ab2aeffa416d7ffa', 1, '2021-01-25 07:31:47', '2021-01-25 07:31:47', '重置密码', '/sys/user/password/reset', '5');
INSERT INTO `sys_api` VALUES ('ead5e8b0211701b6b7d8c775917348c7', 1, '2021-01-22 02:47:46', '2021-01-22 02:47:46', '批量删除用户', '/sys/user/batchDelete', '5');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `type_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '字典类型id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '值',
  `data` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '自定数据',
  `sort` bigint(20) NULL DEFAULT NULL COMMENT '排序',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', 1, '2020-08-20 16:43:30', '2020-10-12 11:22:11', '1', '公用', '1', '', 1, '');
INSERT INTO `sys_dict` VALUES ('15356fda8920d318d5616a72436df0ac', 1, '2021-01-20 02:23:00', '2021-01-25 06:20:51', '71150785510908540b3f66b1a77a10b8', '已停用', '2', '', 2, '');
INSERT INTO `sys_dict` VALUES ('19a087f9afc7de739dbe5c77f07e945f', 1, '2021-01-20 02:19:17', '2021-01-20 02:19:17', '376c5a86a9ee3412c42925c11a7264e0', '已通过', '2', '', 2, '');
INSERT INTO `sys_dict` VALUES ('1df589b38b4af23715866ffd68079e1b', 1, '2021-01-20 02:20:03', '2021-01-20 02:20:03', 'aac5d41ac20bb33b62eaa8f61cd5b227', '已审核', '2', '', 2, '');
INSERT INTO `sys_dict` VALUES ('2', 1, '2020-08-20 16:30:57', '2020-10-12 10:13:11', '1', '菜单管理', '2', NULL, 2, NULL);
INSERT INTO `sys_dict` VALUES ('3', 1, '2020-08-25 17:56:44', '2020-10-12 10:13:11', '1', '字典管理', '3', NULL, 3, NULL);
INSERT INTO `sys_dict` VALUES ('4', 1, '2020-08-25 17:57:18', '2020-10-12 10:13:11', '1', '字典类型管理', '4', NULL, 4, NULL);
INSERT INTO `sys_dict` VALUES ('488bfa2877af7f85dbbeefcccf8ee5cd', 1, '2021-01-20 02:19:29', '2021-01-20 02:19:29', '376c5a86a9ee3412c42925c11a7264e0', '未通过', '3', '', 3, '');
INSERT INTO `sys_dict` VALUES ('5', 1, '2020-10-10 09:55:53', '2020-10-12 10:13:11', '1', '用户管理', '5', NULL, 5, NULL);
INSERT INTO `sys_dict` VALUES ('6', 1, '2020-10-10 09:56:11', '2020-10-12 10:13:11', '1', '角色管理', '6', NULL, 6, NULL);
INSERT INTO `sys_dict` VALUES ('6cacb0a8252e474fd81b24ad1185222b', 1, '2021-01-22 08:22:09', '2021-01-22 08:22:09', '1', '个人中心', '9', '', 9, '');
INSERT INTO `sys_dict` VALUES ('7', 1, '2020-10-10 09:56:37', '2020-10-12 10:13:11', '1', '功能管理', '7', NULL, 7, NULL);
INSERT INTO `sys_dict` VALUES ('7b49c10dc700a2fbe179560331f4f6c2', 1, '2021-01-20 02:19:54', '2021-01-20 02:19:54', 'aac5d41ac20bb33b62eaa8f61cd5b227', '未审核', '1', '', 1, '');
INSERT INTO `sys_dict` VALUES ('8', 1, '2020-10-10 09:57:05', '2020-10-12 10:13:11', '1', '接口管理', '8', NULL, 8, NULL);
INSERT INTO `sys_dict` VALUES ('9e714f52e0a61a0ae3b09fe2e5b20ef7', 1, '2021-01-20 02:22:48', '2021-01-20 02:22:48', '71150785510908540b3f66b1a77a10b8', '已启用', '1', '', 1, '');
INSERT INTO `sys_dict` VALUES ('b0dbd19fc0868690678d202721b5ba37', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '1', '微信小程序用户管理', 'WxMiniAppUser_manage', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES ('f41ea1cfc4b3ab78e45a9e58d8778eee', 1, '2021-01-20 02:19:04', '2021-01-20 02:19:04', '376c5a86a9ee3412c42925c11a7264e0', '等待审核', '1', '', 1, '');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '字典类型名称',
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '字典类型标识符',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1', 1, '2020-08-20 16:30:08', '2021-01-25 03:32:54', '系统模块', 'SYSTEM_MODULE', '用于功能和接口配置时设置所属模块信息');
INSERT INTO `sys_dict_type` VALUES ('376c5a86a9ee3412c42925c11a7264e0', 1, '2021-01-20 02:17:59', '2021-01-20 02:21:05', '用户审核结果', 'USER_APPROVE_RESULT', '用于用户信息');
INSERT INTO `sys_dict_type` VALUES ('71150785510908540b3f66b1a77a10b8', 1, '2021-01-20 02:21:53', '2021-01-20 02:21:53', '用户状态', 'USER_STATUS', '用于用户信息');
INSERT INTO `sys_dict_type` VALUES ('aac5d41ac20bb33b62eaa8f61cd5b227', 1, '2021-01-20 02:17:47', '2021-01-20 02:21:08', '用户审核状态', 'USER_APPROVE_STATUS', '用于用户信息');

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT NULL COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '接口名称',
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '所属模块 从字典表取值',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES ('01924c9eb0c4f70ef28189c902da033f', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '分页查询微信小程序用户', 'WxMiniAppUser_manage');
INSERT INTO `sys_function` VALUES ('1', 1, '2020-08-20 15:35:45', '2020-10-12 11:19:33', '添加记录', '2');
INSERT INTO `sys_function` VALUES ('10', 1, '2020-08-25 18:03:19', '2020-10-12 11:20:06', '查看记录', '3');
INSERT INTO `sys_function` VALUES ('2', 1, '2020-08-20 16:34:16', '2020-10-12 11:19:33', '分页查询', '2');
INSERT INTO `sys_function` VALUES ('22', 1, '2020-10-10 16:49:54', '2020-10-12 11:20:13', '添加记录', '4');
INSERT INTO `sys_function` VALUES ('23', 1, '2020-10-10 16:50:14', '2020-10-12 11:20:13', '分页查询', '4');
INSERT INTO `sys_function` VALUES ('24', 1, '2020-10-10 16:50:28', '2020-10-12 11:20:13', '删除记录', '4');
INSERT INTO `sys_function` VALUES ('2482ebd3920566969202f3cc5010bd48', 1, '2021-01-25 07:32:00', '2021-01-25 07:32:00', '重置密码', '5');
INSERT INTO `sys_function` VALUES ('25', 1, '2020-10-10 16:50:52', '2020-10-12 11:20:13', '编辑记录', '4');
INSERT INTO `sys_function` VALUES ('26', 1, '2020-10-10 16:51:07', '2020-10-12 11:20:13', '查看记录', '4');
INSERT INTO `sys_function` VALUES ('27', 1, '2020-10-10 16:53:03', '2020-10-12 11:20:45', '分页查询', '5');
INSERT INTO `sys_function` VALUES ('28', 1, '2020-10-10 16:53:13', '2020-10-12 11:20:45', '添加记录', '5');
INSERT INTO `sys_function` VALUES ('29', 1, '2020-10-10 16:53:30', '2020-10-12 11:20:45', '删除记录', '5');
INSERT INTO `sys_function` VALUES ('3', 1, '2020-08-20 16:34:28', '2020-10-12 11:19:33', '删除记录', '2');
INSERT INTO `sys_function` VALUES ('30', 1, '2020-10-10 16:53:41', '2020-10-12 11:20:45', '编辑记录', '5');
INSERT INTO `sys_function` VALUES ('31', 1, '2020-10-10 16:53:55', '2020-10-12 11:20:45', '查看记录', '5');
INSERT INTO `sys_function` VALUES ('32', 1, '2020-10-10 16:56:45', '2020-10-12 11:20:37', '添加记录', '6');
INSERT INTO `sys_function` VALUES ('33', 1, '2020-10-10 16:56:56', '2020-10-12 11:20:37', '删除记录', '6');
INSERT INTO `sys_function` VALUES ('34', 1, '2020-10-10 16:57:32', '2020-10-12 11:20:37', '编辑记录', '6');
INSERT INTO `sys_function` VALUES ('35', 1, '2020-10-10 16:57:50', '2020-10-12 11:20:37', '查看记录', '6');
INSERT INTO `sys_function` VALUES ('36', 1, '2020-10-10 16:58:01', '2020-10-12 11:20:37', '分页查询', '6');
INSERT INTO `sys_function` VALUES ('3620cb3c93f00999722f66dbd2ce904a', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '删除微信小程序用户', 'WxMiniAppUser_manage');
INSERT INTO `sys_function` VALUES ('37', 1, '2020-10-10 16:59:23', '2020-10-12 11:20:19', '分页查询', '7');
INSERT INTO `sys_function` VALUES ('38', 1, '2020-10-10 16:59:36', '2020-10-12 11:20:19', '添加记录', '7');
INSERT INTO `sys_function` VALUES ('39', 1, '2020-10-10 16:59:53', '2020-10-12 11:20:20', '删除记录', '7');
INSERT INTO `sys_function` VALUES ('4', 1, '2020-08-20 16:34:33', '2020-10-12 11:19:33', '编辑记录', '2');
INSERT INTO `sys_function` VALUES ('40', 1, '2020-10-10 17:00:08', '2020-10-12 11:20:20', '编辑记录', '7');
INSERT INTO `sys_function` VALUES ('41', 1, '2020-10-10 17:00:26', '2020-10-12 11:20:20', '查看记录', '7');
INSERT INTO `sys_function` VALUES ('42', 1, '2020-10-10 17:07:08', '2020-10-12 11:20:20', '接口配置', '7');
INSERT INTO `sys_function` VALUES ('43', 1, '2020-10-10 17:09:32', '2020-10-12 11:19:56', '添加记录', '8');
INSERT INTO `sys_function` VALUES ('44', 1, '2020-10-10 17:09:41', '2020-10-12 11:19:56', '删除记录', '8');
INSERT INTO `sys_function` VALUES ('45', 1, '2020-10-10 17:09:50', '2020-10-12 11:19:56', '编辑记录', '8');
INSERT INTO `sys_function` VALUES ('46', 1, '2020-10-10 17:10:03', '2020-10-12 11:19:56', '查看记录', '8');
INSERT INTO `sys_function` VALUES ('47', 1, '2020-10-10 17:10:26', '2020-10-12 11:19:56', '分页查询', '8');
INSERT INTO `sys_function` VALUES ('48', 1, '2020-10-10 17:32:27', '2020-10-12 11:20:37', '角色功能配置', '6');
INSERT INTO `sys_function` VALUES ('49', 1, '2020-10-10 17:32:46', '2020-10-12 11:20:37', '角色菜单配置', '6');
INSERT INTO `sys_function` VALUES ('5', 1, '2020-08-20 16:34:56', '2020-10-12 11:19:33', '查看记录', '2');
INSERT INTO `sys_function` VALUES ('6', 1, '2020-08-25 18:02:11', '2020-10-12 11:20:06', '添加记录', '3');
INSERT INTO `sys_function` VALUES ('7', 1, '2020-08-25 18:02:18', '2020-10-12 11:20:06', '分页查询', '3');
INSERT INTO `sys_function` VALUES ('8', 1, '2020-08-25 18:02:36', '2020-10-12 11:20:06', '删除记录', '3');
INSERT INTO `sys_function` VALUES ('9', 1, '2020-08-25 18:02:52', '2020-10-12 11:20:06', '编辑记录', '3');
INSERT INTO `sys_function` VALUES ('927666869b94e95d38441c514f27ad7b', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '新增微信小程序用户', 'WxMiniAppUser_manage');
INSERT INTO `sys_function` VALUES ('b3e20460e2610b7b105fcfd84a377746', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '更新微信小程序用户', 'WxMiniAppUser_manage');
INSERT INTO `sys_function` VALUES ('c4a144690d12badb21340864a6787cd5', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '查看微信小程序用户详情', 'WxMiniAppUser_manage');
INSERT INTO `sys_function` VALUES ('d0cc49fdf58cd983fe5d44ed5f099f43', 1, '2021-01-22 08:22:36', '2021-01-22 08:22:36', '查看个人详情', '9');

-- ----------------------------
-- Table structure for sys_function_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_function_api`;
CREATE TABLE `sys_function_api`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `function_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '系统功能 唯一标识',
  `api_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '系统接口url 唯一标识',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统功能与接口url对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function_api
-- ----------------------------
INSERT INTO `sys_function_api` VALUES ('00cb9b69225ac92b82092e51a0602579', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '5f44e6eb47283ce1782b6c1ad5968ae1', 'd40fa0fc0f791f5cc35d8165da786784');
INSERT INTO `sys_function_api` VALUES ('0b2837d3aed883b750b5b632c41ffa96', 1, '2021-01-22 02:48:19', '2021-01-22 02:48:19', '29', '42');
INSERT INTO `sys_function_api` VALUES ('0ded7743cfb1f1977ba7b69a47dca7a3', 1, '2021-01-18 07:40:26', '2021-01-18 07:40:26', '33', 'd8fa984d6b02c9f57be9ef6788de62ec');
INSERT INTO `sys_function_api` VALUES ('0e24785487e0f7ed3e468377a1b742b5', 1, '2021-01-18 07:57:09', '2021-01-18 07:57:09', '8', '8b2c43c9ae19e2bc37ca025b96e56c20');
INSERT INTO `sys_function_api` VALUES ('100', 1, '2020-10-10 16:51:39', '2020-10-10 16:51:39', '22', '18');
INSERT INTO `sys_function_api` VALUES ('101', 1, '2020-10-10 16:52:02', '2020-10-10 16:52:02', '23', '17');
INSERT INTO `sys_function_api` VALUES ('102', 1, '2020-10-10 16:52:02', '2020-10-10 16:52:02', '23', '18');
INSERT INTO `sys_function_api` VALUES ('104', 1, '2020-10-10 16:52:33', '2020-10-10 16:52:33', '25', '19');
INSERT INTO `sys_function_api` VALUES ('105', 1, '2020-10-10 16:52:33', '2020-10-10 16:52:33', '25', '21');
INSERT INTO `sys_function_api` VALUES ('106', 1, '2020-10-10 16:52:33', '2020-10-10 16:52:33', '25', '18');
INSERT INTO `sys_function_api` VALUES ('107', 1, '2020-10-10 16:52:43', '2020-10-10 16:52:43', '26', '19');
INSERT INTO `sys_function_api` VALUES ('108', 1, '2020-10-10 16:52:43', '2020-10-10 16:52:43', '26', '18');
INSERT INTO `sys_function_api` VALUES ('109', 1, '2020-10-10 16:54:10', '2020-10-10 16:54:10', '27', '44');
INSERT INTO `sys_function_api` VALUES ('110', 1, '2020-10-10 16:54:22', '2020-10-10 16:54:22', '28', '41');
INSERT INTO `sys_function_api` VALUES ('113', 1, '2020-10-10 16:56:08', '2020-10-10 16:56:08', '30', '43');
INSERT INTO `sys_function_api` VALUES ('114', 1, '2020-10-10 16:56:08', '2020-10-10 16:56:08', '30', '45');
INSERT INTO `sys_function_api` VALUES ('115', 1, '2020-10-10 16:56:19', '2020-10-10 16:56:19', '31', '45');
INSERT INTO `sys_function_api` VALUES ('116', 1, '2020-10-10 16:58:30', '2020-10-10 16:58:30', '32', '25');
INSERT INTO `sys_function_api` VALUES ('118', 1, '2020-10-10 16:58:44', '2020-10-10 16:58:44', '34', '26');
INSERT INTO `sys_function_api` VALUES ('119', 1, '2020-10-10 16:58:44', '2020-10-10 16:58:44', '34', '29');
INSERT INTO `sys_function_api` VALUES ('120', 1, '2020-10-10 16:58:49', '2020-10-10 16:58:49', '35', '29');
INSERT INTO `sys_function_api` VALUES ('121', 1, '2020-10-10 16:58:57', '2020-10-10 16:58:57', '36', '28');
INSERT INTO `sys_function_api` VALUES ('130', 1, '2020-10-10 17:09:04', '2020-10-10 17:09:04', '42', '46');
INSERT INTO `sys_function_api` VALUES ('131', 1, '2020-10-10 17:09:04', '2020-10-10 17:09:04', '42', '47');
INSERT INTO `sys_function_api` VALUES ('140', 1, '2020-10-10 17:28:48', '2020-10-10 17:28:48', '37', '48');
INSERT INTO `sys_function_api` VALUES ('141', 1, '2020-10-10 17:28:48', '2020-10-10 17:28:48', '37', '34');
INSERT INTO `sys_function_api` VALUES ('142', 1, '2020-10-10 17:29:23', '2020-10-10 17:29:23', '38', '48');
INSERT INTO `sys_function_api` VALUES ('143', 1, '2020-10-10 17:29:23', '2020-10-10 17:29:23', '38', '30');
INSERT INTO `sys_function_api` VALUES ('144', 1, '2020-10-10 17:29:37', '2020-10-10 17:29:37', '40', '48');
INSERT INTO `sys_function_api` VALUES ('145', 1, '2020-10-10 17:29:37', '2020-10-10 17:29:37', '40', '31');
INSERT INTO `sys_function_api` VALUES ('146', 1, '2020-10-10 17:29:37', '2020-10-10 17:29:37', '40', '32');
INSERT INTO `sys_function_api` VALUES ('147', 1, '2020-10-10 17:29:46', '2020-10-10 17:29:46', '41', '48');
INSERT INTO `sys_function_api` VALUES ('148', 1, '2020-10-10 17:29:46', '2020-10-10 17:29:46', '41', '32');
INSERT INTO `sys_function_api` VALUES ('149', 1, '2020-10-10 17:30:09', '2020-10-10 17:30:09', '43', '48');
INSERT INTO `sys_function_api` VALUES ('150', 1, '2020-10-10 17:30:09', '2020-10-10 17:30:09', '43', '37');
INSERT INTO `sys_function_api` VALUES ('151', 1, '2020-10-10 17:30:16', '2020-10-10 17:30:16', '45', '48');
INSERT INTO `sys_function_api` VALUES ('152', 1, '2020-10-10 17:30:16', '2020-10-10 17:30:16', '45', '38');
INSERT INTO `sys_function_api` VALUES ('153', 1, '2020-10-10 17:30:16', '2020-10-10 17:30:16', '45', '40');
INSERT INTO `sys_function_api` VALUES ('154', 1, '2020-10-10 17:30:27', '2020-10-10 17:30:27', '46', '48');
INSERT INTO `sys_function_api` VALUES ('155', 1, '2020-10-10 17:30:27', '2020-10-10 17:30:27', '46', '40');
INSERT INTO `sys_function_api` VALUES ('156', 1, '2020-10-10 17:30:37', '2020-10-10 17:30:37', '47', '48');
INSERT INTO `sys_function_api` VALUES ('157', 1, '2020-10-10 17:30:37', '2020-10-10 17:30:37', '47', '36');
INSERT INTO `sys_function_api` VALUES ('160', 1, '2020-10-10 18:07:05', '2020-10-10 18:07:05', '48', '50');
INSERT INTO `sys_function_api` VALUES ('161', 1, '2020-10-10 18:07:05', '2020-10-10 18:07:05', '48', '52');
INSERT INTO `sys_function_api` VALUES ('162', 1, '2020-10-10 18:07:18', '2020-10-10 18:07:18', '49', '49');
INSERT INTO `sys_function_api` VALUES ('163', 1, '2020-10-10 18:07:18', '2020-10-10 18:07:18', '49', '51');
INSERT INTO `sys_function_api` VALUES ('1e6901e05536c440c4ee3b72c7bd5293', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '636cd1983aa9a8bad08c869f89ab4044', '8f716ba87cae14fb7ac4538771e31410');
INSERT INTO `sys_function_api` VALUES ('20246a495310e4333f89b610f1e542d8', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'e072d4176d5e119d780cc511a45a56a1', 'a3a4901b862a3ecccde35b6bd4fc0951');
INSERT INTO `sys_function_api` VALUES ('27e9fe56b92d447ba86c8e020dd79bbf', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '0d06ca37272a35c9a1f7a135c7abbc9f', '7936550a13967131d70cf0aa92f400d5');
INSERT INTO `sys_function_api` VALUES ('2cf3e8255a6ebeeea1c9b0cff8d6a693', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '4dd3a0f6f6547d4f99cea595cebb3a8f', 'deb2f23bfffb193e5cc6ef20241b2e6a');
INSERT INTO `sys_function_api` VALUES ('30c54e9f4a67d8b76f932b3023eba29a', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '5c0989624adfa0b5d7ea36b13a144255', '43dbc6f639d1b2e3dce329985a22b2d3');
INSERT INTO `sys_function_api` VALUES ('374142d9b3c1603d64d1751c4285af08', 1, '2021-01-22 01:43:25', '2021-01-22 01:43:25', '3', '39d2bf20a058310d0e5926604b2e65f1');
INSERT INTO `sys_function_api` VALUES ('3c1b415fe980be6e9cc8753767857f01', 1, '2021-01-18 07:57:09', '2021-01-18 07:57:09', '8', '18');
INSERT INTO `sys_function_api` VALUES ('3d697062b1b67aa1d42d43567726d574', 1, '2021-03-11 01:37:06', '2021-03-11 09:42:28', 'c6b18a0ad9123decfa57b4596f26b533', '7fadb5d098ecc580c585f4b71bb2d520');
INSERT INTO `sys_function_api` VALUES ('3d74cb62c6591f1e880b35d432ad3750', 1, '2021-01-20 02:16:30', '2021-01-20 02:16:30', '24', 'e57b786e332214664bcbce43fa85368f');
INSERT INTO `sys_function_api` VALUES ('49a97279b9fd55d70abd8519ea0e2e1f', 1, '2021-01-22 02:05:39', '2021-01-22 02:05:39', '44', '39');
INSERT INTO `sys_function_api` VALUES ('49ace183897ce64b3fc7b21b82768e3e', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'fa3920f5ea9ada3c257211e97878f791', 'b30622e2eb1060b3ab1a3780d70d06cb');
INSERT INTO `sys_function_api` VALUES ('4c38e65772035959e7acd57dfd9c3f67', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '5c0989624adfa0b5d7ea36b13a144255', 'ad05c1df705734e7c596df6f52375190');
INSERT INTO `sys_function_api` VALUES ('4eaf5c6343e2179e8752e5e2c000e4ca', 1, '2021-01-20 02:53:49', '2021-01-20 02:53:49', '39', '33');
INSERT INTO `sys_function_api` VALUES ('4f939d33493dca4697137123c98ad9e2', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '01924c9eb0c4f70ef28189c902da033f', '6d304e8fefaa468a3b66a98e8a44e8ee');
INSERT INTO `sys_function_api` VALUES ('548d7458bd18b7eb6b842631b2052b32', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', '2796817e48281336508c35e72d25e748', 'c8e03adb530acf14649233b14f4c5c33');
INSERT INTO `sys_function_api` VALUES ('58', 1, '2020-10-09 16:46:40', '2020-10-09 16:46:40', '18', '1');
INSERT INTO `sys_function_api` VALUES ('5ab8083761cefaba3e06a08769f01840', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', 'c8ba3ac16ca59c1bcbbbad36f5379639', 'b686550ed0666439248a43014e4cc2a9');
INSERT INTO `sys_function_api` VALUES ('622af00eb07be77263f168d7ae888c71', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', 'c4a144690d12badb21340864a6787cd5', 'a3f516caafb31762f7aab10ea9551cd3');
INSERT INTO `sys_function_api` VALUES ('64e4de2032a9a7b66cc0a120d541c731', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '0d06ca37272a35c9a1f7a135c7abbc9f', 'e655b572512ce72f304dbceeeeb5325c');
INSERT INTO `sys_function_api` VALUES ('67', 1, '2020-10-10 15:32:35', '2020-10-10 15:32:35', '4', '1');
INSERT INTO `sys_function_api` VALUES ('68', 1, '2020-10-10 15:32:35', '2020-10-10 15:32:35', '4', '3');
INSERT INTO `sys_function_api` VALUES ('6823bfa9ea59cd89a6fa628a2a55a9c4', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', 'b3e20460e2610b7b105fcfd84a377746', '81ca2fc5e0a42d38254e232bcbc67bf3');
INSERT INTO `sys_function_api` VALUES ('688e6c00173ff40ab28d171e3b00cdf6', 1, '2021-01-22 01:43:25', '2021-01-22 01:43:25', '3', '6');
INSERT INTO `sys_function_api` VALUES ('69', 1, '2020-10-10 15:32:35', '2020-10-10 15:32:35', '4', '5');
INSERT INTO `sys_function_api` VALUES ('6be2c7b5f12395c615c54d9b4311389d', 1, '2021-01-22 08:24:06', '2021-01-22 08:24:06', 'd0cc49fdf58cd983fe5d44ed5f099f43', '45');
INSERT INTO `sys_function_api` VALUES ('70', 1, '2020-10-10 15:33:11', '2020-10-10 15:33:11', '5', '1');
INSERT INTO `sys_function_api` VALUES ('7012618acf1fe7d4f22e099869d148a9', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'ff4a8a6bba997e4509ca37adddd3723a', '73ab249a404f94a44f521fdc1c120696');
INSERT INTO `sys_function_api` VALUES ('71', 1, '2020-10-10 15:33:11', '2020-10-10 15:33:11', '5', '3');
INSERT INTO `sys_function_api` VALUES ('72484ba9e8a95997f76de1c8b4c50bdb', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '3620cb3c93f00999722f66dbd2ce904a', 'a3f516caafb31762f7aab10ea9551cd3');
INSERT INTO `sys_function_api` VALUES ('725d10b64639115c2f98c27a9ca532a4', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'e072d4176d5e119d780cc511a45a56a1', '494f1ed40203a71d21363d4944c889e0');
INSERT INTO `sys_function_api` VALUES ('74', 1, '2020-10-10 16:42:39', '2020-10-10 16:42:39', '6', '18');
INSERT INTO `sys_function_api` VALUES ('75', 1, '2020-10-10 16:42:39', '2020-10-10 16:42:39', '6', '8');
INSERT INTO `sys_function_api` VALUES ('76', 1, '2020-10-10 16:42:56', '2020-10-10 16:42:56', '7', '18');
INSERT INTO `sys_function_api` VALUES ('77', 1, '2020-10-10 16:42:56', '2020-10-10 16:42:56', '7', '13');
INSERT INTO `sys_function_api` VALUES ('7edda7d787cddde402f4977d6b88e750', 1, '2021-01-18 07:57:09', '2021-01-18 07:57:09', '8', '10');
INSERT INTO `sys_function_api` VALUES ('80ba89793f2073c8157d906aa53d68e2', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '8c08954e0e9354a4d34251db4e93f25a', '8f5666bd68c9cefab6b8bed691fedce6');
INSERT INTO `sys_function_api` VALUES ('82', 1, '2020-10-10 16:43:32', '2020-10-10 16:43:32', '10', '12');
INSERT INTO `sys_function_api` VALUES ('83', 1, '2020-10-10 16:43:41', '2020-10-10 16:43:41', '9', '18');
INSERT INTO `sys_function_api` VALUES ('84', 1, '2020-10-10 16:43:41', '2020-10-10 16:43:41', '9', '9');
INSERT INTO `sys_function_api` VALUES ('84396641f1a29678f8ec766de1edfa19', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'fa3920f5ea9ada3c257211e97878f791', 'b5f560ba86f675b0e885d2ed345afc19');
INSERT INTO `sys_function_api` VALUES ('85', 1, '2020-10-10 16:43:41', '2020-10-10 16:43:41', '9', '12');
INSERT INTO `sys_function_api` VALUES ('866360ba9c3c0ca5162407d723610f99', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', '9c1b4ea5586c3cd6eb9987a6ea714f4c', 'de79cf2e0f980497d8c8a3ff4c2faf8c');
INSERT INTO `sys_function_api` VALUES ('87dcb2b4b2b310aea449ddc39518254d', 1, '2021-01-22 02:05:39', '2021-01-22 02:05:39', '44', 'e620a9dac57eb07721577f51353ce3ac');
INSERT INTO `sys_function_api` VALUES ('88', 1, '2020-10-10 16:47:03', '2020-10-10 16:47:03', '2', '1');
INSERT INTO `sys_function_api` VALUES ('88d5767807a9dbbd2add45f0cfb0def1', 1, '2021-01-22 08:21:32', '2021-01-22 08:21:32', '1', '4');
INSERT INTO `sys_function_api` VALUES ('89', 1, '2020-10-10 16:47:03', '2020-10-10 16:47:03', '2', '2');
INSERT INTO `sys_function_api` VALUES ('91b931bede3d6e21fcd2118afbdae5d0', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '1e91a3641fadab46c52a486b161aaccc', '444e629778ee29e41a611d61ade6bba3');
INSERT INTO `sys_function_api` VALUES ('97748e561a01a208094a8be5986d1171', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '3620cb3c93f00999722f66dbd2ce904a', '27f2146d37bf260ead74ea0d2965b201');
INSERT INTO `sys_function_api` VALUES ('99', 1, '2020-10-10 16:51:39', '2020-10-10 16:51:39', '22', '20');
INSERT INTO `sys_function_api` VALUES ('9e17416b943cb64520022e7f8b2179d2', 1, '2021-01-22 01:43:25', '2021-01-22 01:43:25', '3', '1');
INSERT INTO `sys_function_api` VALUES ('a041291641c74524b92941d060a92575', 1, '2021-01-20 02:53:49', '2021-01-20 02:53:49', '39', '343fe86b824c44c89d388a32621cb599');
INSERT INTO `sys_function_api` VALUES ('a486ac79ef7d86f1e8f05877bf817461', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'cbf0ce651c20f405f64aa5d21022dcf6', 'b30622e2eb1060b3ab1a3780d70d06cb');
INSERT INTO `sys_function_api` VALUES ('a933099d5af7c0bd4fa373a89392aff2', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'c70d5c28deae3c0fd66c2c6fd7122da5', '3e9cbe14b083c2d18b3811d06e55e4af');
INSERT INTO `sys_function_api` VALUES ('adb06f6872e5717d130bd9d67255e511', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', '7ee55e3280e90a230f762bb3972e1fa3', '494f1ed40203a71d21363d4944c889e0');
INSERT INTO `sys_function_api` VALUES ('aef27aaf7d9f68dbc6b6876ede657c24', 1, '2021-01-25 07:32:12', '2021-01-25 07:32:12', '2482ebd3920566969202f3cc5010bd48', 'e9672ef999e72419ab2aeffa416d7ffa');
INSERT INTO `sys_function_api` VALUES ('b853d41646b9a72b0ea725c48373064e', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '1e91a3641fadab46c52a486b161aaccc', 'b686550ed0666439248a43014e4cc2a9');
INSERT INTO `sys_function_api` VALUES ('b9a74af726f123144b4c0a2efc3e7aaa', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', '2796817e48281336508c35e72d25e748', 'd925149b4389d8324ba186e2ae9fa420');
INSERT INTO `sys_function_api` VALUES ('c14998e991fa04d999d3bd25a0c1b1db', 1, '2021-01-20 02:16:30', '2021-01-20 02:16:30', '24', '22');
INSERT INTO `sys_function_api` VALUES ('c193501605c67ed54d804838a40417ca', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', '927666869b94e95d38441c514f27ad7b', '9a844f8c7fc63b978cd62b91efc849e3');
INSERT INTO `sys_function_api` VALUES ('c76e2f6e8f5aa5122d0aa709a61e37ab', 1, '2021-01-18 07:40:26', '2021-01-18 07:40:26', '33', '27');
INSERT INTO `sys_function_api` VALUES ('cd9c3c50451a00cf03c8d23f2aa32ad6', 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46', 'b3e20460e2610b7b105fcfd84a377746', '67739974d1b476d7213d6615d4293485');
INSERT INTO `sys_function_api` VALUES ('d3664af96ee14402b256c313865a7013', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '29873aa4986520384d369d253c3faa83', '833d11be607af1be0f35690c6687b6bb');
INSERT INTO `sys_function_api` VALUES ('d968dc4c230e9af6dc480016cbca250f', 1, '2021-01-22 08:21:32', '2021-01-22 08:21:32', '1', '1');
INSERT INTO `sys_function_api` VALUES ('dc9208991c8cde081a73bcba8cca7821', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', 'c7f86220c644ae898da6b2d9b8b300a8', '43dbc6f639d1b2e3dce329985a22b2d3');
INSERT INTO `sys_function_api` VALUES ('dfbea1c0d537b77ac6a846e5e885f015', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', '2a1a6bd01bc7d681b6099e9a324adaa1', '5f705745a8efca9366c7a233eb53e86f');
INSERT INTO `sys_function_api` VALUES ('e27a147246d2fc605da8920d6a38e2bd', 1, '2021-01-22 02:48:19', '2021-01-22 02:48:19', '29', 'ead5e8b0211701b6b7d8c775917348c7');
INSERT INTO `sys_function_api` VALUES ('e5ef40b3bf20f4eaea768b46deb4e06a', 1, '2021-03-11 01:37:07', '2021-03-11 09:42:28', 'ff4a8a6bba997e4509ca37adddd3723a', '788b120a48c4f0b74656c6c4af4aaf4c');
INSERT INTO `sys_function_api` VALUES ('fc57bc568e8ec17b9924c1b7284047b9', 1, '2021-01-22 08:24:06', '2021-01-22 08:24:06', 'd0cc49fdf58cd983fe5d44ed5f099f43', '396f3c5d818f22baa68b1eae2219422e');
INSERT INTO `sys_function_api` VALUES ('fef1272db805cfbd571211192f14edf3', 1, '2021-03-23 01:48:15', '2021-03-23 09:49:08', '4dd3a0f6f6547d4f99cea595cebb3a8f', '07d6e2375f81b6bd5a2145d9ae16fd2e');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '图标',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '访问路径',
  `parent_menu_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '父菜单唯一标识',
  `sort` bigint(20) NULL DEFAULT NULL COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('00f4e8837fb4819aa2c87142c3ff7a96', 'el-icon-grape', '/wxMiniAppUser/list', NULL, 900, '微信小程序用户管理', NULL, 1, '2021-03-26 10:37:46', '2021-03-26 10:37:46');
INSERT INTO `sys_menu` VALUES ('1', 'el-icon-user', '/system-user/list', '2', 1, '用户管理', '系统内置', 1, '2020-07-22 10:14:36', '2020-10-10 17:53:42');
INSERT INTO `sys_menu` VALUES ('2', 'el-icon-setting', '/system', NULL, 2, '系统设置', '系统内置', 1, '2020-07-22 10:15:33', '2021-01-18 11:54:27');
INSERT INTO `sys_menu` VALUES ('3', 'el-icon-collection', '/system-dict/list', '2', 3, '字典管理', '系统内置', 1, '2020-07-22 10:16:11', '2020-08-20 17:20:21');
INSERT INTO `sys_menu` VALUES ('3676adc910d807dc36c49297efbf330c', '', '/system-dictType/list', '2', 3, '字典类型管理', '', 1, '2021-01-18 04:05:10', '2021-01-18 04:05:10');
INSERT INTO `sys_menu` VALUES ('4', 'el-icon-s-home', '/home', NULL, 1, '首页', '系统内置', 1, '2020-07-22 17:06:40', '2021-01-18 11:54:30');
INSERT INTO `sys_menu` VALUES ('5', 'el-icon-info', '/about', NULL, 3, '关于', '系统内置', 1, '2020-07-22 17:08:54', '2021-01-18 11:54:32');
INSERT INTO `sys_menu` VALUES ('6', 'el-icon-lock', '/system-role/list', '2', 2, '角色管理', '系统内置', 1, '2020-07-30 11:32:33', '2020-08-20 17:20:24');
INSERT INTO `sys_menu` VALUES ('7', 'el-icon-menu', '/system-menu/list', '2', 4, '菜单管理', '系统内置', 1, '2020-08-04 09:35:05', '2020-08-20 17:20:26');
INSERT INTO `sys_menu` VALUES ('8', NULL, '/system-function/list', '2', 5, '功能管理', '系统内置', 1, '2020-09-27 10:18:44', '2020-09-27 10:18:47');
INSERT INTO `sys_menu` VALUES ('9', NULL, '/system-api/list', '2', 6, '接口管理', '系统内置', 1, '2020-09-27 17:27:40', '2020-09-27 17:29:08');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '角色备注',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', '--', 1, '2020-07-24 16:28:24', '2021-01-18 03:28:29');

-- ----------------------------
-- Table structure for sys_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_function`;
CREATE TABLE `sys_role_function`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `function_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '系统功能 唯一标识',
  `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '角色 唯一标识',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统角色与功能对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES ('00a06e573df4cff351c53d5f98816bce', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '24', '1');
INSERT INTO `sys_role_function` VALUES ('069e2e111c732d8bcc9599042083790a', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'cbf0ce651c20f405f64aa5d21022dcf6', '1');
INSERT INTO `sys_role_function` VALUES ('07e629a93540123561ed1c975fdfc4b1', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '4', '1');
INSERT INTO `sys_role_function` VALUES ('0959c5fdb4d153406de7db6ddba1cf7d', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'c70d5c28deae3c0fd66c2c6fd7122da5', '1');
INSERT INTO `sys_role_function` VALUES ('190e53169e0f03c75eb3a165d8166c24', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '49', '1');
INSERT INTO `sys_role_function` VALUES ('1e60dcc47cd8d5f1f6523ccbbd8807dd', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '42', '1');
INSERT INTO `sys_role_function` VALUES ('1e896480dd0d5bc73c1af9299dabbb66', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '3', '1');
INSERT INTO `sys_role_function` VALUES ('2696d7e78196c3cf0863fcae46dbd15a', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '9', '1');
INSERT INTO `sys_role_function` VALUES ('27230493b5a0f1e8af09a2012c5ef885', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'c7f86220c644ae898da6b2d9b8b300a8', '1');
INSERT INTO `sys_role_function` VALUES ('29cdff07fed4a5d422d36baf2bcf7e22', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '28', '1');
INSERT INTO `sys_role_function` VALUES ('2af44aabae0565ee3849d0008e53f55e', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '41', '1');
INSERT INTO `sys_role_function` VALUES ('2bce8bdbe8b8797581d57fb0d4213fce', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '5f44e6eb47283ce1782b6c1ad5968ae1', '1');
INSERT INTO `sys_role_function` VALUES ('2ebffb9d9d30ac5269a2cfca6026aa4f', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '8', '1');
INSERT INTO `sys_role_function` VALUES ('30d560477edf0877aa7a8fe2c9a6d153', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '0d06ca37272a35c9a1f7a135c7abbc9f', '1');
INSERT INTO `sys_role_function` VALUES ('30f8d12480ea47f7cd4f724672ee7a49', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '5c0989624adfa0b5d7ea36b13a144255', '1');
INSERT INTO `sys_role_function` VALUES ('32f525201f6a471339856173839846ba', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '40', '1');
INSERT INTO `sys_role_function` VALUES ('38fe3f771477f7cd02c608f541f8b66b', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '47', '1');
INSERT INTO `sys_role_function` VALUES ('3d200b65083d3f8182afdf2e4563fcfe', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '27', '1');
INSERT INTO `sys_role_function` VALUES ('3d95dc1406576d6dc86831aeea1999a8', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '636cd1983aa9a8bad08c869f89ab4044', '1');
INSERT INTO `sys_role_function` VALUES ('4a02a95b186779791423848deea1d299', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'ff4a8a6bba997e4509ca37adddd3723a', '1');
INSERT INTO `sys_role_function` VALUES ('4a67bc32936cbf12bd04d43f5f8aeccf', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '43', '1');
INSERT INTO `sys_role_function` VALUES ('4ce879ea0cab2b848189d83a4528ad26', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '30', '1');
INSERT INTO `sys_role_function` VALUES ('581175e77efd521061d917623c68c086', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '10', '1');
INSERT INTO `sys_role_function` VALUES ('585d3161cc16bc43a8dad9890d9b4e19', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '36', '1');
INSERT INTO `sys_role_function` VALUES ('609bc29f469441c4f439561e02d36ab8', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '2', '1');
INSERT INTO `sys_role_function` VALUES ('66f2cb8932251ee51170c2de89d2350e', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '22', '1');
INSERT INTO `sys_role_function` VALUES ('6a9c31f12b72034ce8caa98c9c877dbd', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '7ee55e3280e90a230f762bb3972e1fa3', '1');
INSERT INTO `sys_role_function` VALUES ('7193ccb1fe0ab138a04ff5691c10727e', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'c8ba3ac16ca59c1bcbbbad36f5379639', '1');
INSERT INTO `sys_role_function` VALUES ('74471549549621aec16baea8e11b758e', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '31', '1');
INSERT INTO `sys_role_function` VALUES ('772a7f9be1cdfc385f8a874683f1b448', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '5', '1');
INSERT INTO `sys_role_function` VALUES ('7b444784067cbb9bfb0702abc5034ee5', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'd0cc49fdf58cd983fe5d44ed5f099f43', '1');
INSERT INTO `sys_role_function` VALUES ('7d91642cf4ab490459865f66db7a8d0f', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '34', '1');
INSERT INTO `sys_role_function` VALUES ('870d1f84c68ed58bcefac1150625b6cb', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '9c1b4ea5586c3cd6eb9987a6ea714f4c', '1');
INSERT INTO `sys_role_function` VALUES ('87d3d5b68ef83104978eb2bd607e0030', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '23', '1');
INSERT INTO `sys_role_function` VALUES ('8be25f7af37e39e3af77e51e7538ec82', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '25', '1');
INSERT INTO `sys_role_function` VALUES ('96f76ec339d166ab9e781c6002b520fb', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '29873aa4986520384d369d253c3faa83', '1');
INSERT INTO `sys_role_function` VALUES ('9c980ac460df90e86cfbb0c618e56e38', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '48', '1');
INSERT INTO `sys_role_function` VALUES ('a09d2e9c96c55c9946b5b467b60cb9fa', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '2796817e48281336508c35e72d25e748', '1');
INSERT INTO `sys_role_function` VALUES ('a1329aae6bae0dc74d024f46ba4b0d99', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '29', '1');
INSERT INTO `sys_role_function` VALUES ('a319a0f83d1ef3017dd170cc99b56f42', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'e072d4176d5e119d780cc511a45a56a1', '1');
INSERT INTO `sys_role_function` VALUES ('aacafe5b7f2a892ebf6270a385631386', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '26', '1');
INSERT INTO `sys_role_function` VALUES ('b38ac460355a0e39f8885af6674969c5', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '46', '1');
INSERT INTO `sys_role_function` VALUES ('b4b1416a5aa8b8671ef00d670acac724', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '2482ebd3920566969202f3cc5010bd48', '1');
INSERT INTO `sys_role_function` VALUES ('b50fb1250f8bce268a05ac5de92ca81e', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '1', '1');
INSERT INTO `sys_role_function` VALUES ('bd993a6d776b64f023141120b1cdc844', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '2a1a6bd01bc7d681b6099e9a324adaa1', '1');
INSERT INTO `sys_role_function` VALUES ('c01f9dc692bc74ed5307d2b674f0f51d', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '32', '1');
INSERT INTO `sys_role_function` VALUES ('c03c0aa0b1a30fbf677f97352b023710', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '7', '1');
INSERT INTO `sys_role_function` VALUES ('c8fe0015c3425ecc133eb270f2d088d5', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '35', '1');
INSERT INTO `sys_role_function` VALUES ('c93456ff7bc497b6b3b7c323ddbd01a1', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '8c08954e0e9354a4d34251db4e93f25a', '1');
INSERT INTO `sys_role_function` VALUES ('ce17c1f4ecc86a2ea529dd11eb89a97e', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '6', '1');
INSERT INTO `sys_role_function` VALUES ('ced4dd6b980b5a73415a5e013caa9441', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'c6b18a0ad9123decfa57b4596f26b533', '1');
INSERT INTO `sys_role_function` VALUES ('d21b5849a20a571e797e7e5c44214c18', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '1e91a3641fadab46c52a486b161aaccc', '1');
INSERT INTO `sys_role_function` VALUES ('d4a8fa152a5d155b9bbd6f585ec2cac1', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '44', '1');
INSERT INTO `sys_role_function` VALUES ('d8d8226339dda03fc2a77645d45451da', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', 'fa3920f5ea9ada3c257211e97878f791', '1');
INSERT INTO `sys_role_function` VALUES ('dfece6615c8951ad204747dc7dff14dd', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '4dd3a0f6f6547d4f99cea595cebb3a8f', '1');
INSERT INTO `sys_role_function` VALUES ('e62cc67d3143069be297ef821a1282ca', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '33', '1');
INSERT INTO `sys_role_function` VALUES ('e841da444f2817b7085c0958077b4fdd', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '39', '1');
INSERT INTO `sys_role_function` VALUES ('f8645211ffafadf04fb8f011c54b4bb7', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '37', '1');
INSERT INTO `sys_role_function` VALUES ('f97bd1445da6646c5b75b202243449f1', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '45', '1');
INSERT INTO `sys_role_function` VALUES ('fcb4893d4e64f5debbda8f96a7a56cc7', 1, '2021-03-23 02:40:36', '2021-03-23 02:40:36', '38', '1');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `menu_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '菜单id',
  `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统角色与菜单对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('5e324b5798777af7231d6b046667ff21', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '8', '1');
INSERT INTO `sys_role_menu` VALUES ('6b3ba65548c60c1f4786ba90789ebc3b', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '3676adc910d807dc36c49297efbf330c', '1');
INSERT INTO `sys_role_menu` VALUES ('8b5007e9626cce55b6ad21923a4a5a74', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '4', '1');
INSERT INTO `sys_role_menu` VALUES ('b3f76471d95db2be2ad4b8304d793e9f', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '3', '1');
INSERT INTO `sys_role_menu` VALUES ('b4f860328948b8d16c5d26d0868fb4ba', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '6', '1');
INSERT INTO `sys_role_menu` VALUES ('c4707e466c78c00f0251d86f0870803c', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '2', '1');
INSERT INTO `sys_role_menu` VALUES ('ce669a27753fce479a03b4260556af96', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '9', '1');
INSERT INTO `sys_role_menu` VALUES ('e10236e3c507d39f3f0c10c4dd79df2e', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '7', '1');
INSERT INTO `sys_role_menu` VALUES ('e5a9919b9f8abdc94552089f8911cb3e', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('f8bf9e9c4216021dda68297ef48db298', 1, '2021-03-23 02:39:22', '2021-03-23 02:39:22', '5', '1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NOT NULL COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '记录创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '记录更新时间',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用于加密密码的盐',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `approve_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '审核状态',
  `approve_result` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '审核结果',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '用户名',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 1, '2020-07-24 09:11:06', '2021-05-20 11:44:54', 'zhangsan', '202CB962AC59075B964B07152D234B70', NULL, '13000989890', '1', '2', '2', '张三');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `valid` tinyint(1) NULL DEFAULT NULL COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `user_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '用户 唯一标识',
  `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '角色 唯一标识',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '系统用户与角色对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', 1, '2020-07-24 16:28:56', '2020-07-24 16:28:56', '1', '1');

-- ----------------------------
-- Table structure for wx_miniapp_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_miniapp_user`;
CREATE TABLE `wx_miniapp_user`  (
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '唯一标识(主键ID)',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '记录修改时间',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` int(11) NULL DEFAULT NULL COMMENT '性别',
  `language` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '语言',
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '城市',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `openid` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'openid',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '微信小程序用户' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
