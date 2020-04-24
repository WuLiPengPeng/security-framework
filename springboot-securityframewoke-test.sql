/*
Navicat MySQL Data Transfer

Source Server         : 本机本地
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : springboot-securityframewoke-test

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2020-04-19 19:47:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL COMMENT 'id主键',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `perm` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'wlp', '$2a$10$16X7CDBDydGOMj6QgQbJkuoUVBvuKJjxpR4yJ81G/15sGJDVR.P7q', 'user:add');
INSERT INTO `t_user` VALUES ('2', 'ygf', '$2a$10$16X7CDBDydGOMj6QgQbJkuoUVBvuKJjxpR4yJ81G/15sGJDVR.P7q', 'user:update');
