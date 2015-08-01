CREATE DATABASE  IF NOT EXISTS `adtracker` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `adtracker`;
-- MySQL dump 10.13  Distrib 5.5.43, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: adtracker
-- ------------------------------------------------------
-- Server version	5.5.43-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `click`
--

DROP TABLE IF EXISTS `click`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `click` (
  `impid` varchar(45) NOT NULL,
  `d1` varchar(45) NOT NULL,
  `appId` varchar(45) NOT NULL,
  `ts` varchar(45) NOT NULL,
  `adg` varchar(45) NOT NULL,
  `cmp` varchar(45) NOT NULL,
  `date` varchar(45) NOT NULL,
  PRIMARY KEY (`impid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `click`
--

LOCK TABLES `click` WRITE;
/*!40000 ALTER TABLE `click` DISABLE KEYS */;
INSERT INTO `click` VALUES ('abc-jhasd-123>','asjd','kjsd','1283756','abc','jhasd','1970-01-01'),('adg1-cmp1-id454','deviceid1','appId1','14523484743','adg1','cmp1','1970-06-18'),('adg1-cmp1-id456','deviceid1','appId1','14523484742','adg2','cmp1','1970-06-18'),('adg1-cmp1-id457','deviceid3','appId1','19523484742','adg1','cmp1','1970-08-15'),('adg2-cmp2-id456','deviceid3','appId1','19523484742','adg2','cmp2','1970-08-15'),('adg3-cmp3-id456','deviceid2','appId2','14523484747','adg1','cmp3','1970-06-18');
/*!40000 ALTER TABLE `click` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-07-03 17:04:09
