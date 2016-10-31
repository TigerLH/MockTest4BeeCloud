/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : mock

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-10-29 00:41:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for response
-- ----------------------------
DROP TABLE IF EXISTS `response`;
CREATE TABLE `response` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `col` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `statuscode` varchar(255) DEFAULT NULL,
  `contentType` varchar(255) DEFAULT NULL,
  `userAgent` varchar(255) DEFAULT NULL,
  `response` text,
  `rule_id` int(1) DEFAULT NULL COMMENT '1为运行状态，0为停止状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of response
-- ----------------------------
INSERT INTO `response` VALUES ('115', 'Mock金币测试', 'wapsc.189.cn', 'http://wapsc.189.cn/api/lls/newQueryUserGoldDetail/77440FF66A3015F402CE5883F85E6012631EF84A1CF928854435A6385C22C211A8BF8E1335F1195A9B92BF53E3574BDDCAB55EDA04B092DC/1', 'undefined', 'GET', '200', 'undefined', 'undefined', '{\"result\":{\"code\":\"1\",\"message\":\"查询成功。\"},\"plusTotal\":\"12222\",\"userDetail\":[{\"custId\":\"12056066\",\"actionType\":\"PLUS\",\"actionCode\":\"SIGNINNEW\",\"goldNum\":\"2\",\"createTime\":\"2016/10/26\",\"description\":\"每日签到\"},{\"custId\":\"12056066\",\"actionType\":\"PLUS\",\"actionCode\":\"SIGNINNEW\",\"goldNum\":\"2\",\"createTime\":\"2016/08/19\",\"description\":\"每日签到\"},{\"custId\":\"12056066\",\"actionType\":\"PLUS\",\"actionCode\":\"SIGNINNEW\",\"goldNum\":\"2\",\"createTime\":\"2016/08/17\",\"description\":\"每日签到\"},{\"custId\":\"12056066\",\"actionType\":\"PLUS\",\"actionCode\":\"SIGNINNEW\",\"goldNum\":\"2\",\"createTime\":\"2016/08/15\",\"description\":\"每日签到\"},{\"custId\":\"12056066\",\"actionType\":\"PLUS\",\"actionCode\":\"SIGNINNEW\",\"goldNum\":\"2\",\"createTime\":\"2016/08/06\",\"description\":\"每日签到\"},{\"custId\":\"12056066\",\"actionType\":\"PLUS\",\"actionCode\":\"SIGNINNEW\",\"goldNum\":\"2\",\"createTime\":\"2016/08/04\",\"description\":\"每日签到\"}],\"minusTotal\":\"0\",\"page\":{\"nowPager\":1,\"pageSize\":10,\"totalRecords\":6,\"totalPage\":1,\"pageStart\":0,\"pageEnd\":10},\"message\":\"近三月收入了=============测试Mock，你被mock数据了=================金币，消耗了 55560 金币\"}', null);

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `response_id` int(11) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '1为可用，0为不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES ('14', '测试规则', '/newQueryUserGoldDetail/', '115', '1');
