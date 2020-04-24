/*
Navicat MySQL Data Transfer

Source Server         : 本机本地
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : spring-security-test

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2020-04-19 19:47:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_perms
-- ----------------------------
DROP TABLE IF EXISTS `t_perms`;
CREATE TABLE `t_perms` (
  `permsId` int(11) NOT NULL COMMENT '权限Id',
  `permsName` varchar(50) NOT NULL COMMENT '权限名称',
  `permsDesc` varchar(255) NOT NULL COMMENT '权限描述',
  PRIMARY KEY (`permsId`),
  UNIQUE KEY `Index_1` (`permsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of t_perms
-- ----------------------------
INSERT INTO `t_perms` VALUES ('1', 'ADD', '增加');
INSERT INTO `t_perms` VALUES ('2', 'DELTE', '删除');
INSERT INTO `t_perms` VALUES ('3', 'UPDATE', '更改');
INSERT INTO `t_perms` VALUES ('4', 'LIST', '查询');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `roleId` int(11) NOT NULL COMMENT '角色Id',
  `roleName` varchar(50) NOT NULL COMMENT '角色名称',
  `roleDesc` varchar(255) NOT NULL COMMENT '角色描述',
  PRIMARY KEY (`roleId`),
  UNIQUE KEY `Index_1` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'ROLE_SUPERADMIN', '超级管理员');
INSERT INTO `t_role` VALUES ('2', 'ROLE_ADMIN', '管理员');
INSERT INTO `t_role` VALUES ('3', 'ROLE_USER', '普通用户');

-- ----------------------------
-- Table structure for t_role_perms
-- ----------------------------
DROP TABLE IF EXISTS `t_role_perms`;
CREATE TABLE `t_role_perms` (
  `roleId` int(11) DEFAULT NULL COMMENT '角色Id',
  `permsId` int(11) DEFAULT NULL COMMENT '权限Id',
  KEY `FK_Reference_3` (`roleId`),
  KEY `FK_Reference_4` (`permsId`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`roleId`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`permsId`) REFERENCES `t_perms` (`permsId`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_perms
-- ----------------------------
INSERT INTO `t_role_perms` VALUES ('1', '1');
INSERT INTO `t_role_perms` VALUES ('1', '2');
INSERT INTO `t_role_perms` VALUES ('1', '4');
INSERT INTO `t_role_perms` VALUES ('1', '3');
INSERT INTO `t_role_perms` VALUES ('2', '3');
INSERT INTO `t_role_perms` VALUES ('2', '4');
INSERT INTO `t_role_perms` VALUES ('3', '4');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL COMMENT '用户Id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `accountNonExpired` tinyint(1) NOT NULL COMMENT '账户是否过期',
  `accountNonLocked` tinyint(1) NOT NULL COMMENT '账户是否锁定',
  `credentialsNonExpired` tinyint(1) NOT NULL COMMENT '凭证是否过期',
  `enabled` tinyint(1) NOT NULL COMMENT '账户是否启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'wlp', '$2a$10$16X7CDBDydGOMj6QgQbJkuoUVBvuKJjxpR4yJ81G/15sGJDVR.P7q', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `userId` bigint(20) DEFAULT NULL COMMENT '用户id',
  `roleId` int(11) DEFAULT NULL COMMENT '角色id',
  KEY `FK_Reference_1` (`userId`),
  KEY `FK_Reference_2` (`roleId`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`roleId`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '2');
