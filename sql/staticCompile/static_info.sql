/*
 Navicat Premium Data Transfer

 Source Server         : 127
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : hxyframe_activiti

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 30/05/2021 15:07:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for static_info
-- ----------------------------
DROP TABLE IF EXISTS `static_info`;
CREATE TABLE `static_info`  (
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INFO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`NID`) USING BTREE,
  UNIQUE INDEX `NID`(`NID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;


INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10001', '活动', NULL, '受理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10002', '数据', '10001', '人民监督员监督案件受理登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10003', '活动', '10001', '审查', '对受理后的材料进行审查办理');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10004', '数据', '10003', '补充移送材料通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10005', '数据', '10003', '人民监督员监督案件审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10006', '活动', '10003', '移送其他院', '不属于本院管辖的移送有管辖权的人民检察院');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10007', '数据', '10006', '移送函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10008', '活动', '10003', '组织监督评议', '审查后，组织确定人民监督员进行评议和表决');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10009', '数据', '10008', '人民监督员监督案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10010', '活动', '10008', '告知表决意见', '将表决意见告知本院业务部门或者下级院');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10011', '数据', '10010', '人民监督员表决意见通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10012', '活动', '10010', '反馈处理结果', '将本院业务部门或下级院的处理决定反馈给人民监督员');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事检察', '10013', '数据', '10012', '人民监督员监督案件处理结果通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11001', '活动', NULL, '受理', '受理新的起诉案件');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11002', '数据', '11001', '接收案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11003', '数据', '11001', '接收案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11004', '活动', '11001', '分案', '案管部门或部门领导、内勤将案件分配给承办人');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11005', '活动', '11004', '审查起诉', '承办人接受案件后，进行案件办理');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11006', '数据', '11005', '公诉案件审查报告', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11007', '活动', '11004', '退回补充侦查', '审查起诉的并行节点，该节点为送案节点');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11008', '数据', '11007', '补充侦查决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11009', '数据', '11007', '退查提纲', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11010', '数据', '11007', '换押证', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11011', '活动', '11005', '起诉（含部分不起诉）', '检察院对认为是犯罪的人提出指控');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11012', '数据', '11011', '起诉书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11013', '活动', '11011', '全案撤回起诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11014', '数据', '11013', '撤回起诉决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11015', '活动', '11005', '全案不起诉', '不起诉是检察院在审查起诉后做出不将案件移送人民法院进行审判从而终止诉讼决定');
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11016', '数据', '11015', '不起诉决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11017', '活动', '11005', '建议撤案（针对自侦）', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11018', '数据', '11017', '撤案建议书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11019', '活动', '11005', '改变管辖', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11020', '数据', '11019', '交办案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11021', '数据', '11019', '改变管辖通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11022', '数据', '11019', '报送（移送）案件意见书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11023', '活动', '11005', '并案', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11024', '数据', '11023', '合并案件审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11025', '活动', '11005', '拆案', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11026', '数据', '11025', '拆分案件审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11027', '活动', '11005', '出庭公诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11028', '数据', '11027', '公诉意见', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11029', '活动', '11027', '裁判结果审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11030', '数据', '11029', '对法院判决、裁定审查表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11031', '活动', '11029', '提出抗诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11032', '数据', '11031', '刑事抗诉书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('公益诉讼', '11033', '数据', '11031', '提请抗诉报告书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12001', '活动', NULL, '受理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12002', '数据 ', '12001', '基本情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12003', '活动', '12001', '查阅收监材料', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12004', '活动', '12001', '收押现场检察', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12005', '活动', '12001', '新犯监区巡视检察', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12006', '活动', '12001', '法制宣传教育', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12007', '活动', '12005', '存在执法或执法不规范', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12008', '活动', '12007', '转纠正违法流程', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12009', '活动', '12005', '发现案件线索', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12010', '活动', '12009', '转查办和预防职务犯罪流程', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12011', '活动', '12005', '受理控告、举报和申诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('刑事执行', '12012', '活动', '12011', '转控告、举报和申诉流程', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13001', '活动', NULL, '受理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13002', '数据', '13001', '民事案件收案登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13003', '活动', '13001', '初步审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13004', '数据', '13003', '初步审查报告', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13005', '数据', '13003', '审查核实审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13006', '活动', '13003', '不支持监督申请', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13007', '数据', '13006', '不支持监督申请决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13008', '活动', '13003', '转办下级院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13009', '数据', '13008', '转办函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13010', '活动', '13003', '交办下级院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13011', '数据', '13010', '交办函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13012', '数据', '13010', '交办通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13013', '活动', '13003', '移送其他检察院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13014', '数据', '13013', '函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13015', '活动', '13003', '进一步审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13016', '数据', '13015', '审查终结报告', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13017', '活动', '13015', '提出抗诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13018', '数据', '13017', '民事抗诉书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13019', '数据', '13017', '检察建议书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13020', '数据', '13017', '纠正违法通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13021', '数据', '13017', '通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13022', '数据', '13017', '再审情况审查登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13023', '活动', '13017', '出席再审法庭', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13024', '数据', '13023', '出庭通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13025', '数据', '13023', '指令出庭通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13026', '活动', '13015', '提请抗诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13027', '数据', '13026', '提请抗诉报告书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13028', '活动', '13015', '提出再审检察建议', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13029', '数据', '13028', '再审检察建议书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13030', '数据', '13028', '再审检察建议采纳情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13031', '活动', '13015', '提出检察建议', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13032', '数据', '13031', '检察建议采纳情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13033', '活动', '13015', '提出纠正违法通知', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13034', '数据', '13033', '纠正违法通知采纳情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13035', '活动', '13015', '不支持监督申请', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13036', '数据', '13035', '不支持监督申请决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13037', '活动', '13015', '终结审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('民事', '13038', '数据', '13037', '终结审查决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14001', '活动', NULL, '受理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14002', '数据', '14001', '行政案件收案登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14003', '活动', '14001', '初步审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14004', '数据', '14003', '初步审查报告', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14005', '数据', '14003', '审查核实审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14006', '活动', '14003', '不支持监督申请', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14007', '数据', '14006', '不支持监督申请决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14008', '活动', '14003', '转办下级院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14009', '数据', '14008', '转办函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14010', '活动', '14003', '交办下级院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14011', '数据', '14010', '交办函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14012', '数据', '14010', '交办通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14013', '活动', '14003', '移送其他检察院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14014', '数据', '14013', '函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14015', '活动', '14003', '进一步审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14016', '数据', '14015', '审查终结报告', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14017', '活动', '14015', '提出抗诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14018', '数据', '14017', '行政抗诉书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14019', '数据', '14017', '检察建议书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14020', '数据', '14017', '纠正违法通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14021', '数据', '14017', '通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14022', '数据', '14017', '再审情况审查登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14023', '活动', '14017', '出席再审法庭', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14024', '数据', '14023', '出庭通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14025', '数据', '14023', '指令出庭通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14026', '活动', '14015', '提请抗诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14027', '数据', '14026', '提请抗诉报告书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14028', '活动', '14015', '提出再审检察建议', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14029', '数据', '14028', '再审检察建议书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14030', '数据', '14028', '再审检察建议采纳情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14031', '活动', '14015', '提出检察建议', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14032', '数据', '14031', '检察建议采纳情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14033', '活动', '14015', '提出纠正违法通知', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14034', '数据', '14033', '纠正违法通知采纳情况登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14035', '活动', '14015', '不支持监督申请', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14036', '数据', '14035', '不支持监督申请决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14037', '活动', '14015', '终结审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('行政', '14038', '数据', '14037', '终结审查决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15001', '活动', NULL, '受理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15002', '数据', '15001', '接收案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15003', '数据', '15001', '接收案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15004', '活动', '15001', '分案', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15005', '活动', '15004', '审查起诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15006', '数据', '15005', '公诉案件审查报告', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15007', '活动', '15004', '退回补充侦查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15008', '数据', '15007', '补充侦查决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15009', '数据', '15007', '退查提纲', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15010', '数据', '15007', '换押证', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15011', '活动', '15005', '起诉（含部分不起诉）', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15012', '数据', '15011', '起诉书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15013', '活动', '15011', '全案撤回起诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15014', '数据', '15013', '撤回起诉决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15015', '活动', '15005', '全案不起诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15016', '数据', '15015', '不起诉决定书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15017', '活动', '15005', '建议撤案（针对自侦）', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15018', '数据', '15017', '撤案建议书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15019', '活动', '15005', '改变管辖', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15020', '数据', '15019', '交办案件通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15021', '数据', '15019', '改变管辖通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15022', '数据', '15019', '报送（移送）案件意见书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15023', '活动', '15005', '并案', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15024', '数据', '15023', '合并案件审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15025', '活动', '15005', '拆案', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15026', '数据', '15025', '拆分案件审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15027', '活动', '15005', '出庭公诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15028', '数据', '15027', '公诉意见', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15029', '活动', '15027', '裁判结果审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15030', '数据', '15029', '对法院判决、裁定审查表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15031', '活动', '15029', '提出抗诉', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15032', '数据', '15031', '刑事抗诉书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('未检业务', '15033', '数据', '15031', '提请抗诉报告书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16001', '活动', NULL, '受理审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16002', '数据', '16001', '来信来访登记表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16003', '活动', '16001', '转本院其他部门', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16004', '数据', '16003', '交办信访函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16005', '活动', '16001', '交办下级检察院', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16006', '数据', '16005', '人民检察院信访交办函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16007', '活动', '16006', '交办结果审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16008', '数据', '16007', '纠正案件错误通知书', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16009', '活动', '16001', '移送其他单位', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16010', '数据', '16009', '人民检察院信访转办函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16011', '活动', '16001', '直接答复', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16012', '数据', '16011', '答复函', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16013', '活动', '16001', '直接办理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('控告申诉', '16014', '数据', '16013', '审查阻碍依法行使诉讼权利事项查审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17001', '活动', NULL, '提交', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17002', '数据', '17001', '检察委员会议题', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17003', '数据', '17001', '提交检察委员会讨论议题审批表', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17004', '活动', '17001', '受理', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17005', '活动', '17004', '指定承办人', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17006', '活动', '17005', '议题审查', NULL);
INSERT INTO `static_info` (`CATEGORY`, `NID`, `TYPE`, `PID`, `NAME`, `INFO`) VALUES ('检委办', '17007', '数据', '17006', '检委办审查意见', NULL);
