/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 112.124.12.211:3306
 Source Schema         : lyf_food

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 27/10/2020 16:06:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for food_attr
-- ----------------------------
DROP TABLE IF EXISTS `food_attr`;
CREATE TABLE `food_attr`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '食物属性id',
  `food_id` int(11) DEFAULT NULL COMMENT '食物id',
  `attgroup_id` int(11) DEFAULT NULL COMMENT '属性组id',
  `attr_name` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '属性名',
  `attr_value` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `food_id`(`food_id`) USING BTREE,
  CONSTRAINT `re_attr` FOREIGN KEY (`food_id`) REFERENCES `food_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 769 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_attr_group
-- ----------------------------
DROP TABLE IF EXISTS `food_attr_group`;
CREATE TABLE `food_attr_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性组id',
  `group_name` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '属性组名',
  `search` tinyint(5) NOT NULL DEFAULT 0 COMMENT '0不可被检索，1可被检索',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `food_category_relation`;
CREATE TABLE `food_category_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `food_id` int(11) DEFAULT NULL COMMENT '食物id',
  `category_id` int(11) DEFAULT NULL COMMENT '分类id',
  `food_name` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '食物名称',
  `category_name` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `food_id`(`food_id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  CONSTRAINT `re_category` FOREIGN KEY (`food_id`) REFERENCES `food_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 247 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_images
-- ----------------------------
DROP TABLE IF EXISTS `food_images`;
CREATE TABLE `food_images`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '食物图片id',
  `food_id` int(11) DEFAULT NULL COMMENT '食物id',
  `images_url` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '图片地址',
  `type` tinyint(10) DEFAULT NULL COMMENT '0为默认图片，1为非默认图片',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `food_id`(`food_id`) USING BTREE,
  CONSTRAINT `re_img` FOREIGN KEY (`food_id`) REFERENCES `food_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_info
-- ----------------------------
DROP TABLE IF EXISTS `food_info`;
CREATE TABLE `food_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'food_id',
  `title` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '食物标题',
  `descrit` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '食物描述',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `auditing` tinyint(5) NOT NULL DEFAULT 1 COMMENT '审核状态(0审核通过，1待通过)',
  `s_id` int(11) DEFAULT NULL COMMENT '所属专题',
  `create_time` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `s_id`(`s_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_special
-- ----------------------------
DROP TABLE IF EXISTS `food_special`;
CREATE TABLE `food_special`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '专题id',
  `name` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '专题名字',
  `imgurl` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '专题图片',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '显示状态(0显示，1不显示)',
  `create_time` varchar(100) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_special_infomation
-- ----------------------------
DROP TABLE IF EXISTS `food_special_infomation`;
CREATE TABLE `food_special_infomation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '专题详情id',
  `imgurl` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '详情图片',
  `content` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '详情内容',
  `s_id` int(11) DEFAULT NULL COMMENT '相关专题id',
  `status` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `s_id`(`s_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food_step
-- ----------------------------
DROP TABLE IF EXISTS `food_step`;
CREATE TABLE `food_step`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '步骤id',
  `food_id` int(11) DEFAULT NULL COMMENT '食物id',
  `content` varchar(255) CHARACTER SET gbk COLLATE gbk_bin DEFAULT NULL COMMENT '步骤讲解',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `food_id`(`food_id`) USING BTREE,
  CONSTRAINT `re_step` FOREIGN KEY (`food_id`) REFERENCES `food_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 595 CHARACTER SET = gbk COLLATE = gbk_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sort
-- ----------------------------
DROP TABLE IF EXISTS `t_sort`;
CREATE TABLE `t_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `show_status` tinyint(4) DEFAULT 1,
  `sort` int(11) DEFAULT 0,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 340 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
