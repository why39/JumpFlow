/*
 Navicat MySQL Data Transfer

 Source Server         : hxyframe_activiti
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : hxyframe_activiti

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 10/05/2021 19:08:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_dq_ak
-- ----------------------------
DROP TABLE IF EXISTS `tb_dq_ak`;
CREATE TABLE `tb_dq_ak` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `AFD_MC` varchar(255) DEFAULT NULL COMMENT '案发地',
  `CBDW_MC` varchar(255) DEFAULT NULL COMMENT '承办单位',
  `CBDW_BM` int(255) DEFAULT NULL COMMENT '承办单位编码',
  `AJLB_MC` varchar(255) DEFAULT NULL COMMENT '案件类别名称',
  `AJLB_BM` int(255) DEFAULT NULL COMMENT '案件类别编码',
  `AY_MC` varchar(255) DEFAULT NULL COMMENT '案由名称',
  `AY_DM` int(255) DEFAULT NULL COMMENT '案由代码',
  `BMSAH` varchar(255) DEFAULT NULL COMMENT '部门受案号',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- Table structure for tb_dq_fzxyr
-- ----------------------------
DROP TABLE IF EXISTS `tb_dq_fzxyr`;
CREATE TABLE `tb_dq_fzxyr` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `xyrxm` varchar(255) DEFAULT NULL COMMENT '嫌疑人姓名',
  `xyrbh` int(255) DEFAULT NULL COMMENT '嫌疑人编号',
  `afsnl` int(255) DEFAULT NULL COMMENT '案发时年龄',
  `xb` varchar(5) DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
