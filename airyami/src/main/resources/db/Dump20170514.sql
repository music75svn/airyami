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
  `code_group_id` varchar(10) NOT NULL COMMENT '공통코드그룹ID',
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
INSERT INTO `tb_code` VALUES ('C001','C001001','Y',10,'소분류1-1','admin','20170429234850',NULL,NULL),('C001','C001002','Y',10,'소분류1-2','admin','20170429234850',NULL,NULL),('C002','C002001','Y',10,'소분류2-1','admin','20170429234850',NULL,NULL),('C002','C002002','Y',10,'소분류2-2','admin','20170429234850',NULL,NULL),('GRP_CD','C001','Y',10,'대분류1','admin','20170429234850',NULL,NULL),('GRP_CD','C002','Y',20,'대분류2','admin','20170429234850',NULL,NULL),('LANG','cn','Y',30,'중국어','admin','20170429235241',NULL,NULL),('LANG','en','Y',20,'영어','admin','20170429234923',NULL,NULL),('LANG','ko','Y',10,'한국어','admin','20170429234850',NULL,NULL),('MENU_TYPE','A','Y',10,'사이트 종류','admin','20170429234850',NULL,NULL),('USER_GROUP','A','Y',10,'어드민','admin','20170429234850',NULL,NULL),('USER_GROUP','P','Y',20,'파트너','admin','20170429234850',NULL,NULL),('USER_GROUP','S','Y',30,'셀러','admin','20170429234850',NULL,NULL),('USER_GROUP','V','Y',40,'벤더','admin','20170429234850',NULL,NULL),('USE_YN','N','Y',20,'미사용','admin','20170429234850',NULL,NULL),('USE_YN','Y','Y',10,'사용','admin','20170429234850',NULL,NULL);
/*!40000 ALTER TABLE `tb_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_code_group`
--

DROP TABLE IF EXISTS `tb_code_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_code_group` (
  `code_group_id` varchar(10) NOT NULL COMMENT '공통코드그룹ID',
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
INSERT INTO `tb_code_group` VALUES ('C001','소분류01','Y','분류테스트 1','admin','20170429234434','admin','20170507011412'),('C002','소분류02','Y','분류테스트','admin','20170429234434',NULL,NULL),('GRP_CD','대분류','Y','대분류테스트','admin','20170429234434',NULL,NULL),('LANG','언어','Y','언어코드','admin','20170429234434',NULL,NULL),('MENU_TYPE','메뉴타입','Y','\'A\' 어드민사이트,','admin','20170506234434',NULL,NULL),('test','C001','Y','1231','admin','20170507003106',NULL,NULL),('USER_GROUP','사용자그룹','Y','파트너 : P, 셀러 : S, 벤더 :V, 관리자:A','admin','20170506234434',NULL,NULL),('USE_YN','사용여부','Y','사용여부 Y/N','admin','20170506234434',NULL,NULL);
/*!40000 ALTER TABLE `tb_code_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_code_nm`
--

DROP TABLE IF EXISTS `tb_code_nm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_code_nm` (
  `code_group_id` varchar(10) NOT NULL COMMENT '공통코드그룹ID',
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
INSERT INTO `tb_code_nm` VALUES ('C001','C001001','en','소분류1-1en','admin','20170429235036',NULL,NULL),('C001','C001001','ko','소분류1-1','admin','20170429235036',NULL,NULL),('C001','C001002','en','소분류1-2en','admin','20170429235036',NULL,NULL),('C001','C001002','ko','소분류1-2','admin','20170429235036',NULL,NULL),('C002','C002001','en','소분류2-1en','admin','20170429235036',NULL,NULL),('C002','C002001','ko','소분류2-1','admin','20170429235036',NULL,NULL),('C002','C002002','en','소분류2-2en','admin','20170429235036',NULL,NULL),('C002','C002002','ko','소분류2-2','admin','20170429235036',NULL,NULL),('GRP_CD','C001','en','대분류1en','admin','20170429235036',NULL,NULL),('GRP_CD','C001','ko','대분류1','admin','20170429235036',NULL,NULL),('GRP_CD','C002','en','대분류2en','admin','20170429235036',NULL,NULL),('GRP_CD','C002','ko','대분류2','admin','20170429235036',NULL,NULL),('LANG','cn','en','중국어en','admin','20170429235036',NULL,NULL),('LANG','cn','ko','중국어','admin','20170429235228',NULL,NULL),('LANG','en','en','영어en','admin','20170429235036',NULL,NULL),('LANG','en','ko','영어','admin','20170429235049',NULL,NULL),('LANG','ko','en','한국어en','admin','20170429235036',NULL,NULL),('LANG','ko','ko','한국어','admin','20170429235036',NULL,NULL),('MENU_TYPE','A','en','Admin','admin','20170429235036',NULL,NULL),('MENU_TYPE','A','ko','관리자','admin','20170429235036',NULL,NULL),('USER_GROUP','A','en','Admin','admin','20170429235036',NULL,NULL),('USER_GROUP','A','ko','관리자','admin','20170429235036',NULL,NULL),('USER_GROUP','P','en','Partner','admin','20170429235036',NULL,NULL),('USER_GROUP','P','ko','파트너','admin','20170429235036',NULL,NULL),('USER_GROUP','S','en','Seller','admin','20170429235036',NULL,NULL),('USER_GROUP','S','ko','셀러','admin','20170429235036',NULL,NULL),('USER_GROUP','V','en','Vender','admin','20170429235036',NULL,NULL),('USER_GROUP','V','ko','벤더','admin','20170429235036',NULL,NULL),('USE_YN','N','en','Unused','admin','20170429235036',NULL,NULL),('USE_YN','N','ko','미사용','admin','20170429235036',NULL,NULL),('USE_YN','Y','en','Use','admin','20170429235036',NULL,NULL),('USE_YN','Y','ko','사용','admin','20170429235036',NULL,NULL);
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
-- Table structure for table `tb_company_master`
--

DROP TABLE IF EXISTS `tb_company_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_company_master` (
  `biz_entity_id` char(8) NOT NULL COMMENT '생성규칙 참조',
  `biz_entity_type` char(1) DEFAULT NULL COMMENT 'basic_parameter_inf(기준정보) 의biz_entity_type(사업주체유형) 참조→ P, S, V, L, M',
  `biz_type` char(1) DEFAULT NULL COMMENT '1-개인, 2-법인(회사), 3-업체',
  `biz_license_no` varchar(20) DEFAULT NULL COMMENT 'default값 - NULL',
  `comp_nm` varchar(20) DEFAULT NULL,
  `comp_ceo_id` varchar(20) DEFAULT NULL COMMENT 'user_master의 user_id 참조',
  `addr_country` char(2) DEFAULT NULL,
  `addr_province` varchar(20) DEFAULT NULL,
  `addr_city` varchar(20) DEFAULT NULL,
  `addr_full` varchar(100) DEFAULT NULL,
  `phone_country_no` varchar(3) DEFAULT NULL COMMENT 'basic_parameter_inf(기준정보) 의 country_number(국가번호) 참조',
  `phone_no` varchar(10) DEFAULT NULL,
  `smart_phone_no` varchar(11) DEFAULT NULL,
  `wechat_id` varchar(20) DEFAULT NULL,
  `email_id` varchar(30) DEFAULT NULL,
  `valid_yn` char(1) DEFAULT NULL COMMENT 'Y/N',
  `expired_date` char(6) DEFAULT NULL COMMENT 'yymmdd -> default : 991231',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`biz_entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='회사 마스터';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_company_master`
--

LOCK TABLES `tb_company_master` WRITE;
/*!40000 ALTER TABLE `tb_company_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_company_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_menu`
--

DROP TABLE IF EXISTS `tb_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_menu` (
  `MENU_TYPE` char(1) NOT NULL COMMENT '사용자 : U, 관리자 : A',
  `MENU_CODE` varchar(20) NOT NULL,
  `UPPER_MENU_CODE` varchar(20) DEFAULT NULL,
  `MENU_LEVEL` int(11) DEFAULT NULL,
  `LINK_URL` varchar(200) DEFAULT NULL,
  `LINK_PARAM` varchar(200) DEFAULT NULL,
  `MENU_ORDER` int(11) DEFAULT NULL,
  `USE_YN` char(1) DEFAULT NULL,
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`MENU_TYPE`,`MENU_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='메뉴관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_menu`
--

LOCK TABLES `tb_menu` WRITE;
/*!40000 ALTER TABLE `tb_menu` DISABLE KEYS */;
INSERT INTO `tb_menu` VALUES ('A','1','0',0,NULL,NULL,0,'Y',NULL,NULL,NULL,NULL),('A','1001','1',1,NULL,NULL,100,'Y',NULL,NULL,NULL,NULL),('A','1001001','1001',2,NULL,NULL,100,'Y',NULL,NULL,NULL,NULL),('A','1002','1',1,NULL,NULL,200,'Y',NULL,NULL,NULL,NULL),('A','1002001','1002',2,'/menu/menuView.do',NULL,100,'Y',NULL,NULL,NULL,NULL),('A','1999','1',1,NULL,NULL,999,'Y',NULL,NULL,NULL,NULL),('A','1999001','1999',2,'/template/template.do',NULL,100,'Y',NULL,NULL,NULL,NULL),('A','1999002','1999',2,'/template/template_list.do',NULL,200,'Y',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tb_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_menu_nm`
--

DROP TABLE IF EXISTS `tb_menu_nm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_menu_nm` (
  `MENU_TYPE` char(1) NOT NULL COMMENT '사용자 : U, 관리자 : A',
  `MENU_CODE` varchar(20) NOT NULL,
  `lang_cd` char(2) NOT NULL COMMENT '사용언어',
  `MENU_NAME` varchar(30) DEFAULT NULL COMMENT '코드명',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`MENU_TYPE`,`MENU_CODE`,`lang_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='메뉴다국어명';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_menu_nm`
--

LOCK TABLES `tb_menu_nm` WRITE;
/*!40000 ALTER TABLE `tb_menu_nm` DISABLE KEYS */;
INSERT INTO `tb_menu_nm` VALUES ('A','1001','ko','코드관리',NULL,NULL,NULL,NULL),('A','1001001','ko','코드그룹',NULL,NULL,NULL,NULL),('A','1002','ko','메뉴관리',NULL,NULL,NULL,NULL),('A','1002001','ko','메뉴',NULL,NULL,NULL,NULL),('A','1999','ko','Template',NULL,NULL,NULL,NULL),('A','1999001','ko','Template예제',NULL,NULL,NULL,NULL),('A','1999002','ko','리스트예제2',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tb_menu_nm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_menu_usergroup`
--

DROP TABLE IF EXISTS `tb_menu_usergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_menu_usergroup` (
  `MENU_TYPE` char(1) NOT NULL COMMENT '사용자 : U, 관리자 : A',
  `MENU_CODE` varchar(20) NOT NULL,
  `USER_GROUP` varchar(20) NOT NULL COMMENT '파트너 : P, 셀러 : S, 벤더 :V, 관리자 : A',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`MENU_TYPE`,`MENU_CODE`,`USER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='사용자별 메뉴관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_menu_usergroup`
--

LOCK TABLES `tb_menu_usergroup` WRITE;
/*!40000 ALTER TABLE `tb_menu_usergroup` DISABLE KEYS */;
INSERT INTO `tb_menu_usergroup` VALUES ('A','1','A',NULL,NULL,NULL,NULL),('A','1001','A',NULL,NULL,NULL,NULL),('A','1001001','A',NULL,NULL,NULL,NULL),('A','1002','A',NULL,NULL,NULL,NULL),('A','1002001','A',NULL,NULL,NULL,NULL),('A','1999','A',NULL,NULL,NULL,NULL),('A','1999001','A',NULL,NULL,NULL,NULL),('A','1999002','A',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tb_menu_usergroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_master`
--

DROP TABLE IF EXISTS `tb_user_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_master` (
  `user_id` varchar(20) NOT NULL COMMENT '사용자 로그인 아이디',
  `user_pswd` varchar(20) DEFAULT NULL COMMENT '사용자 로그인 비밀번호',
  `user_type` char(1) DEFAULT NULL COMMENT '사용자 또는 사업주체(Biz_Entity) 유형',
  `user_role` char(3) DEFAULT NULL COMMENT '※ user_role_master 테이블 참조',
  `biz_entity_id` varchar(8) DEFAULT NULL COMMENT 'if user_master.user_type = C → NULLif user_master.user_type = P → 파트너社번호 등록if user_master.user_type = S → 판매상번호 등록if user_master.user_type = V → 공급상번호 등록if user_master.user_type = L  → 물류상번호 등록if user_master.user_type = M → NULL',
  `sex` char(1) DEFAULT NULL,
  `birth_year` char(4) DEFAULT NULL,
  `birth_month` char(2) DEFAULT NULL,
  `birth_day` char(2) DEFAULT NULL,
  `home_addr_country` char(2) DEFAULT NULL,
  `home_addr_province` varchar(20) DEFAULT NULL,
  `home_addr_city` varchar(20) DEFAULT NULL,
  `home_addr_full` varchar(100) DEFAULT NULL,
  `comp_addr_country` char(2) DEFAULT NULL,
  `comp_addr_province` varchar(20) DEFAULT NULL,
  `comp_addr_city` varchar(20) DEFAULT NULL,
  `comp_addr_full` varchar(100) DEFAULT NULL,
  `wechat_id` varchar(20) DEFAULT NULL COMMENT '또는 facebook messenger id',
  `phone_country_no` varchar(3) DEFAULT NULL,
  `phone_no` varchar(11) DEFAULT NULL,
  `email_id` varchar(30) DEFAULT NULL,
  `bank_name` varchar(80) DEFAULT NULL,
  `account_no` varchar(20) DEFAULT NULL,
  `last_order_date` char(6) DEFAULT NULL COMMENT 'yymmdd (마지막 고객 P/O 생성일자) → customer_po_header 신규 레코드 생성時 업데이트',
  `insert_user_id` varchar(20) DEFAULT NULL,
  `insert_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  `update_user_id` varchar(20) DEFAULT NULL,
  `update_dt` char(14) DEFAULT NULL COMMENT 'yyyymmdd24hrmiss',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='사용자 마스터';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_master`
--

LOCK TABLES `tb_user_master` WRITE;
/*!40000 ALTER TABLE `tb_user_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_master` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-14  3:30:58
