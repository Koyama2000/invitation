/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.5.28 : Database - invitationdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`invitationdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `invitationdb`;

/*Table structure for table `invitation` */

DROP TABLE IF EXISTS `invitation`;

CREATE TABLE `invitation` (
  `id` bigint(4) NOT NULL AUTO_INCREMENT COMMENT '帖子编号',
  `title` varchar(20) NOT NULL COMMENT '帖子标题',
  `summary` varchar(255) NOT NULL COMMENT '帖子摘要',
  `author` varchar(20) DEFAULT NULL COMMENT '作者',
  `createdate` date NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `invitation` */

insert  into `invitation`(`id`,`title`,`summary`,`author`,`createdate`) values 
(1,'中纪委网站发文双节前点名高端白酒 释放什','中央纪委国家监委网站发文双节前点名高端白酒释放什么信号？','成都商报','2020-09-24'),
(2,'高空抛物引发的思考','最近，一则“天降铁球砸死女婴，整栋楼判赔”的消息，再次引发公众对铁球伤人案的讨论','甜蜜蜜12321','2020-09-24'),
(4,'中纪委网站发文双节','中纪委网高端白酒 释放什','玩家47','2020-03-05');

/*Table structure for table `reply_detail` */

DROP TABLE IF EXISTS `reply_detail`;

CREATE TABLE `reply_detail` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `invid` bigint(8) NOT NULL COMMENT '帖子编号',
  `content` varchar(255) NOT NULL COMMENT '回复内容',
  `author` varchar(25) NOT NULL COMMENT '回复人昵称',
  `createdate` date DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `reply_detail` */

insert  into `reply_detail`(`id`,`invid`,`content`,`author`,`createdate`) values 
(1,2,'wdadaw','awdaw','2020-10-04');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
