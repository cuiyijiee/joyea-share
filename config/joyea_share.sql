/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : joyea_share

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 07/05/2019 19:36:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
  `album_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `album_name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `album_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `shared` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`album_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of album
-- ----------------------------
BEGIN;
INSERT INTO `album` VALUES (5, 696047, '23', '', '2019-05-05 23:39:45', NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for album_src
-- ----------------------------
DROP TABLE IF EXISTS `album_src`;
CREATE TABLE `album_src` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `album_id` int(11) NOT NULL,
  `src_neid` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_rev` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_path` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `src_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `src_size` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `src_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of album_src
-- ----------------------------
BEGIN;
INSERT INTO `album_src` VALUES (16, 5, '391664415', '5c3565a58f525f408b6a164cfd317a1a', '47cce0eb3c9c4a64ab22902789a95602', '/各部门全共享资料/财务部/仅一LOGO/9.JOYEA仅一1-反黑.gif', '', '11.5 KB', '', '2019-05-05 23:39:45');
INSERT INTO `album_src` VALUES (17, 5, '391664393', '8e215620bc9f7e9f36b071ba52819bc6', 'e4353b43593e478eba65b54afc51409f', '/各部门全共享资料/财务部/仅一LOGO/3.JOYEA仅一1.gif', '', '12.3 KB', '', '2019-05-05 23:39:45');
INSERT INTO `album_src` VALUES (18, 5, '391664377', 'b68f8b0712758739738148076475365e', '7c30d5a58d5c466e987492d2352ec8ce', '/各部门全共享资料/财务部/仅一LOGO/1.JOYEA仅一2.gif', '', '36.4 KB', '', '2019-05-05 23:39:45');
COMMIT;

-- ----------------------------
-- Table structure for src_collect
-- ----------------------------
DROP TABLE IF EXISTS `src_collect`;
CREATE TABLE `src_collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `src_neid` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `src_path` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_type` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_rev` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_size` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of src_collect
-- ----------------------------
BEGIN;
INSERT INTO `src_collect` VALUES (11, 391664415, 696047, '/各部门全共享资料/财务部/仅一LOGO/9.JOYEA仅一1-反黑.gif', 'image/.gif', '5c3565a58f525f408b6a164cfd317a1a', '47cce0eb3c9c4a64ab22902789a95602', '11.5 KB', '2019-05-04 21:02:19');
INSERT INTO `src_collect` VALUES (12, 391664393, 696047, '/各部门全共享资料/财务部/仅一LOGO/3.JOYEA仅一1.gif', 'image/.gif', '8e215620bc9f7e9f36b071ba52819bc6', 'e4353b43593e478eba65b54afc51409f', '12.3 KB', '2019-05-04 21:02:31');
INSERT INTO `src_collect` VALUES (14, 631129947, 696047, '/IT运维/点检图片/05.28/05.28 江苏电信对仅一防火墙进行了弱口令测试.png', 'image/.png', '490e74c4d488658b76d8f56c08c50720', 'f2537d00ad4d408c8e5c2746e69a19f0', '30.6 KB', '2019-05-04 21:02:32');
INSERT INTO `src_collect` VALUES (15, 621979327, 696047, '/IT运维/点检图片/05.14/05.14 晚上数据有有一条SQL阻塞2.png', 'image/.png', '388561b1a427914516fdd18f95630a03', 'f8eebe54643c406b841a947e5b8b5f5a', '47.5 KB', '2019-05-04 21:04:33');
INSERT INTO `src_collect` VALUES (16, 621979341, 696047, '/IT运维/点检图片/05.14/05.14 晚上数据有有一条SQL阻塞3.png', 'image/.png', '0e4f8f0d7f4a26bbdda95c3a64b85256', '890b8ebda5984b7abb8889a61e6f95a8', '32.4 KB', '2019-05-04 21:04:34');
INSERT INTO `src_collect` VALUES (17, 621979273, 696047, '/IT运维/点检图片/05.14/05.14 晚上数据有有一条SQL阻塞.png', 'image/.png', '69014a58dedafade8ca40cbee0b30f2c', '3cc37e52e1a542e4a38d61902195bbe4', '30.4 KB', '2019-05-04 21:04:34');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
