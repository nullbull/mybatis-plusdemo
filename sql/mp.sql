/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.24 : Database - testdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`testdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `testdb`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '涓婚敭ID',
  `name` varchar(30) DEFAULT NULL COMMENT '濮撳悕',
  `age` int(11) DEFAULT NULL COMMENT '骞撮緞',
  `email` varchar(50) DEFAULT NULL COMMENT '閭',
  `phone` varchar(11) DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`age`,`email`,`phone`,`sex`) values (1,'zwt',21,'2',NULL,NULL),(2,'niuzhenhao',20,'1',NULL,NULL),(3,'nzh',28,'3',NULL,NULL),(4,'test',21,'4',NULL,NULL),(5,'hhh',24,'5',NULL,NULL),(8,'justinniu',23,'1129114837@qq.com',NULL,NULL);

/*Table structure for table `user_test` */

DROP TABLE IF EXISTS `user_test`;

CREATE TABLE `user_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `nickname` varchar(16) DEFAULT NULL,
  `age` tinyint(4) DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `salt` varchar(6) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_by` tinyint(4) DEFAULT NULL,
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `login_ip` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

/*Data for the table `user_test` */

insert  into `user_test`(`id`,`username`,`password`,`phone`,`nickname`,`age`,`sex`,`email`,`salt`,`picture`,`created_at`,`updated_at`,`modify_by`,`last_login`,`login_ip`) values (41,'zwt','8113acdfc651db18d977159ddb2684b0','17611233021','牛贞昊',18,1,'1129114837@qq.com','123112',NULL,'2019-01-22 15:47:38','2019-01-22 15:47:38',0,'2019-01-22 15:47:38',NULL),(42,'nzh','91aba43ca53e8b0f4c477f6a7e62dcf8','13188759088',NULL,NULL,NULL,'niuhao.1996@qq.com','123112',NULL,'2019-01-23 10:54:09','2019-01-23 10:54:09',NULL,'2019-01-23 10:54:09',NULL),(43,'niuzhenhao','3a3c21872515e64e540691aed718d1ca','17888845416',NULL,NULL,NULL,'niuhao.1996@gmai.com','123112',NULL,'2019-01-24 10:24:16','2019-01-24 10:24:16',NULL,'2019-01-24 10:24:16',NULL),(44,'test','22f2416176b452048ff4584b28163d71','17611233023',NULL,NULL,NULL,'1129114837@qq.co','123112',NULL,'2019-01-24 14:06:46','2019-01-24 14:06:46',NULL,'2019-01-24 14:06:46',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
