-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: airyami
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `tb_admin`
--

DROP TABLE IF EXISTS `tb_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin` (
  `ADMIN_ID` varchar(20) NOT NULL,
  `PASSWORD` varchar(100) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `ADMIN_TYPE` varchar(20) DEFAULT NULL,
  `USE_YN` char(1) DEFAULT NULL,
  `REG_ID` varchar(20) DEFAULT NULL,
  `REG_DATE` datetime DEFAULT NULL,
  `MOD_ID` varchar(20) DEFAULT NULL,
  `MOD_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ADMIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='관리자 정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_admin`
--

LOCK TABLES `tb_admin` WRITE;
/*!40000 ALTER TABLE `tb_admin` DISABLE KEYS */;
INSERT INTO `tb_admin` VALUES ('admin','*4ACFE3202A5FF5CF467898FC58AAB1D615029441','배수한','zayou20000@gmail.com','','Y','','2017-04-23 01:43:04','',NULL);
/*!40000 ALTER TABLE `tb_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_code`
--

DROP TABLE IF EXISTS `tb_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_code` (
  `code_group_id` varchar(7) NOT NULL COMMENT '공통코드그룹ID',
  `code_id` varchar(7) NOT NULL COMMENT '코드ID',
  `use_yn` char(1) DEFAULT NULL COMMENT '사용여부',
  `sort_order` int(11) DEFAULT NULL COMMENT '순번',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '비고',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`code_group_id`,`code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='공통코드';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_code`
--

LOCK TABLES `tb_code` WRITE;
/*!40000 ALTER TABLE `tb_code` DISABLE KEYS */;
INSERT INTO `tb_code` VALUES ('C001','C001001','Y',10,'소분류1-1','admin','20170429234850',NULL,NULL),('C001','C001002','Y',10,'소분류1-2','admin','20170429234850',NULL,NULL),('C002','C002001','Y',10,'소분류2-1','admin','20170429234850',NULL,NULL),('C002','C002002','Y',10,'소분류2-2','admin','20170429234850',NULL,NULL),('GRP_CD','C001','Y',10,'대분류1','admin','20170429234850',NULL,NULL),('GRP_CD','C002','Y',20,'대분류2','admin','20170429234850',NULL,NULL),('LANG','cn','Y',30,'중국어','admin','20170429235241',NULL,NULL),('LANG','en','Y',20,'영어','admin','20170429234923',NULL,NULL),('LANG','ko','Y',10,'한국어','admin','20170429234850',NULL,NULL);
/*!40000 ALTER TABLE `tb_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_code_group`
--

DROP TABLE IF EXISTS `tb_code_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_code_group` (
  `code_group_id` varchar(7) NOT NULL COMMENT '공통코드그룹ID',
  `code_group_nm` varchar(200) DEFAULT NULL COMMENT '공통코드그룹명',
  `use_yn` char(1) DEFAULT NULL COMMENT '사용여부',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '비고',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`code_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='공통코드그룹';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_code_group`
--

LOCK TABLES `tb_code_group` WRITE;
/*!40000 ALTER TABLE `tb_code_group` DISABLE KEYS */;
INSERT INTO `tb_code_group` VALUES ('C001','소분류01','Y','분류테스트','admin','20170429234434',NULL,NULL),('C002','소분류02','Y','분류테스트','admin','20170429234434',NULL,NULL),('GRP_CD','대분류','Y','대분류테스트','admin','20170429234434',NULL,NULL),('LANG','언어','Y','언어코드','admin','20170429234434',NULL,NULL);
/*!40000 ALTER TABLE `tb_code_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_code_nm`
--

DROP TABLE IF EXISTS `tb_code_nm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_code_nm` (
  `code_group_id` varchar(7) NOT NULL COMMENT '공통코드그룹ID',
  `code_id` varchar(7) NOT NULL COMMENT '코드ID',
  `lang_cd` char(2) NOT NULL COMMENT '사용언어',
  `code_nm` varchar(200) DEFAULT NULL COMMENT '코드명',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`code_group_id`,`code_id`,`lang_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='공통코드다국어명';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_code_nm`
--

LOCK TABLES `tb_code_nm` WRITE;
/*!40000 ALTER TABLE `tb_code_nm` DISABLE KEYS */;
INSERT INTO `tb_code_nm` VALUES ('C001','C001001','en','소분류1-1en','admin','20170429235036',NULL,NULL),('C001','C001001','ko','소분류1-1','admin','20170429235036',NULL,NULL),('C001','C001002','en','소분류1-2en','admin','20170429235036',NULL,NULL),('C001','C001002','ko','소분류1-2','admin','20170429235036',NULL,NULL),('C002','C002001','en','소분류2-1en','admin','20170429235036',NULL,NULL),('C002','C002001','ko','소분류2-1','admin','20170429235036',NULL,NULL),('C002','C002002','en','소분류2-2en','admin','20170429235036',NULL,NULL),('C002','C002002','ko','소분류2-2','admin','20170429235036',NULL,NULL),('GRP_CD','C001','en','대분류1en','admin','20170429235036',NULL,NULL),('GRP_CD','C001','ko','대분류1','admin','20170429235036',NULL,NULL),('GRP_CD','C002','en','대분류2en','admin','20170429235036',NULL,NULL),('GRP_CD','C002','ko','대분류2','admin','20170429235036',NULL,NULL),('LANG','cn','en','중국어en','admin','20170429235036',NULL,NULL),('LANG','cn','ko','중국어','admin','20170429235228',NULL,NULL),('LANG','en','en','영어en','admin','20170429235036',NULL,NULL),('LANG','en','ko','영어','admin','20170429235049',NULL,NULL),('LANG','ko','en','한국어en','admin','20170429235036',NULL,NULL),('LANG','ko','ko','한국어','admin','20170429235036',NULL,NULL);
/*!40000 ALTER TABLE `tb_code_nm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_comm_code`
--

DROP TABLE IF EXISTS `tb_comm_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_comm_code` (
  `GROUP_CODE` varchar(20) NOT NULL COMMENT '코드그룹코드',
  `CODE` varchar(20) NOT NULL COMMENT '코드',
  `CODE_NAME` varchar(50) DEFAULT NULL COMMENT '코드명',
  `CODE_DESC` varchar(3000) DEFAULT NULL COMMENT '코드설명',
  `ETC_1` varchar(200) DEFAULT NULL COMMENT '기타',
  `ETC_2` varchar(200) DEFAULT NULL COMMENT '기타',
  `PRINT_ORDER` int(11) DEFAULT NULL COMMENT '출력순서',
  `USE_YN` char(1) DEFAULT NULL COMMENT '사용여부 Y/N',
  PRIMARY KEY (`GROUP_CODE`,`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='공통코드';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_comm_code`
--

LOCK TABLES `tb_comm_code` WRITE;
/*!40000 ALTER TABLE `tb_comm_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_comm_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `USER_ID` varchar(20) NOT NULL COMMENT '사용자ID',
  `UP_USER_ID` varchar(20) DEFAULT NULL COMMENT '추천인사용자ID',
  `USER_NM` varchar(20) DEFAULT NULL COMMENT '사용자명',
  `PWD` varchar(100) DEFAULT NULL COMMENT '비밀번호',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '이메일',
  `MOBILE` varchar(15) DEFAULT NULL COMMENT '핸드폰번호',
  `USE_YN` varchar(1) DEFAULT 'Y' COMMENT '사용여부',
  `ZIP_CODE` varchar(6) DEFAULT NULL COMMENT '우편번호',
  `ADDRESS1` varchar(200) DEFAULT NULL COMMENT '주소1',
  `ADDRESS2` varchar(200) DEFAULT NULL COMMENT '주소2',
  `CREATE_DT` varchar(14) DEFAULT NULL,
  `DELETE_DT` varchar(14) DEFAULT NULL,
  `UPDATE_DT` varchar(14) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='사용자';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES ('admin',NULL,'어드민','*4ACFE3202A5FF5CF467898FC58AAB1D615029441','','','Y',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-30 13:24:56
