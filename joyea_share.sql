/*
 Navicat Premium Data Transfer

 Source Server         : notebook
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 192.168.1.5:3306
 Source Schema         : joyea_share

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 28/04/2019 23:21:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
  `album_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `is_like` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`album_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for album_src
-- ----------------------------
DROP TABLE IF EXISTS `album_src`;
CREATE TABLE `album_src` (
  `_id` int(11) NOT NULL AUTO_INCREMENT,
  `album_id` int(11) NOT NULL,
  `src_neid` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_rev` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `src_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for src_like
-- ----------------------------
DROP TABLE IF EXISTS `src_like`;
CREATE TABLE `src_like` (
  `id` int(11) NOT NULL,
  `src_neid` int(11) NOT NULL,
  `user_id` varchar(0) COLLATE utf8mb4_bin NOT NULL,
  `src_path` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_type` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_rev` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `src_size` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime NOT NULL,
  `desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
