/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : joyea_share

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 05/10/2020 23:21:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
  `album_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `album_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `album_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `shared` tinyint(1) NOT NULL DEFAULT '0',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refer_num` int DEFAULT '0',
  `share_cover_neid` int DEFAULT NULL,
  `share_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `download_num` int DEFAULT '0',
  `like_num` int DEFAULT '0' COMMENT '收藏数',
  `hangye_tag_id` int DEFAULT '-1',
  `xianbie_tag_id` int DEFAULT '-1',
  `jixing_tag_id` int DEFAULT '-1',
  `jieduan_tag_id` int DEFAULT '-1',
  `shichang_tag_id` int DEFAULT '-1',
  `share_local_cover_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `copy_from` int DEFAULT NULL,
  PRIMARY KEY (`album_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of album
-- ----------------------------
BEGIN;
INSERT INTO `album` VALUES (33, 696047, '仅一', '', '2019-07-16 22:01:05', '2019-07-16 22:01:09', 1, 'joyea', 4, NULL, 'helloksjafnksadlkvjlk;sadjfk;sajfksajdflk;sajdfkl;jsad;fkljsal;kfj;lsakjf;lasjdf;sld', 0, 15, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (34, 1766247, '测试清单1_南大门', '', '2019-07-17 14:51:45', '2019-07-17 15:13:26', 1, '测试', 6, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (38, 799277, '0910石药接待', '', '2019-07-25 11:04:24', '2019-07-25 11:07:48', 1, '窦阳阳', 1, NULL, NULL, 0, 2, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (39, 799277, '0220扬子江出差', '', '2019-07-25 11:09:25', '2019-07-25 11:54:37', 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (41, 696821, '南大门2.0', '', '2019-07-25 11:51:20', '2019-07-26 19:01:57', 1, '张秀', 0, NULL, NULL, 0, 1, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (42, 1766247, '一号园测试0815', '', '2019-08-15 17:42:24', '2019-08-15 17:43:11', 1, '测试', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (43, 1766247, 'stick0815', '', '2019-08-15 17:48:02', '2019-08-15 17:48:15', 1, '测试', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (45, 799277, '来访', '', '2020-03-12 08:47:46', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (46, 799277, '0312扬子江来访', '', '2020-03-14 16:30:17', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (47, 696821, '3b8f884d-e9bb-4ccc-a4cd-e12b30613aaf', '', '2020-04-23 02:29:13', NULL, 0, '张秀', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (48, 696821, '59d4ffb3-6fd4-4621-868f-09376ab1a735', '', '2020-04-24 14:14:03', NULL, 0, '张秀', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (49, 799277, 'f924446c-a933-4f11-a3b7-65652ce959a2', '', '2020-04-24 14:34:08', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (50, 696047, '123321', '', '2020-05-06 02:04:22', NULL, 0, 'joyea', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (51, 696047, 'tedst安徽可以', '', '2020-05-06 02:05:41', NULL, 0, 'joyea', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (52, 696047, '视频', '', '2020-05-06 02:07:21', '2020-05-10 23:37:50', 0, 'joyea', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (53, 799277, '0502振东出差', '', '2020-05-06 09:40:06', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (54, 799277, '测试22', '', '2020-05-09 14:37:52', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (55, 696047, '测试0511', '', '2020-05-11 00:41:43', NULL, 0, 'joyea', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (56, 799277, '0803出差扬子江', '', '2020-05-11 11:41:10', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (57, 696047, '益生菌', '', '2020-05-14 23:33:02', NULL, 0, 'joyea', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (58, 717, '0616', '', '2020-06-17 11:33:16', NULL, 1, '窦阳阳', 0, NULL, '1231231', 0, 0, 0, 1, 1, 2, 2, '2', NULL);
INSERT INTO `album` VALUES (59, 213, 'jdkd', '', '2020-06-19 16:29:39', NULL, 0, '刘剑云', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (60, 20, '我的清单', '', '2020-06-22 22:00:19', NULL, 1, '司马小兴', 0, NULL, '我是简介', 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (61, 20, '123', '', '2020-06-23 23:54:52', NULL, 1, '司马小兴', 0, NULL, '', 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (63, 20, 'efefe', '', '2020-06-27 21:23:19', '2020-07-06 00:38:38', 1, '司马小兴', 1, NULL, 'helloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlkhelloksjafnksadlkvjlk', 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (64, 20, 'fdfdfd', '', '2020-06-27 21:24:12', '2020-07-07 23:34:37', 0, '司马小兴', 0, NULL, NULL, 1, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (65, 213, 'test', '', '2020-07-08 23:45:04', '2020-07-13 23:51:59', 0, '刘剑云', 1, NULL, NULL, 1, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (66, 213, 'video', '', '2020-07-09 00:00:10', NULL, 0, '刘剑云', 0, NULL, NULL, 7, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (72, 717, 'qweq', '', '2020-07-30 23:20:13', NULL, 1, '窦阳阳', 0, NULL, '1', 0, 0, 1, 2, 2, 1, 1, '5', NULL);
INSERT INTO `album` VALUES (73, 717, '123', '', '2020-08-02 16:52:59', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
INSERT INTO `album` VALUES (76, 717, '南大门2.0', '', '2020-08-03 22:54:44', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, 41);
INSERT INTO `album` VALUES (81, 717, '0910石药接待', '', '2020-08-03 23:04:42', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, 38);
INSERT INTO `album` VALUES (83, 717, '仅一', '', '2020-08-03 23:04:45', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, 33);
INSERT INTO `album` VALUES (92, 717, '123123123123123sfsasadsadasda', '', '2020-08-04 00:07:45', NULL, 0, '窦阳阳', 0, NULL, NULL, 0, 0, -1, -1, -1, -1, -1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for album_collect
-- ----------------------------
DROP TABLE IF EXISTS `album_collect`;
CREATE TABLE `album_collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `album_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for album_src
-- ----------------------------
DROP TABLE IF EXISTS `album_src`;
CREATE TABLE `album_src` (
  `id` int NOT NULL AUTO_INCREMENT,
  `album_id` int NOT NULL,
  `src_neid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_rev` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `src_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `src_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `src_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `src_bytes` int DEFAULT NULL,
  `src_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=396 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of album_src
-- ----------------------------
BEGIN;
INSERT INTO `album_src` VALUES (146, 33, '1092814890', 'ba3ad14ff85a03c2194c8782d1242c59', '805b870e8c6e41e8a7a189f433f6a3ba', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (1).JPG', 'image/.jpg', '13.1 MB', '', '2019-07-16 22:01:05', 13759186, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (1).JPG');
INSERT INTO `album_src` VALUES (147, 33, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2019-07-16 22:01:05', 13393468, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (2).JPG');
INSERT INTO `album_src` VALUES (148, 34, '1084277118', '336e8fc142b0d65832a552a358a103ea', '6da581e680c14bf3b599f33ffdea7578', '/营销素材展示/0、一号园照片/01、南门房/火车头.jpg', 'image/.jpg', '7.1 MB', '', '2019-07-17 14:51:45', 7495103, '');
INSERT INTO `album_src` VALUES (149, 34, '1084277109', '453ff946a38821ee945f9f51b9bf36cb', 'ab1fb5efe32448bcbcf9f7f25c49d164', '/营销素材展示/0、一号园照片/01、南门房/南大门.jpg', 'image/.jpg', '6.5 MB', '', '2019-07-17 14:51:45', 6787527, '');
INSERT INTO `album_src` VALUES (150, 34, '1084277652', 'decad0c3da51a70c26c225bc2da0bcb3', '9b87bf7615314c8dad362b901346a474', '/营销素材展示/0、一号园照片/01、南门房/形象墙.jpg', 'image/.jpg', '18.6 MB', '', '2019-07-17 14:51:45', 19467083, '');
INSERT INTO `album_src` VALUES (162, 38, '1092814506', '0e9d2a2ec60b54c77c8661d7e4bc29b3', '571801605c88487d804401115ec3a8bc', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/014、金诃藏药（安儿宁颗粒）/1、金诃藏药-安儿宁颗粒/金诃藏药-安儿宁颗粒20170330 (1).JPG', 'image/.jpg', '1.6 MB', 'helloworld23232', '2019-07-25 11:05:33', 1637498, '');
INSERT INTO `album_src` VALUES (163, 38, '1202889657', '33213bd44fd74b541e97b0411956b70c', 'ecaf3c5a59974cd780c3d2278d482637', '/营销素材展示/004、公司介绍资料/1、公司介绍资料/1、仅一简介（含代工）20180417.pptx', 'doc/.pptx', '43.4 MB', 'helloworld', '2019-07-25 11:05:33', 45557587, '');
INSERT INTO `album_src` VALUES (164, 38, '1092814698', '3d8595b0b5ae297ec2ccc4c967a60d20', 'fed11d0b32da4985827b607c7929b175', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/014、金诃藏药（安儿宁颗粒）/1、金诃藏药-安儿宁颗粒/金诃藏药-安儿宁颗粒20170330 (2).jpg', 'image/.jpg', '7.5 MB', 'helloworld12', '2019-07-25 11:05:33', 7893494, '');
INSERT INTO `album_src` VALUES (165, 38, '1257850917', '9b358c883546d5a5ef04ec49cafda6db', 'a83241d749ca41b68fbe1c99a39fbabd', '/营销素材展示/001、药品行业stick生产线/003、设备视频/1、整线视频/STICK整线合集（压缩--手机播放用）.mp4', 'video/.mp4', '627.6 MB', '', '2019-07-25 11:05:33', 658100307, '');
INSERT INTO `album_src` VALUES (170, 41, '1084277118', '336e8fc142b0d65832a552a358a103ea', '6da581e680c14bf3b599f33ffdea7578', '/营销素材展示/000、一号园照片/01、南门房/火车头.jpg', 'image/.jpg', '', '', '2019-07-25 11:51:20', 7495103, '火车头.jpg');
INSERT INTO `album_src` VALUES (171, 41, '1084277652', 'decad0c3da51a70c26c225bc2da0bcb3', '9b87bf7615314c8dad362b901346a474', '/营销素材展示/000、一号园照片/01、南门房/形象墙.jpg', 'image/.jpg', '18.6 MB', '', '2019-07-25 11:51:20', 19467083, '');
INSERT INTO `album_src` VALUES (172, 41, '1084277109', '453ff946a38821ee945f9f51b9bf36cb', 'ab1fb5efe32448bcbcf9f7f25c49d164', '/营销素材展示/000、一号园照片/01、南门房/南大门.jpg', 'image/.jpg', '6.5 MB', '', '2019-07-25 11:51:20', 6787527, '');
INSERT INTO `album_src` VALUES (173, 39, '1092814506', '0e9d2a2ec60b54c77c8661d7e4bc29b3', '571801605c88487d804401115ec3a8bc', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/014、金诃藏药（安儿宁颗粒）/1、金诃藏药-安儿宁颗粒/金诃藏药-安儿宁颗粒20170330 (1).JPG', 'image/.jpg', '', '1234\n', '2019-07-25 11:54:37', 1637498, '金诃藏药-安儿宁颗粒20170330 (1).JPG');
INSERT INTO `album_src` VALUES (174, 39, '1202889657', '33213bd44fd74b541e97b0411956b70c', 'ecaf3c5a59974cd780c3d2278d482637', '/营销素材展示/004、公司介绍资料/1、公司介绍资料/1、仅一简介（含代工）20180417.pptx', 'doc/.pptx', '', '3456', '2019-07-25 11:54:37', 45557587, '1、仅一简介（含代工）20180417.pptx');
INSERT INTO `album_src` VALUES (175, 39, '1092814698', '3d8595b0b5ae297ec2ccc4c967a60d20', 'fed11d0b32da4985827b607c7929b175', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/014、金诃藏药（安儿宁颗粒）/1、金诃藏药-安儿宁颗粒/金诃藏药-安儿宁颗粒20170330 (2).jpg', 'image/.jpg', '', '3456', '2019-07-25 11:54:37', 7893494, '金诃藏药-安儿宁颗粒20170330 (2).jpg');
INSERT INTO `album_src` VALUES (176, 42, '1084277118', '336e8fc142b0d65832a552a358a103ea', '6da581e680c14bf3b599f33ffdea7578', '/营销素材展示/000、一号园照片/01、南门房/火车头.jpg', 'image/.jpg', '7.1 MB', '', '2019-08-15 17:42:24', 7495103, '');
INSERT INTO `album_src` VALUES (177, 42, '1084277652', 'decad0c3da51a70c26c225bc2da0bcb3', '9b87bf7615314c8dad362b901346a474', '/营销素材展示/000、一号园照片/01、南门房/形象墙.jpg', 'image/.jpg', '18.6 MB', '', '2019-08-15 17:42:24', 19467083, '');
INSERT INTO `album_src` VALUES (178, 42, '1084277109', '453ff946a38821ee945f9f51b9bf36cb', 'ab1fb5efe32448bcbcf9f7f25c49d164', '/营销素材展示/000、一号园照片/01、南门房/南大门.jpg', 'image/.jpg', '6.5 MB', '', '2019-08-15 17:42:24', 6787527, '');
INSERT INTO `album_src` VALUES (179, 42, '1084280865', '9e2bd4c7d7313e639f221723657e5529', '308461b46a1f4c5781d441c92fcd4296', '/营销素材展示/000、一号园照片/04、大厅/1.南大厅.jpg', 'image/.jpg', '15.0 MB', '', '2019-08-15 17:42:24', 15763490, '');
INSERT INTO `album_src` VALUES (180, 42, '1084280778', 'e93bff6dafb7398c57bf9d5664a93ea4', '9371f2d844884f5d931c84966e4364a1', '/营销素材展示/000、一号园照片/04、大厅/3.南大厅.jpg', 'image/.jpg', '18.5 MB', '', '2019-08-15 17:42:24', 19349972, '');
INSERT INTO `album_src` VALUES (181, 42, '1084280475', '8c4c144eb5c162adc269054960deaa84', '30b864c137614c0babd9e82d013f2416', '/营销素材展示/000、一号园照片/04、大厅/2.南大厅.jpg', 'image/.jpg', '12.0 MB', '', '2019-08-15 17:42:24', 12594403, '');
INSERT INTO `album_src` VALUES (182, 43, '1129077471', '596e6ce6aa33fd229edb324baeb374c6', '234214aee6214929b2b81af12bcd5090', '/营销素材展示/001、药品行业stick生产线/003、设备视频/1、整线视频/1、颗粒剂设备视频/001、康芝-12列计量盒式stick整线.mpg', 'video/.mpg', '289.5 MB', '', '2019-08-15 17:48:02', 303550464, '');
INSERT INTO `album_src` VALUES (183, 43, '1129092852', 'd13dfb505120e47e4fda5b2940f65f24', 'a8776bfd00714b509432070ecb3d7e45', '/营销素材展示/001、药品行业stick生产线/003、设备视频/1、整线视频/1、颗粒剂设备视频/002、北京韩美-12列计量盒整线.mpg', 'video/.mpg', '266.7 MB', '', '2019-08-15 17:48:02', 279627776, '');
INSERT INTO `album_src` VALUES (184, 43, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2019-08-15 17:48:02', 12311133, '');
INSERT INTO `album_src` VALUES (185, 43, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2019-08-15 17:48:02', 12723389, '');
INSERT INTO `album_src` VALUES (186, 43, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2019-08-15 17:48:02', 12691326, '');
INSERT INTO `album_src` VALUES (187, 44, '1203606141', '11594f8d6bc0c8a3bca082b4a3591314', 'c4174f37e14d413c8197fca09705fbc6', '/营销素材展示/005、同行信息/1、国际同行/Mespack/2、雀巢/MESpack雀巢STICK录像/条状小袋装中袋照片录像/滑道截止门动作录像.MOV', 'video/.mov', '11.8 MB', '', '2020-01-16 22:34:54', 12408354, '滑道截止<mark>门</mark>动作录像.MOV');
INSERT INTO `album_src` VALUES (188, 44, '1096938309', '22e791acb9243f8250bc0a45dfbdc26c', '9ad6e91b81bf4455a71992bca47be152', '/营销素材展示/001、药品行业stick生产线/002、设备照片/1、整线照片/1、颗粒剂设备照片/3、华北制药颗粒剂线照片/华北制药-stick线-12列stick整线-车间门.JPG', 'image/.jpg', '1.4 MB', '', '2020-01-16 22:34:54', 1502353, '华北制药-stick线-12列stick整线-车间<mark>门</mark>.JPG');
INSERT INTO `album_src` VALUES (189, 44, '1092818700', '0a308f38400f75d300dfee0fa1d1760b', 'b9b6d9569cb5439b97700b79c20f05b0', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/4、化药（成人药）/016、启瑞（门冬氨酸鸟氨酸颗粒）-同类感/启瑞-门冬氨酸鸟氨酸颗粒-同类感20161125.JPG', 'image/.jpg', '11.1 MB', '', '2020-01-16 22:34:54', 11664561, '');
INSERT INTO `album_src` VALUES (190, 45, '1092817848', 'ee226402e8857fb2b97c1394bbc9b304', '332cb8fc7e1a4382847867eb21da9b2f', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/3、化药（儿童药）/010、修正（小儿氨酚黄那敏颗粒）/1、修正（小儿氨酚黄那敏颗粒）-新包装/修正-小儿氨酚黄那敏颗粒20160425 (1).JPG', 'image/.jpg', '3.5 MB', '', '2020-03-12 08:47:47', 3693839, '');
INSERT INTO `album_src` VALUES (191, 45, '1092817908', 'b8fa2565faf90b10c19ef2608460e453', '7893bd28151e4d06a24601f9c90c46ab', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/3、化药（儿童药）/010、修正（小儿氨酚黄那敏颗粒）/1、修正（小儿氨酚黄那敏颗粒）-新包装/修正-小儿氨酚黄那敏颗粒20160425 (2).JPG', 'image/.jpg', '2.8 MB', '', '2020-03-12 08:47:47', 2962081, '');
INSERT INTO `album_src` VALUES (192, 45, '1092817917', '1f14d0fb8e6d3e237ae283f3a8c22008', 'f793096b85fc410387b9b1b071196942', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/3、化药（儿童药）/010、修正（小儿氨酚黄那敏颗粒）/1、修正（小儿氨酚黄那敏颗粒）-新包装/修正-小儿氨酚黄那敏颗粒20160425 (3).JPG', 'image/.jpg', '3.7 MB', '', '2020-03-12 08:47:47', 3848273, '');
INSERT INTO `album_src` VALUES (193, 46, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-03-14 16:30:17', 12311133, '');
INSERT INTO `album_src` VALUES (194, 46, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-03-14 16:30:17', 12691326, '');
INSERT INTO `album_src` VALUES (195, 46, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-03-14 16:30:17', 12723389, '');
INSERT INTO `album_src` VALUES (196, 47, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-04-23 02:29:13', 12311133, '');
INSERT INTO `album_src` VALUES (197, 47, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-04-23 02:29:13', 12691326, '');
INSERT INTO `album_src` VALUES (198, 47, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-04-23 02:29:13', 12723389, '');
INSERT INTO `album_src` VALUES (199, 47, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-04-23 02:29:13', 12311133, '');
INSERT INTO `album_src` VALUES (200, 47, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-04-23 02:29:13', 12691326, '');
INSERT INTO `album_src` VALUES (201, 47, '703070695', '78547fbc74a806b06b1aeaf1f6704b7b', '6465b21acffb48beab12d5afbd791c4e', '/IT/信息安全/加密软件需求及问题处理.xlsx', 'doc/.xlsx', '10.2 KB', '', '2020-04-23 02:29:13', 10438, '');
INSERT INTO `album_src` VALUES (202, 47, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-04-23 02:29:13', 12723389, '');
INSERT INTO `album_src` VALUES (203, 48, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/004、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2020-04-24 14:14:03', 13393468, '');
INSERT INTO `album_src` VALUES (204, 48, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/004、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2020-04-24 14:14:03', 13393468, '');
INSERT INTO `album_src` VALUES (205, 48, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/004、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2020-04-24 14:14:03', 13393468, '');
INSERT INTO `album_src` VALUES (206, 49, '1092820065', '1f2583b7e16ca4967a622b70f3aa6c77', '2b8adb84e8ee4ba28b7d8e72826d43b7', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/003、药品-干混悬剂/001、普利（地氯雷他定干混悬剂、阿奇霉素干混悬剂）/2、普利（阿奇霉素干混悬剂）/普利-阿奇霉素干混悬剂20170330 (1).JPG', 'image/.jpg', '1.5 MB', '', '2020-04-24 14:34:08', 1541777, '');
INSERT INTO `album_src` VALUES (207, 49, '1092819933', '1fb2a2419572137813b12c28e7a5a499', 'f07d8d57b77143179a2665009840a3ef', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/003、药品-干混悬剂/001、普利（地氯雷他定干混悬剂、阿奇霉素干混悬剂）/2、普利（阿奇霉素干混悬剂）/普利-阿奇霉素干混悬剂20170330 (2).JPG', 'image/.jpg', '1.3 MB', '', '2020-04-24 14:34:08', 1401042, '');
INSERT INTO `album_src` VALUES (208, 49, '1092819933', '1fb2a2419572137813b12c28e7a5a499', 'f07d8d57b77143179a2665009840a3ef', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/003、药品-干混悬剂/001、普利（地氯雷他定干混悬剂、阿奇霉素干混悬剂）/2、普利（阿奇霉素干混悬剂）/普利-阿奇霉素干混悬剂20170330 (2).JPG', 'image/.jpg', '1.3 MB', '', '2020-04-24 14:34:08', 1401042, '');
INSERT INTO `album_src` VALUES (209, 49, '1092820065', '1f2583b7e16ca4967a622b70f3aa6c77', '2b8adb84e8ee4ba28b7d8e72826d43b7', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/003、药品-干混悬剂/001、普利（地氯雷他定干混悬剂、阿奇霉素干混悬剂）/2、普利（阿奇霉素干混悬剂）/普利-阿奇霉素干混悬剂20170330 (1).JPG', 'image/.jpg', '1.5 MB', '', '2020-04-24 14:34:08', 1541777, '');
INSERT INTO `album_src` VALUES (210, 50, '413204721', 'e0a8fd8efd5448020b43361828227b94', '4db393c2faba4045b909c46534d65030', '/一号园IT项目组/项目管理文档/一号园访客来访流程及智能化原则_20170918.docx', 'doc/.docx', '17.8 KB', '', '2020-05-06 02:04:22', 18188, '一号园访客来访流程及智能化原则_20170918.docx');
INSERT INTO `album_src` VALUES (211, 50, '1084277118', '336e8fc142b0d65832a552a358a103ea', '6da581e680c14bf3b599f33ffdea7578', '/营销素材展示/004、公司介绍资料/5、一号园资料/0、一号园照片/01、南门房/火车头.jpg', 'image/.jpg', '7.1 MB', '', '2020-05-06 02:04:22', 7495103, '火车头.jpg');
INSERT INTO `album_src` VALUES (212, 50, '413272885', '7436a575fd320fadb4b5047256f985fd', '7bbe17c7f4ea44eda7c0625b54231861', '/一号园IT项目组/2017资料/电信/丹阳仅一包装智能化方案（R3.3）.pdf', 'doc/.pdf', '2.8 MB', '', '2020-05-06 02:04:22', 2885186, '丹阳仅一包装智能化方案（R3.3）.pdf');
INSERT INTO `album_src` VALUES (213, 50, '391429811', '445759d3280a27acad2527b30af49c15', '26c18c41b34f413fadb0a0ff04fd623d', '/一号园IT项目组/2017资料/电信/ITC-广播系统方案说明书.pdf', 'doc/.pdf', '1.0 MB', '', '2020-05-06 02:04:22', 1099781, 'ITC-广播系统方案说明书.pdf');
INSERT INTO `album_src` VALUES (214, 50, '383216527', '6418c0e05ca96af52753ae531f7519cf', 'c14917a4ac81418ea370cb04aec30aa4', '/一号园IT项目组/003访客安防门禁/门禁白名单功能设计需求20170904.docx', 'doc/.docx', '32.1 KB', '', '2020-05-06 02:04:22', 32921, '门禁白名单功能设计需求20170904.docx');
INSERT INTO `album_src` VALUES (215, 50, '501870759', '0168c50847fab2157f67d0181ab7a6c2', '498c1ef3d3a348f9b625d6e480fd59a4', '/一号园IT项目组/002基础网络/一号园有线无线建设项目招标文件/一号园网络工程建设项目招标文件v20171121.doc', 'doc/.doc', '941.5 KB', '', '2020-05-06 02:04:22', 964096, '一号园网络工程建设项目招标文件v20171121.doc');
INSERT INTO `album_src` VALUES (216, 50, '380964289', 'b8ebfc971a3fb7177f6c887c1fa81521', 'c256aa64d9b04d258e31d4d469f2a7cf', '/一号园IT项目组/002基础网络/一号园有线网络系统建设需求.docx', 'doc/.docx', '18.3 KB', '', '2020-05-06 02:04:22', 18763, '一号园有线网络系统建设需求.docx');
INSERT INTO `album_src` VALUES (217, 51, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-05-06 02:05:41', 12311133, '');
INSERT INTO `album_src` VALUES (218, 51, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-05-06 02:05:41', 12723389, '');
INSERT INTO `album_src` VALUES (219, 51, '1092814740', '5b1eb665c950e97ea0e9694ae064e50d', 'ed4dcdde437c4ecfb6cbb80f73ce8c1f', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/002、辅仁（小儿清热宁颗粒）-同类感/辅仁-小儿清热宁颗粒-同类感 20141224 (2).jpg', 'image/.jpg', '2.3 MB', '', '2020-05-06 02:05:41', 2457062, '');
INSERT INTO `album_src` VALUES (220, 51, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-05-06 02:05:41', 12691326, '');
INSERT INTO `album_src` VALUES (221, 51, '1096936305', '3960d2fda260e68cf742d51a27831560', '9fcac4cf281f4b2586c029206ad9761d', '/营销素材展示/001、药品行业stick生产线/001、样品照片/003、合集/1、药品照片合集/药品照片合集-儿童药合集20160425.JPG', 'image/.jpg', '3.7 MB', '', '2020-05-06 02:05:41', 3860011, '');
INSERT INTO `album_src` VALUES (222, 51, '1096936296', '4694771361fe488c4aaab05c21cd59ba', '11bb6b0b1b5a419cb052d7c252e9a3ec', '/营销素材展示/001、药品行业stick生产线/001、样品照片/003、合集/1、药品照片合集/药品照片合集-中药配方颗粒20091123.jpg', 'image/.jpg', '604.3 KB', '', '2020-05-06 02:05:41', 618760, '');
INSERT INTO `album_src` VALUES (225, 53, '1092818667', '82c1dd7b31f7738308653af51914a5d5', '704df6caf9704582894c131dbbc4ca0d', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/4、化药（成人药）/015、康远（碳酸钙D3颗粒）-同类感/康远-碳酸钙D3颗粒-同类感20151125.JPG', 'image/.jpg', '11.9 MB', '', '2020-05-06 09:40:06', 12466682, '康远-<mark>碳酸钙</mark><mark>D3</mark><mark>颗粒</mark>-同类感20151125.JPG');
INSERT INTO `album_src` VALUES (226, 53, '1835428038', '7841dfafa0230db8a4bdfab15bea1813', '934b04ebaae743c980882e8b5224875f', '/营销素材展示/001、药品行业stick生产线/005、介绍资料/004、补钙颗粒介绍资料/致振东碳酸钙D3颗粒stick包装方案建议20190829(1)(1).pptx', 'doc/.pptx', '29.6 MB', '', '2020-05-06 09:40:06', 31019312, '致振东<mark>碳酸钙</mark><mark>D3</mark><mark>颗粒</mark>stick包装方案建议20190829(1)(1).pptx');
INSERT INTO `album_src` VALUES (227, 53, '1203626742', '303bdbe0587ce5c7c4a2f61a81183e8f', '4d8603b4598c4e388c018c354f0aeee4', '/营销素材展示/005、同行信息/1、国际同行/Omag/3、济川(未签)/OMAG-济川价格/OMAG-济川投标价格 (3).png', 'image/.png', '1008.7 KB', '', '2020-05-06 09:40:06', 1032878, 'OMAG-<mark>济</mark><mark>川</mark>投标价格 (3).png');
INSERT INTO `album_src` VALUES (228, 53, '1096955739', '50c0f59676f4cdaa56e1b8e48ce14176', 'e05e52cd3a744a1fb4e21f4c14dc43c8', '/营销素材展示/001、药品行业stick生产线/006、客户logo及地图/logo/药品20170715/J济川logo.jpg', 'image/.jpg', '1.4 MB', '', '2020-05-06 09:40:06', 1518034, 'J<mark>济</mark><mark>川</mark>logo.jpg');
INSERT INTO `album_src` VALUES (229, 53, '1940033202', 'b2bd3dba8c58f783b30e32638b9c4c42', '5eaa8f01b65045e286663ff0dd81cb54', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/4、化药（成人药）/025、振东（碳酸钙D3颗粒）/振东-碳酸钙D3颗粒（3g）20200416.jpg', 'image/.jpg', '7.2 MB', '', '2020-05-06 09:40:06', 7571704, '振东-<mark>碳酸钙</mark><mark>D3</mark><mark>颗粒</mark>（3g）20200416.jpg');
INSERT INTO `album_src` VALUES (230, 53, '1940026743', '79ab0924aec5ef2635d99489cb71432c', '4293429230324fcaaf2d83bb65dff20c', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/3、化药（儿童药）/015、振东（碳酸钙D3颗粒）/振东-碳酸钙D3颗粒（1.5g）20200416.jpg', 'image/.jpg', '6.0 MB', '', '2020-05-06 09:40:06', 6309474, '振东-<mark>碳酸钙</mark><mark>D3</mark><mark>颗粒</mark>（1.5g）20200416.jpg');
INSERT INTO `album_src` VALUES (231, 53, '1204241955', '29a5d44cff5c1cc4967b1e69998d3fc3', '1c35b4926cee4fed87b42b0f59637f25', '/营销素材展示/005、同行信息/2、国内同行/F发泰/002、济川/1、12列计量盒整线/1、商务资料/济川项目发泰报价.xls', 'doc/.xls', '48.5 KB', '', '2020-05-06 09:40:06', 49664, '<mark>济</mark><mark>川</mark>项目发泰报价.xls');
INSERT INTO `album_src` VALUES (232, 54, '1092823953', 'ef8fd3ffc35c4a20a431778bc9fe1319', 'a1062616180a4ddd86ee935f8e37a610', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/013、维善益生菌/杜邦-维善益生菌20160425.JPG', 'image/.jpg', '4.0 MB', '', '2020-05-09 14:37:52', 4176313, '');
INSERT INTO `album_src` VALUES (233, 52, '1202860407', '055751f7284e6db33f66d17186204272', '6834f0a3531d48708642a18ed3ab7884', '/营销素材展示/003、软双铝生产线/2、设备视频/东阳软双铝MS BanidingLine_M2_x264.mp4', 'video/.mp4', '48.0 MB', '', '2020-05-10 23:37:50', 50343592, '');
INSERT INTO `album_src` VALUES (234, 55, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-05-11 00:41:43', 12311133, '');
INSERT INTO `album_src` VALUES (235, 55, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-05-11 00:41:43', 12691326, '');
INSERT INTO `album_src` VALUES (236, 55, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-05-11 00:41:43', 12723389, '');
INSERT INTO `album_src` VALUES (237, 55, '1092819444', '60982515883560a7d34db00a2ce7b81c', 'dc156f05577145689cd735941f7480bb', '/营销素材展示/001、药品行业stick生产线/001、样品照片/001、药品/002、药品-粉剂/1、中药/001、云南同根三七（文山新型饮片、三七粉）/云南好农夫-三七粉20171204 (1).JPG', 'image/.jpg', '12.8 MB', '', '2020-05-11 00:41:43', 13428000, '');
INSERT INTO `album_src` VALUES (238, 56, '1092823953', 'ef8fd3ffc35c4a20a431778bc9fe1319', 'a1062616180a4ddd86ee935f8e37a610', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/013、维善益生菌/杜邦-维善益生菌20160425.JPG', 'image/.jpg', '4.0 MB', '', '2020-05-11 11:41:10', 4176313, '杜邦-维善<mark>益生菌</mark>20160425.JPG');
INSERT INTO `album_src` VALUES (239, 56, '1940118405', '2459eec3104d9ab222c995299435427f', 'a5818a1f65a647e395d792d2596f1881', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/017、YPEPRO益生菌固体饮料/YPEPRO益生菌固体饮料20200416 (3).jpg', 'image/.jpg', '5.3 MB', '', '2020-05-11 11:41:10', 5510684, 'YPEPRO<mark>益生菌</mark>固体饮料20200416 (3).jpg');
INSERT INTO `album_src` VALUES (240, 56, '1092826554', '563ccad07a09a18350b9cb5fcbaace8b', '0e1f99722570443f991a7f99d389fb36', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/053、香港科达（益生菌）/香港科达-益生菌20161207 (2).jpg', 'image/.jpg', '5.2 MB', '', '2020-05-11 11:41:10', 5455321, '香港科达-<mark>益生菌</mark>20161207 (2).jpg');
INSERT INTO `album_src` VALUES (241, 56, '1203736218', '8a8280c4f53eb7dde34bd416d1dfdc92', '2277597b02324ad6bab909e6020a5834', '/营销素材展示/005、同行信息/1、国际同行/Solpac/1、公司资料/2、样袋照片/solpac-BYO益生菌样品20160907 (2).JPG', 'image/.jpg', '1.8 MB', '', '2020-05-11 11:41:10', 1849946, 'solpac-BYO<mark>益生菌</mark>样品20160907 (2).JPG');
INSERT INTO `album_src` VALUES (242, 56, '1940118420', 'c726ba796d41af050c69073b89f36764', '82997179e569493486debe15eb5a1a99', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/017、YPEPRO益生菌固体饮料/YPEPRO益生菌固体饮料20200416 (1).jpg', 'image/.jpg', '5.7 MB', '', '2020-05-11 11:41:10', 5974923, 'YPEPRO<mark>益生菌</mark>固体饮料20200416 (1).jpg');
INSERT INTO `album_src` VALUES (243, 57, '1940148081', 'b586cf8b098dadf1e997ed6693d68168', '9ce32bd6fcb44d8ab2f62f5b33ad21ce', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/028、森澜/004、B365益生菌/B365-益生菌20200416 (2).jpg', 'image/.jpg', '5.6 MB', '', '2020-05-14 23:33:02', 5850189, 'B365-<mark>益生菌</mark>20200416 (2).jpg');
INSERT INTO `album_src` VALUES (244, 57, '1092826524', '771e9dc3d948c6098024d441bf7328d8', 'f339c4ec52624bb4ae60889ff2c6725a', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/053、香港科达（益生菌）/香港科达-益生菌20161207 (1).jpg', 'image/.jpg', '8.7 MB', '', '2020-05-14 23:33:02', 9147318, '香港科达-<mark>益生菌</mark>20161207 (1).jpg');
INSERT INTO `album_src` VALUES (245, 57, '1940147961', 'f6f9ec6df0d263abf59f363a8bfde5b0', '9ac3b69375454a1eaf08b1946eb0fbb5', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/028、森澜/004、B365益生菌/B365-益生菌20200416 (1).jpg', 'image/.jpg', '8.7 MB', '', '2020-05-14 23:33:02', 9081542, 'B365-<mark>益生菌</mark>20200416 (1).jpg');
INSERT INTO `album_src` VALUES (246, 57, '1092823992', '0354ae00ffba52193386ac3330da93d4', '4d49e94dd3ee4d5985252da54381f74e', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/016、无限极益生菌/杜邦-无限极益生菌20180604 (3).JPG', 'image/.jpg', '12.5 MB', '', '2020-05-14 23:33:02', 13104647, '杜邦-无限极<mark>益生菌</mark>20180604 (3).JPG');
INSERT INTO `album_src` VALUES (247, 57, '1203736218', '8a8280c4f53eb7dde34bd416d1dfdc92', '2277597b02324ad6bab909e6020a5834', '/营销素材展示/005、同行信息/1、国际同行/Solpac/1、公司资料/2、样袋照片/solpac-BYO益生菌样品20160907 (2).JPG', 'image/.jpg', '1.8 MB', '', '2020-05-14 23:33:02', 1849946, 'solpac-BYO<mark>益生菌</mark>样品20160907 (2).JPG');
INSERT INTO `album_src` VALUES (248, 57, '1092823959', 'fd5b9623851ffb21f057ccfe2b2f2c61', '67f94ee3a2754f6a9d89c26a6c5cffc7', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/016、无限极益生菌/杜邦-无限极益生菌20180604 (2).JPG', 'image/.jpg', '12.2 MB', '', '2020-05-14 23:33:02', 12805111, '杜邦-无限极<mark>益生菌</mark>20180604 (2).JPG');
INSERT INTO `album_src` VALUES (249, 58, '1092826968', '917eb178dc9fe764479b522900691ced', '5d75a97e757a4a3f89f878da62f55faf', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品样品照片/2、保健品-粉剂/006、海健堂（活性肽胶原蛋白、蓝海美资胶原蛋白、胶原蛋白）/3、海健堂（蓝海美资胶原蛋白粉）/海健堂-蓝海美资胶原蛋白粉20150415.JPG', 'image/.jpg', '1.9 MB', '', '2020-06-17 11:33:16', 1942987, '');
INSERT INTO `album_src` VALUES (250, 58, '1092826929', '5ee5d0195670a4631f329d2e9b4a08e3', '4e8436b3e61d41bc9aed770571374ece', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品样品照片/2、保健品-粉剂/006、海健堂（活性肽胶原蛋白、蓝海美资胶原蛋白、胶原蛋白）/2、海健堂（活性肽胶原蛋白粉）/海健堂-活性肽胶原蛋白粉20150415.JPG', 'image/.jpg', '2.1 MB', '', '2020-06-17 11:33:16', 2209029, '');
INSERT INTO `album_src` VALUES (251, 58, '1092828108', '8efe2ee452b59656be9d8fefd9931dd6', 'cad6d8c6695d40c398a8112080558d29', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品样品照片/4、保健品-液体/025、香港科达（益畅菌）/香港科达-益畅菌20160520.JPG', 'image/.jpg', '3.8 MB', '', '2020-06-17 11:33:16', 4008788, '');
INSERT INTO `album_src` VALUES (252, 58, '1092828249', 'e127c7469ed3eecd02ef59b0c7917d91', '2bc2e28caffd4c028e11ccec97ff27c1', '/营销素材展示/001、药品行业stick生产线/001、样品照片/002、保健品样品照片/4、保健品-液体/025、香港科达（益畅菌）/香港科达-益畅菌20160425.JPG', 'image/.jpg', '2.8 MB', '', '2020-06-17 11:33:16', 2929954, '');
INSERT INTO `album_src` VALUES (253, 59, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/001、stick线/001、样品照片/001、药品样品照片/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-06-19 16:29:39', 12723389, '');
INSERT INTO `album_src` VALUES (254, 59, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/001、stick线/001、样品照片/001、药品样品照片/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-06-19 16:29:39', 12691326, '');
INSERT INTO `album_src` VALUES (255, 59, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/001、stick线/001、样品照片/001、药品样品照片/001、药品-颗粒剂/1、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-06-19 16:29:39', 12311133, '');
INSERT INTO `album_src` VALUES (256, 60, '1092814737', 'f75be3de98deee6682c8fcb135bc9745', '5d5615e1617e4ccdaaae17ff79779964', '/营销素材展示/STICK线/001、样品照片/001、药品样品照片/001、药品-颗粒剂/001、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (1).JPG', 'image/.jpg', '11.7 MB', '', '2020-06-22 22:00:19', 12311133, '');
INSERT INTO `album_src` VALUES (257, 60, '1092814776', '0b7202bc98608243882e699b69734b6b', '135d97ddd27e4deebbf2f3f5893a0d50', '/营销素材展示/STICK线/001、样品照片/001、药品样品照片/001、药品-颗粒剂/001、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (2).JPG', 'image/.jpg', '12.1 MB', '', '2020-06-22 22:00:19', 12691326, '');
INSERT INTO `album_src` VALUES (258, 60, '1092814824', '4872cd89f2fa69bf9ef11429c74b7eee', 'dc99d31a25e241f88b84fb92d418b7e2', '/营销素材展示/STICK线/001、样品照片/001、药品样品照片/001、药品-颗粒剂/001、中药（儿童药）/001、王老吉（小儿七星茶颗粒）-同类感/王老吉-小儿七星茶颗粒-同类感 20161125 (3).JPG', 'image/.jpg', '12.1 MB', '', '2020-06-22 22:00:19', 12723389, '');
INSERT INTO `album_src` VALUES (259, 61, '1203702210', '601cc1d36fa8d6486499e0c5e80f5efa', '4c910e91a80848c6bdc9698d840ec618', '/营销素材展示/同行信息/001、国际同行/Schmucker/1、公司资料/Schmucker多机图纸资料/M09056_TR_00_GB说明书/Microsoft Word - M09056_20_00_GB.FP.pdf', 'doc/.pdf', '11.1 KB', '', '2020-06-23 23:54:52', 11397, 'Microsoft Word - M09056_<mark>20</mark>_00_GB.FP.pdf');
INSERT INTO `album_src` VALUES (260, 61, '1207130379', '0d1635277ba71289464753fd0ec422a8', 'b410c1c2d7044cdaa74172426532dd57', '/营销素材展示/STICK线/005、细分市场引导素材/006、奶粉stick引导素材/002、婴幼儿米粉介绍（飞鹤米粉项目）20180104.pptx', 'doc/.pptx', '43.0 MB', '', '2020-06-23 23:54:52', 45079016, '002、婴幼儿米粉介绍（飞鹤米粉项目）20180104.pptx');
INSERT INTO `album_src` VALUES (272, 64, '1084281549', 'd33612105ca70b94640f368412798acd', '030f04273f0b4b349b537354cab0c2f0', '/营销素材展示/公司介绍/005、一号园资料/001、一号园照片/005、办公环境/1、办公家具/办公家具 (20).JPG', 'image/.jpg', '29.5 MB', '', '2020-06-27 21:24:12', 30978469, '办公家具 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (273, 64, '1204027317', 'c6d6b0bf15c248165a60452def2d798c', 'e027389713d54c3b84f95797526674e4', '/营销素材展示/同行信息/001、国际同行/Topack/2、扬子江（Topack）/1、TOPACK OM stick流水线/TOPACK OM stick流水线(扬子江现场)20170726/装盒机(扬子江现场)20170726/20170726装盒机 (20).JPG', 'image/.jpg', '2.0 MB', '', '2020-06-27 21:24:12', 2064452, '20170726装盒机 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (274, 64, '1204201407', '7f3eaedc57f85fd3017b23f873dc8258', '44349caf0b4242b6bf2e564bfb1773ab', '/营销素材展示/同行信息/001、国际同行/千世/2、展会/千世-中国（武汉）国际制药机械博览会20140425/千世机器静态 (20).JPG', 'image/.jpg', '3.9 MB', '', '2020-06-27 21:24:12', 4094976, '千世机器静态 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (275, 64, '1037798730', '52ad8720c097ce156b5b23a063b87715', 'bedcfc0bec6b48c5a6e7a6029c1bc115', '/信息化需求评级排序及执行反馈/1、素材库/外联开发素材清单/素材库0408/images/清单工作台/regen/u20.png', 'image/.png', '197.0 bytes', '', '2020-06-27 21:24:12', 197, 'u<mark>20</mark>.png');
INSERT INTO `album_src` VALUES (276, 64, '1204025946', '0db932ee5962d06fc63c39ceba5dc8a8', '5187a721ac5f483b91ada850cf19a783', '/营销素材展示/同行信息/001、国际同行/Topack/2、扬子江（Topack）/1、TOPACK OM stick流水线/TOPACK OM stick流水线(扬子江现场)20170726/整理机(扬子江现场)20170726/20170726整理机 (20).JPG', 'image/.jpg', '2.1 MB', '', '2020-06-27 21:24:12', 2219649, '20170726整理机 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (277, 64, '1170963111', '994c1d12804373adaf9e7f3eafafb9ee', '9670c13d71b6489a9bfbb6ac5f19444c', '/营销素材展示/STICK线/001、样品照片/003、乳品样品照片/025、日本辅食/日本辅食照片 (20).JPG', 'image/.jpg', '2.5 MB', '', '2020-06-27 21:24:12', 2640773, '日本辅食照片 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (278, 64, '1204024782', 'f2ab3e0ae728f2eef6f1995a47250e91', 'e766b6184df1442e95241872c1c2c5aa', '/营销素材展示/同行信息/001、国际同行/Topack/2、扬子江（Topack）/1、TOPACK OM stick流水线/TOPACK OM stick流水线(扬子江现场)20170726/topack主机(扬子江现场)20170726/计量盒/20170726-计量盒 (20).JPG', 'image/.jpg', '1.9 MB', '', '2020-06-27 21:24:12', 2002961, '20170726-计量盒 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (279, 64, '1203591837', 'e62c0983a028726163d334f5e640024d', 'ce6c2b2794c9407d8a5b2f857306b678', '/营销素材展示/同行信息/001、国际同行/Hoonga兴亚/1、hoonga20140425药机展展示设备/兴亚机器外观 (20).JPG', 'image/.jpg', '3.0 MB', '', '2020-06-27 21:24:12', 3105992, '兴亚机器外观 (<mark>20</mark>).JPG');
INSERT INTO `album_src` VALUES (280, 64, '1203741159', '08c75559ace0e333c291d19f607ceac2', '5c990bd4e7c3432a959be0e01631fe8b', '/营销素材展示/同行信息/001、国际同行/Solpac/3、优力士（Solpac）/1、8列螺杆机/1、8列螺杆机照片（优力士现场）2017.11.6/优力士现场solpack主机 (20).jpg', 'image/.jpg', '2.8 MB', '', '2020-06-27 21:24:12', 2894719, '优力士现场solpack主机 (<mark>20</mark>).jpg');
INSERT INTO `album_src` VALUES (281, 64, '1096956306', 'b6e03a8841dad701d6a086833fa62a8a', '33c0043598df4137a63cfb16eec48dc5', '/营销素材展示/STICK线/009、客户宣传视频/001、液体/3、SIS功能饮料/SiS WHEY20.mp4', 'video/.mp4', '3.6 MB', '', '2020-06-27 21:24:12', 3756617, 'SiS WHEY<mark>20</mark>.mp4');
INSERT INTO `album_src` VALUES (282, 64, '1203599436', '6c2cf52445e04a32455cd723dbce3754', '72a09a27fd8347fb84adfefd5ce7725e', '/营销素材展示/同行信息/001、国际同行/Inever/2、伊利（Inever）/6列螺杆机/设备照片/INEVER六列机20.JPG', 'image/.jpg', '1.2 MB', '', '2020-06-27 21:24:12', 1210604, 'INEVER六列机<mark>20</mark>.JPG');
INSERT INTO `album_src` VALUES (283, 63, '1084281549', 'd33612105ca70b94640f368412798acd', '030f04273f0b4b349b537354cab0c2f0', '/营销素材展示/公司介绍/005、一号园资料/001、一号园照片/005、办公环境/1、办公家具/办公家具 (20).JPG', 'image/.jpg', '', '', '2020-07-06 00:38:38', 0, '');
INSERT INTO `album_src` VALUES (284, 63, '1203702594', '6607c292967eb110ad318f0ab081fbda', '8cfb996e7a8b4c38bde60652403dd7c6', '/营销素材展示/同行信息/001、国际同行/Schmucker/1、公司资料/Schmucker多机图纸资料/schmucker说明书 Manual PDF M09056/Microsoft Word - M09056_20_00_GB.pdf', 'doc/.pdf', '', '', '2020-07-06 00:38:38', 0, '');
INSERT INTO `album_src` VALUES (285, 63, '1170967065', '7ce178c7d480abce990ffe937a7e1b96', 'e4e9ed553b8747e3a96321965fc89e12', '/营销素材展示/听装线/002、设备照片/002、客户现场照片/002、汤臣倍健听装线照片/汤臣倍健-听装线-现场照片 (20).jpg', 'image/.jpg', '', '', '2020-07-06 00:38:38', 0, '');
INSERT INTO `album_src` VALUES (286, 63, '1203627102', 'd3f215d446c018041a55f6c393c5aa31', 'b952d42db5cc4b6fb8a5c98d5a396907', '/营销素材展示/同行信息/001、国际同行/Sanko三光/1、公司资料/3、设备宣传视频/sanko-粉剂-20列机运行录像fc 1000.mp4', 'video/.mp4', '', '', '2020-07-06 00:38:38', 0, '');
INSERT INTO `album_src` VALUES (287, 63, '1204240482', 'ac246f36c933594fa8ee55d984371c33', '04f47fe1052943729a2b3a6ce3c48711', '/营销素材展示/同行信息/002、国内同行/D德尚/7、唐传生物（德尚）/10列螺杆机/设备照片20120519/天津德尚粉剂十列机20.JPG', 'image/.jpg', '', '', '2020-07-06 00:38:38', 0, '');
INSERT INTO `album_src` VALUES (288, 63, '1204394349', 'd05f43ff3e43f9228e8dfc98e54aff3e', '9665b535e78c49e1b0721abc9ada0aec', '/营销素材展示/同行信息/002、国内同行/S松川/1、公司资料/3、展会设备照片&视频/松川旋转提升机(2015武汉展现场)/旋转提升机/松川提升机静态照片 (20).JPG', 'image/.jpg', '', '', '2020-07-06 00:38:38', 0, '');
INSERT INTO `album_src` VALUES (296, 66, '1290845445', '74fdc4cbd0dc15c361751941512a687c', 'f2955c4cb1c440b9a98a99cf3596531a', '/高分子项目/高速充填.mp4', 'video/.mp4', '354.7 MB', '', '2020-07-09 00:00:10', 371911581, '高速充填.<mark>mp4</mark>');
INSERT INTO `album_src` VALUES (297, 66, '12040272571', '6486d0c612d68b93d265fc85d78b0685', '211baa42652d43568b62af1f5a50aa21', '/营销素材展示/同行信息/001、国际同行/Topack/2、扬子江（Topack）/1、TOPACK OM stick流水线/TOPACK OM stick流水线(扬子江现场)20170726/装盒机(扬子江现场)20170726/20170726推手.mp4', 'video/.mp4', '437.6 MB', '', '2020-07-09 00:00:10', 458828865, '20170726推手.<mark>mp4</mark>');
INSERT INTO `album_src` VALUES (298, 65, '1203606219', 'f035ae6a228c5e252d6561dd16cac35e', '178d03347f2d40d68d18411679739f9f', '/营销素材展示/同行信息/001、国际同行/Mespack/2、雀巢（Mespack）/STICKPACK AUGER FILLER雀巢mispak/CATALOGUES/ROCKWELL.pdf', 'doc/.pdf', '1.2 MB', '', '2020-07-13 23:51:59', 1277624, 'ROCKWELL.<mark>pdf</mark>');
INSERT INTO `album_src` VALUES (299, 65, '748680209', 'd5d2a443e7f1a3809d3733b368312020', '4958ffb0b8a911e8b1f70017fa00f183', '/一号园IT项目组/英文版-NVIDIA_vGPU_P系列GPU部署手册-基于vSphere 6.5u1和Horizon 7.pdf', 'doc/.pdf', '8.8 MB', '', '2020-07-13 23:51:59', 9196800, '英文版-NVIDIA_vGPU_P系列GPU部署手册-基于vSphere 6.5u1和Horizon 7.<mark>pdf</mark>');
INSERT INTO `album_src` VALUES (300, 65, '372555973', '56767ba2a13f19eff9b28cd85b3e5ca4', '99bfd39771e34d138625808bebba81ac', '/一号园IT项目组/006文档云/坚果云/【03】坚果云高级使用技巧.pdf', 'doc/.pdf', '1.5 MB', '', '2020-07-13 23:51:59', 1556152, '【03】坚果云高级使用技巧.<mark>pdf</mark>');
INSERT INTO `album_src` VALUES (301, 65, '1204382223', 'c2dfeb1097523f7037fa909f8ce41d55', '0cb40f6355364ac2b645460e281afc7a', '/营销素材展示/同行信息/002、国内同行/F发泰/002、济川（发泰）/1、12列计量盒整线/3、设备问题/发泰在济川现场的情况20170913/诊断喷胶问题2.jpg.jpg', 'image/.jpg', '1.2 MB', '', '2020-07-13 23:51:59', 1297337, '诊断喷胶问题2.<mark>jpg.jpg</mark>');
INSERT INTO `album_src` VALUES (302, 65, '2053410156', '4fca8b91257e23f03dad65f22e54a105', 'c515d2ed10e846b8a3b84064203d8935', '/营销素材展示/STICK线/005、细分市场引导素材/001、液体stick引导素材/002、瓶装对比素材/瓶装液体的消费者体验/外观/不防伪.jpg', 'image/.jpg', '218.1 KB', '', '2020-07-13 23:51:59', 223342, '不防伪.<mark>jpg</mark>');
INSERT INTO `album_src` VALUES (303, 65, '1203606414', '73477ac658ef2673df2c97bb55ad74e3', '2833752effb4497eb504eb0277493b80', '/营销素材展示/同行信息/001、国际同行/Mespack/2、雀巢（Mespack）/STICKPACK AUGER FILLER雀巢mispak/STICKPACK-XL_English.doc', 'doc/.doc', '5.4 MB', '', '2020-07-13 23:51:59', 5619712, 'STICKPACK-XL_English.<mark>doc</mark>');
INSERT INTO `album_src` VALUES (306, 33, '1092814890', 'ba3ad14ff85a03c2194c8782d1242c59', '805b870e8c6e41e8a7a189f433f6a3ba', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (1).JPG', 'image/.jpg', '13.1 MB', '', '2020-07-30 23:16:26', 13759186, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (1).JPG');
INSERT INTO `album_src` VALUES (307, 33, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2020-07-30 23:16:26', 13393468, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (2).JPG');
INSERT INTO `album_src` VALUES (316, 72, '1084281549', 'd33612105ca70b94640f368412798acd', '030f04273f0b4b349b537354cab0c2f0', '/营销素材展示/公司介绍/005、一号园资料/001、一号园照片/005、办公环境/1、办公家具/办公家具 (20).JPG', 'image/.jpg', '', '', '2020-07-30 23:20:13', 0, '');
INSERT INTO `album_src` VALUES (317, 72, '1203702594', '6607c292967eb110ad318f0ab081fbda', '8cfb996e7a8b4c38bde60652403dd7c6', '/营销素材展示/同行信息/001、国际同行/Schmucker/1、公司资料/Schmucker多机图纸资料/schmucker说明书 Manual PDF M09056/Microsoft Word - M09056_20_00_GB.pdf', 'doc/.pdf', '', '', '2020-07-30 23:20:13', 0, '');
INSERT INTO `album_src` VALUES (318, 72, '1170967065', '7ce178c7d480abce990ffe937a7e1b96', 'e4e9ed553b8747e3a96321965fc89e12', '/营销素材展示/听装线/002、设备照片/002、客户现场照片/002、汤臣倍健听装线照片/汤臣倍健-听装线-现场照片 (20).jpg', 'image/.jpg', '', '', '2020-07-30 23:20:13', 0, '');
INSERT INTO `album_src` VALUES (319, 72, '1203627102', 'd3f215d446c018041a55f6c393c5aa31', 'b952d42db5cc4b6fb8a5c98d5a396907', '/营销素材展示/同行信息/001、国际同行/Sanko三光/1、公司资料/3、设备宣传视频/sanko-粉剂-20列机运行录像fc 1000.mp4', 'video/.mp4', '', '', '2020-07-30 23:20:13', 0, '');
INSERT INTO `album_src` VALUES (320, 72, '1204394349', 'd05f43ff3e43f9228e8dfc98e54aff3e', '9665b535e78c49e1b0721abc9ada0aec', '/营销素材展示/同行信息/002、国内同行/S松川/1、公司资料/3、展会设备照片&视频/松川旋转提升机(2015武汉展现场)/旋转提升机/松川提升机静态照片 (20).JPG', 'image/.jpg', '', '', '2020-07-30 23:20:13', 0, '');
INSERT INTO `album_src` VALUES (321, 72, '1204240482', 'ac246f36c933594fa8ee55d984371c33', '04f47fe1052943729a2b3a6ce3c48711', '/营销素材展示/同行信息/002、国内同行/D德尚/7、唐传生物（德尚）/10列螺杆机/设备照片20120519/天津德尚粉剂十列机20.JPG', 'image/.jpg', '', '', '2020-07-30 23:20:13', 0, '');
INSERT INTO `album_src` VALUES (322, 73, '2256882247', 'eaea8a438f34772dda25690e7cc90146', '2fb631a3c39744248124c32f51c077fb', '/营销素材展示/泡罩线/004、泡罩线设备视频/仅一PRESTA6000泡罩线运行录像20200706.mp4', 'video/.mp4', '749.5 MB', '', '2020-08-02 16:52:59', 785951415, '');
INSERT INTO `album_src` VALUES (331, 76, '1084277118', '336e8fc142b0d65832a552a358a103ea', '6da581e680c14bf3b599f33ffdea7578', '/营销素材展示/000、一号园照片/01、南门房/火车头.jpg', 'image/.jpg', '', '', '2020-08-03 22:54:44', 7495103, '火车头.jpg');
INSERT INTO `album_src` VALUES (332, 76, '1084277652', 'decad0c3da51a70c26c225bc2da0bcb3', '9b87bf7615314c8dad362b901346a474', '/营销素材展示/000、一号园照片/01、南门房/形象墙.jpg', 'image/.jpg', '18.6 MB', '', '2020-08-03 22:54:44', 19467083, '');
INSERT INTO `album_src` VALUES (333, 76, '1084277109', '453ff946a38821ee945f9f51b9bf36cb', 'ab1fb5efe32448bcbcf9f7f25c49d164', '/营销素材展示/000、一号园照片/01、南门房/南大门.jpg', 'image/.jpg', '6.5 MB', '', '2020-08-03 22:54:44', 6787527, '');
INSERT INTO `album_src` VALUES (350, 81, '1092814506', '0e9d2a2ec60b54c77c8661d7e4bc29b3', '571801605c88487d804401115ec3a8bc', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/014、金诃藏药（安儿宁颗粒）/1、金诃藏药-安儿宁颗粒/金诃藏药-安儿宁颗粒20170330 (1).JPG', 'image/.jpg', '1.6 MB', '', '2020-08-03 23:04:42', 1637498, '');
INSERT INTO `album_src` VALUES (351, 81, '1092814698', '3d8595b0b5ae297ec2ccc4c967a60d20', 'fed11d0b32da4985827b607c7929b175', '/营销素材展示/001、药品行业stick生产线/001、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/014、金诃藏药（安儿宁颗粒）/1、金诃藏药-安儿宁颗粒/金诃藏药-安儿宁颗粒20170330 (2).jpg', 'image/.jpg', '7.5 MB', '', '2020-08-03 23:04:42', 7893494, '');
INSERT INTO `album_src` VALUES (352, 81, '1202889657', '33213bd44fd74b541e97b0411956b70c', 'ecaf3c5a59974cd780c3d2278d482637', '/营销素材展示/004、公司介绍资料/1、公司介绍资料/1、仅一简介（含代工）20180417.pptx', 'doc/.pptx', '43.4 MB', '', '2020-08-03 23:04:42', 45557587, '');
INSERT INTO `album_src` VALUES (353, 81, '1257850917', '9b358c883546d5a5ef04ec49cafda6db', 'a83241d749ca41b68fbe1c99a39fbabd', '/营销素材展示/001、药品行业stick生产线/003、设备视频/1、整线视频/STICK整线合集（压缩--手机播放用）.mp4', 'video/.mp4', '627.6 MB', '', '2020-08-03 23:04:42', 658100307, '');
INSERT INTO `album_src` VALUES (358, 83, '1092814890', 'ba3ad14ff85a03c2194c8782d1242c59', '805b870e8c6e41e8a7a189f433f6a3ba', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (1).JPG', 'image/.jpg', '13.1 MB', '', '2020-08-03 23:04:45', 13759186, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (1).JPG');
INSERT INTO `album_src` VALUES (359, 83, '1092814890', 'ba3ad14ff85a03c2194c8782d1242c59', '805b870e8c6e41e8a7a189f433f6a3ba', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (1).JPG', 'image/.jpg', '13.1 MB', '', '2020-08-03 23:04:45', 13759186, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (1).JPG');
INSERT INTO `album_src` VALUES (360, 83, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2020-08-03 23:04:45', 13393468, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (2).JPG');
INSERT INTO `album_src` VALUES (361, 83, '1092814911', 'e4e0555efda2f4ee6cf17088d4e25240', 'c85ed5b44987487a8d86bc4df58cf83f', '/营销素材展示/1、药品行业stick生产线/1、样品照片/1、药品/1、药品-颗粒剂/1、中药（儿童药）/4、仅一自制-小儿感冒颗粒/仅一自制-小儿感冒颗粒20170913 (2).JPG', 'image/.jpg', '12.8 MB', '', '2020-08-03 23:04:45', 13393468, '<mark>仅</mark><mark>一</mark>自制-小儿感冒颗粒20170913 (2).JPG');
INSERT INTO `album_src` VALUES (394, 92, '2322665284', '847c8b524f60c4671597a54341e7cc26', 'f3d4e5ee3d084e52abf0c43bcfe656d6', '/营销素材展示/其他生产线/002、AGV智能物流系统/佐丹力后道包装以及物料配送系统报价书20200711.pptx', 'doc/.pptx', '11.7 MB', '', '2020-08-04 00:07:45', 12278078, '');
INSERT INTO `album_src` VALUES (395, 92, '2322649408', 'ff45c7c50b6f3c7b50a8db6839083ad2', 'd904e4c3672147f2a89974e2c0d983b7', '/营销素材展示/其他生产线/002、AGV智能物流系统/佐丹力智能物料配送系统模拟视频0610.mp4', 'video/.mp4', '8.2 MB', '', '2020-08-04 00:07:45', 8587614, '');
COMMIT;

-- ----------------------------
-- Table structure for joyea_user
-- ----------------------------
DROP TABLE IF EXISTS `joyea_user`;
CREATE TABLE `joyea_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `joyea_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `joyea_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `department` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_admin` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=599 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of joyea_user
-- ----------------------------
BEGIN;
INSERT INTO `joyea_user` VALUES (323, '20', '司马小兴', '设计师', '刘潇部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (324, '448', '景建华', '设计师', '刘潇部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (325, '542', '朱志鹏', '助理设计师', '刘潇部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (326, '685', '刘潇', '代主管', '刘潇部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (327, '850', '钱佳欣', '营销专员', '刘潇部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (328, '932', '贾青浩', '见习设计师', '刘潇部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (329, '446', '李秋霞', '营销专员', '陈鹏部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (330, '696', '陈鹏', '代主管', '陈鹏部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (331, '893', '缪嘉伟', '见习设计师', '陈鹏部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (332, '900', '王子路', '见习设计师', '陈鹏部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (333, '934', '吴文浩', '见习设计师', '陈鹏部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (334, '945', '赵松庆', '见习设计师', '陈鹏部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (335, '305', '姜金洪', '代主管', '金洪部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (336, '395', '吴春琴', '营销专员', '金洪部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (337, '572', '廖灿飞', '设计师', '金洪部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (338, '816', '杨红进', '助理设计师', '金洪部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (339, '886', '殷更红', '见习设计师', '金洪部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (340, '389', '郑志超', '代主管', '志超部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (341, '462', '王为民', '营销专员', '志超部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (342, '803', '代兴超', '助理设计师', '志超部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (343, '852', '黄彦博', '助理设计师', '志超部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (344, '867', '王致炜', '助理设计师', '志超部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (345, '905', '王纬经', '见习设计师', '志超部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (346, '233', '陈秀东', '设计师', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (347, '242', '吴懿', '设计师', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (348, '418', '束义', '代主管', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (349, '538', '梅宝锋', '设计师', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (350, '622', '刘彩铃', '营销专员', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (351, '845', '胡瑞', '设计师', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (352, '903', '潘恒', '见习设计师', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (353, '906', '刘彤', '见习设计师', '束义部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (354, '567', '王叶飞', '主管', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (355, '603', '张云杰', '设计师', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (356, '630', '赵强', '营销专员', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (357, '679', '张松林', '助理设计师', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (358, '766', '崔宏山', '助理设计师', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (359, '857', '张红霞', '营销专员', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (360, '917', '耿玉芝', '见习设计师', '叶飞部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (361, '874', '隋亚民', '技术专家', '加工平台', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (362, '876', '张二锋', '技术专家', '加工平台', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (363, '897', '於松波', '项目助理', '加工平台', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (364, 'EN001', '蒋星波', '部门经理', '加工平台', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (365, '王宇', '王宇', '技术专家', '加工平台', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (366, '13', '杨金浩', '呆滞品处理员', '供应链改革委员会', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (367, '222', '王丹翔', '营销专员', '金洲部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (368, '421', '丁金中', '主设计师', '金洲部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (369, '423', '朱红军', '营销专员', '金洲部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (370, '674', '董传桂', '助理设计师', '金洲部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (371, '705', '金洲', '代主管', '金洲部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (372, '931', '王佳', '见习设计师', '金洲部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (373, '37', '周新民', '工程管理员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (374, '168', '杭俊', '工程管理员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (375, '400', '严光庆', '项目经理', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (376, '614', '刘国先', '工程管理员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (377, '702', '徐红兰', '项目经理', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (378, '739', '金川', '工程管理员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (379, '791', '张海姣', '工程管理员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (380, '837', '戴新法', '驾驶员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (381, '839', '束炜', '工程管理员', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (382, '910', '袁华军', '主管', '一号园', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (383, '209', '江红', '设计师', '张辉部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (384, '473', '眭平波', '营销专员', '张辉部', '2020-06-01 22:59:53', '123456', 0);
INSERT INTO `joyea_user` VALUES (385, '493', '张辉', '代主管', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (386, '547', '胡洪金', '设计师', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (387, '583', '袁晓华', '助理设计师', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (388, '613', '潘志鑫', '见习设计员', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (389, '639', '张雨亭', '见习设计员', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (390, '815', '徐浩', '见习设计员', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (391, '898', '杨旭', '见习设计员', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (392, '936', '林文兴', '见习设计员', '张辉部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (393, '15', '陈锋', '经理', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (394, '139', '贡忠萍', '三维布线布管专员', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (395, '169', '徐夏强', '设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (396, '207', '王建', '高级设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (397, '224', '李锦秀', '营销专员', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (398, '336', '贡俊杰', '助理设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (399, '398', '周康', '助理设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (400, '399', '刘辉', '助理设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (401, '511', '葛涌', '助理设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (402, '548', '马建风', '助理设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (403, '586', '王威', '设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (404, '681', '王鑫灏', '见习设计员', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (405, '729', '周国权', '助理设计师', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (406, '892', '陈流思源', '见习设计员', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (407, '901', '丁晨光', '见习设计员', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (408, '935', '刘路', '见习设计员', '陈锋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (409, '70', '黄国兰', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (410, '226', '王翠萍', '战略促进部经理', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (411, '524', '顾雯', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (412, '574', '姜懿', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (413, '619', '束玲', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (414, '719', '费文', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (415, '757', '黄赛赛', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (416, '794', '贡倩', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (417, '831', '郦泽莹', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (418, '884', '壮梦瑶', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (419, '918', '潘雨榕', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (420, '923', '郭烨', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (421, '942', '赵倩', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (422, '951', '李倩', '战略促进专员', '战略促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (423, '248', '史丽萍', '行政部代主管', '行政服务中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (424, 'Y0027', '张丽', '行政专员', '行政服务中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (425, '65', '张海燕', '会计助理', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (426, '85', '杭玉华', '会计', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (427, '110', '范亚平', '财务部主管', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (428, '249', '王琴霞', '会计', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (429, '551', '刘雪', '会计', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (430, '847', '秦雅琴', '会计助理', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (431, '925', '贺雪聪', '会计助理', '财务支持中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (432, '213', '刘剑云', 'IT部代主管', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (433, '590', '郦俊辉', '企业信息管理师', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (434, '641', '王佳新', '企业信息管理师', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (435, '652', '张秀', 'IT部助理', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (436, '658', '司马云良', '企业信息管理师', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (437, '692', '徐东岩', '产品经理', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (438, '755', '王红红', '企业信息管理师', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (439, '801', '莫阳丹', '企业信息管理师', '数字化运营中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (440, '22', '吴华军', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (441, '28', '吴黎军', '问题跟踪处理员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (442, '32', '朱建新', '项目经理', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (443, '63', '袁京军', '项目经理', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (444, '120', '韦春永', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (445, '172', '束叶军', '售后服务组组长', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (446, '173', '茅敏', '项目调度中心组长', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (447, '177', '芮丹军', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (448, '193', '潘军华', '装配技师', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (449, '206', '王月花', '项目助理', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (450, '245', '陈权辉', '项目经理', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (451, '259', '巢方平', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (452, '270', '周燕', '项目助理', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (453, '295', '史晓强', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (454, '304', '徐磊', '质量促进专员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (455, '308', '刘进', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (456, '315', '肖杰', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (457, '347', '尹晨辉', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (458, '353', '蒋明良', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (459, '361', '蒋海平', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (460, '367', '朱滨', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (461, '402', '缪燕', '发货员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (462, '412', '王磊', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (463, '415', '王伟俊', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (464, '427', '王宙', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (465, '429', '毛星军', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (466, '431', '马银宝', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (467, '451', '柴丹辉', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (468, '453', '戎建平', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (469, '472', '魏文明', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (470, '481', '刘翔', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (471, '485', '孙谦', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (472, '486', '孔小波', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (473, '501', '胡叶华', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (474, '504', '陈凯', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (475, '509', '王秋平', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (476, '518', '朱浩翔', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (477, '523', '赵炜', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (478, '531', '马列辉', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (479, '533', '陈阳', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (480, '534', '马旭华', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (481, '561', '胡丽君', '问题跟踪处理员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (482, '564', '傅宁', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (483, '582', '朱杰', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (484, '620', '冷向辉', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (485, '626', '薛凯', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (486, '646', '巢炜华', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (487, '650', '李新潮', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (488, '660', '芦路', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (489, '690', '郦阳平', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (490, '698', '丁鹏', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (491, '700', '蒋伟', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (492, '718', '孟安', '装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (493, '758', '蒋辉', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (494, '762', '杨志明', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (495, '810', '唐琪', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (496, '817', '谈涛', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (497, '818', '杜超', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (498, '823', '谢明煌', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (499, '827', '刘凯', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (500, '838', '陈玉涛', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (501, '887', '陈磊', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (502, '896', '石磊', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (503, '911', '严明', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (504, '912', '贡佳伟', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (505, '913', '胡玉洁', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (506, '915', '刘俊杰', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (507, '916', '张文浩', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (508, '920', '尤伟波', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (509, '921', '马尊轼', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (510, '922', '孙琦', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (511, '926', '贺凯', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (512, '927', '荆钰琦', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (513, '929', '何振宇', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (514, '937', '聂琦', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (515, '939', '吕健康', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (516, '940', '吴延柱', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (517, '943', '郦亚凯', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (518, '944', '马华新', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (519, '946', '赵云杰', '见习装配员', '装调资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (520, '27', '潘春荣', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (521, '36', '钟爱平', '外联中心经理', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (522, '66', '吴云书', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (523, '91', '张文娟', '采购专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (524, '144', '庄华榕', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (525, '229', '张杏娣', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (526, '258', '张俊辉', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (527, '422', '秦天成', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (528, '465', '邱月春', '外协专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (529, '497', '郦丹洋', '采购专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (530, '525', '周玲', '物流专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (531, '593', '丁超群', '采购专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (532, '806', '贺丽华', '物流专员', '外联资源保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (533, '9', '周毓', '项目经理', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (534, '555', '张小平', '设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (535, '585', '钟文杰', '助理设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (536, '611', '顾元义', '设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (537, '666', '刘雨', '设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (538, '701', '张政', '助理设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (539, '770', '王涛', '项目经理', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (540, '826', '杨树恒', '助理设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (541, '842', '陈志伟', '见习设计员', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (542, '846', '张晨', '装配员', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (543, '860', '冷勇勇', '助理设计师', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (544, '891', '束伟斌', '见习设计员', '义锋阁', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (545, '232', '李金国', '金国部经理', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (546, '382', '安明', '设计师', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (547, '454', '毛根宝', '设计师', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (548, '623', '岳锦涛', '营销专员', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (549, '707', '高海峰', '设计师', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (550, '800', '郑治宇', '助理设计师', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (551, '899', '任波', '见习设计员', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (552, '933', '孟浩', '见习设计员', '金国部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (553, '200', '吴书荣', '营销专员', '周洋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (554, '230', '王立波', '助理设计师', '周洋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (555, '424', '周洋', '周洋部经理', '周洋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (556, '570', '朱飞', '助理设计师', '周洋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (557, '877', '任玲', '营销专员', '周洋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (558, '894', '王浩', '见习设计员', '周洋部', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (559, '152', '史云霞', '创新促进部经理', '创新促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (560, '195', '陈娟', '标准化推广员', '创新促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (561, '756', '任远', '档案管理员', '创新促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (562, '875', '赵宇明', '项目技术支持员', '创新促进中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (563, '435', '王海霞', '质量评估员', '客户利益保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (564, '438', '夏同会', '备件专员', '客户利益保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (565, '487', '袁红星', '检验员', '客户利益保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (566, '528', '朱发英', '质量评估员', '客户利益保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (567, '687', '温贤海', '客户利益保障部代经理', '客户利益保障中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (568, '176', '潘燕', '营销专员', '营销中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (569, '220', '王黎剑', '轮值营销执行总监', '营销中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (570, '272', '吴烨梅', '营销专员', '营销中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (571, '301', '王金', '营销专员', '营销中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (572, '484', '陈尚琛', '营销专员', '营销中心', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (573, '717', '窦阳阳', '营销专员', '营销中心', '2020-06-01 22:59:54', '123321', 0);
INSERT INTO `joyea_user` VALUES (574, '1', '吴立平', '总裁', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (575, '6', '许彩萍', '营销总监', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (576, '11', '吴立亚', '副总裁', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (577, '16', '周进军', '技术总监', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (578, '45', '殷祥根', '总工程师', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (579, '48', '马炳林', '副总裁', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (580, '198', '薛德四', '副总裁', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (581, '640', '吴乃奇', '副总裁', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (582, '840', 'XIA HANNA', '副总裁', '总裁办', '2020-06-01 22:59:54', '123456', 0);
INSERT INTO `joyea_user` VALUES (583, '0032', '朱建新', '项目经理', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (584, '0028', '吴黎军', '包装发货专员', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (585, '0022', '吴华军', '实验室主管', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (586, '0016', '周进军', '副总裁/部门经理', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (587, '0120', '韦春永', 'STICK线装配组长', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (588, '0070', '黄国兰', '助理', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (589, '1013', '史珂', '助理', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (590, '0402', '缪燕', '售后服务专员', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (591, '0172', '束叶军', '售后组组长', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (592, '0173', '茅敏', '项目部主管', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (593, '0177', '芮丹军', '听装调试组长', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (594, '0193', '潘军华', '听装装配组长', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (595, '0206', '王月花', '辅助组助理', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (596, '0308', '刘进', '项目经理', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (597, '0259', '巢方平', '装调工程师', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
INSERT INTO `joyea_user` VALUES (598, '0245', '陈权辉', '装调工程师', '装调资源保障中心', '2020-08-23 18:02:47', '123456', 0);
COMMIT;

-- ----------------------------
-- Table structure for src_collect
-- ----------------------------
DROP TABLE IF EXISTS `src_collect`;
CREATE TABLE `src_collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `src_neid` int NOT NULL,
  `user_id` int NOT NULL,
  `src_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_rev` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `src_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of src_collect
-- ----------------------------
BEGIN;
INSERT INTO `src_collect` VALUES (18, 1071142929, 696821, '/信息化需求评级排序及执行反馈/1、素材库/外联开发素材清单/测试素材/中智-玛咖代用茶-样盒&样袋20151212.png', 'image/.png', '033eb9c0dcba66c0f77f5d5650db3bb5', '616621933faf413882dd18a59a9ce6a9', '736.1 KB', '2019-05-09 15:43:02');
INSERT INTO `src_collect` VALUES (21, 391664415, 696047, '/各部门全共享资料/财务部/仅一LOGO/9.JOYEA仅一1-反黑.gif', 'image/.gif', '5c3565a58f525f408b6a164cfd317a1a', '47cce0eb3c9c4a64ab22902789a95602', '11.5 KB', '2019-05-09 18:17:57');
INSERT INTO `src_collect` VALUES (22, 595239289, 696047, '/IT运维/点检图片/04.05/04.05 VPN正常.png', 'image/.png', '9df4e3813e51490f6fd2a876f4f11017', 'cc1bb64cad2548099e3fdcb106085bbe', '36.9 KB', '2019-05-13 20:26:26');
INSERT INTO `src_collect` VALUES (24, 1084272579, 712177, '/营销素材展示/0、一号园照片/00、一号园鸟瞰/一号园-鸟瞰图 (1).jpg', 'image/.jpg', '25b1bbd60ce4aacede51dd5f284faf7b', '1588f7d7447c4f85a13cbee32f8e7aeb', '5.6 MB', '2019-06-26 14:28:46');
INSERT INTO `src_collect` VALUES (25, 1084277109, 696821, '/营销素材展示/0、一号园照片/01、南门房/南大门.jpg', 'image/.jpg', '453ff946a38821ee945f9f51b9bf36cb', 'ab1fb5efe32448bcbcf9f7f25c49d164', '6.5 MB', '2019-07-02 10:07:09');
INSERT INTO `src_collect` VALUES (26, 1092823956, 799277, '/营销素材展示/001、药品行业stick生产线/001、样品照片/2、保健品/2、保健品-粉剂/012、杜邦（益生菌固体饮料）/1、杜邦（HOWARU益生菌固体饮料）/杜邦-HOWARU益生菌固体饮料20140616.JPG', 'image/.jpg', '43879bcc279b3ee6189890b89035fdbe', 'e990418726cd4b3b9d86c50caa042928', '774.7 KB', '2020-03-13 11:38:16');
COMMIT;

-- ----------------------------
-- Table structure for upload_record
-- ----------------------------
DROP TABLE IF EXISTS `upload_record`;
CREATE TABLE `upload_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uploader` varchar(50) DEFAULT NULL,
  `src_neid` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `checked` tinyint(1) DEFAULT NULL,
  `checked_at` timestamp NULL DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `upload_path` varchar(255) DEFAULT NULL,
  `src_type` varchar(255) DEFAULT NULL,
  `src_rev` varchar(255) DEFAULT NULL,
  `src_hash` varchar(255) DEFAULT NULL,
  `src_name` varchar(255) DEFAULT NULL,
  `upload_path_neid` varchar(50) DEFAULT NULL,
  `refuse_reason` varchar(255) DEFAULT NULL,
  `temp_src_name` varchar(100) DEFAULT NULL,
  `src_desc` varchar(255) DEFAULT NULL,
  `is_pc_upload` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of upload_record
-- ----------------------------
BEGIN;
INSERT INTO `upload_record` VALUES (53, '20', '2477338555', '2020-08-25 20:12:27', 1, '2020-08-25 20:19:45', '', '/营销素材展示/STICK线', 'image/.jpg', 'fcab5bd500e54dd6bc56c06545b773ff', '9dc81fcc759391b2409fba121961e296', '123.jpg', '1092814122', NULL, '536c6200-0de1-4045-b087-8f17aa2a966d.jpg', '123123123', 0);
INSERT INTO `upload_record` VALUES (54, '20', '2477341117', '2020-08-25 20:13:37', 0, NULL, '', NULL, 'video/.mp4', 'a31d00541bdd48869142f28064c38005', '9759f43c944d7e019800b46b3ac155c1', NULL, NULL, NULL, 'cd5cea51-0bbb-4be3-8fa6-e02411139c4d.mp4', 'qeqweqw', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
