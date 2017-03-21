/*
Navicat MariaDB Data Transfer

Source Server         : 120.108.204.235_test
Source Server Version : 100017
Source Database       : ntut_em_proj_01

Target Server Type    : MariaDB
Target Server Version : 100017
File Encoding         : 65001

Date: 2017-03-21 23:32:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_uid` int(11) NOT NULL,
  `tgt_uid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ref_tgt_lim` (`ref_uid`,`tgt_uid`) USING BTREE,
  KEY `tgt_uid_fk` (`tgt_uid`),
  CONSTRAINT `ref_uid_fk` FOREIGN KEY (`ref_uid`) REFERENCES `websites` (`id`),
  CONSTRAINT `tgt_uid_fk` FOREIGN KEY (`tgt_uid`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of relation
-- ----------------------------

-- ----------------------------
-- Table structure for websites
-- ----------------------------
DROP TABLE IF EXISTS `websites`;
CREATE TABLE `websites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url_hash` char(64) CHARACTER SET ascii NOT NULL COMMENT 'sha256',
  `title` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `url` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  `page_rank` double NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `view_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_hash` (`url_hash`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of websites
-- ----------------------------
INSERT INTO `websites` VALUES ('1', 'a767fc25c3b37c021ad7cb8ebe06e43d6dddb57e429ef885b963668695d723e2', '國立臺北科技大學Taipei Tech', 'http://www.ntut.edu.tw/bin/home.php', '0', '2017-03-21 21:42:53', NULL);
SET FOREIGN_KEY_CHECKS=1;
