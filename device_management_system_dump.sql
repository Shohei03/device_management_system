-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: device_management_system
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` int NOT NULL AUTO_INCREMENT,
  `adminFlag` int NOT NULL,
  `code` varchar(255) NOT NULL,
  `createdAt` datetime(6) NOT NULL,
  `deleteFlag` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(64) NOT NULL,
  `updatedAt` datetime(6) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3um79qgwg340lpaw7phtwudtc` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,2,'A001','2022-11-05 08:03:31.069717',0,'的場　一郎','68C881594DA2324C61614A912667AA137FE2CE846522E88D02A3F302A02C77B7','2022-11-05 14:37:54.800649','一般検査室'),(2,2,'A002','2022-11-05 10:26:37.546874',0,'的場　二郎','86F13F24A8C393FC213888D4BCC32EA37C214E0A11A7415FAB53812D11656182','2022-11-05 14:37:48.599877','CT検査室'),(3,2,'A003','2022-11-05 10:30:03.252826',0,'小林　聡','0ECA3F0B189823FBD5E18A810D63DD4F4F2E3134148BC564010288831BE8FD15','2022-11-05 14:37:41.421390','MR検査室'),(4,1,'A004','2022-11-05 10:30:24.518421',0,'藤田　健一','E7199415303CC35F0DBD80644D136AD1F7C077E59D109B7BC41C4C65837D9C47','2022-11-05 14:37:35.939422','乳房検査室'),(5,1,'A005','2022-11-05 10:30:44.414988',0,'山崎　隆','00388598B07BB3828143EB282F078F40A4463C188F68DCD3E203E670848C98EF','2022-11-05 14:37:28.798828','一般検査室'),(6,1,'A006','2022-11-05 10:31:03.707277',0,'山口　浩司','C65C22EB8F5F8C41E9442097DD4E376B3AE3F6F52C675588BD624113966174E5','2022-11-05 14:37:19.416343','MR検査室'),(7,1,'A007','2022-11-05 10:31:34.131512',0,'岩田　直樹','8EC190058188D7C3B48A9CD0AA8BB8D52A564808952839DF0F552831AF378896','2022-11-05 14:37:13.705489','TV検査室'),(8,0,'A008','2022-11-05 10:31:54.996853',0,'村上　博','F9398E4907DAC5C86F32F500204A5564A7F252AB6B212825E62B3232D6CA47FA','2022-11-05 14:37:02.442933','CT検査室'),(9,0,'A009','2022-11-05 10:32:14.075749',0,'中川　雄一','C38288E271FEEBF0CFB8F7AA6B3AE1C0D651B92ED9E1C7BCCCDFBFA3CE05832B','2022-11-05 14:36:56.635257','乳房検査室'),(10,0,'A010','2022-11-05 10:32:36.821111',0,'佐藤　尚','59ECE0C4F696E53B80A9F68CFC2F3D2D06BE7C76A01BCB0B3A499FEC5E6C2C28','2022-11-05 14:36:50.035498','一般検査室'),(11,0,'A011','2022-11-05 10:32:58.701978',0,'木村　良平','07FD58F3E840A7D0D9BD81F4906AFB82EFEDBDC6D172D55AD002C1C27224DA38','2022-11-05 14:36:39.512748','MR検査室'),(12,0,'A012','2022-11-05 10:33:20.180605',0,'村田　宏','6F82B847803F42DCDFCF5AB8D49D2D2C6D03D466D34DCCCD7D5E4C6FA60BE8FD','2022-11-05 14:36:31.845593','MR検査室'),(13,0,'A013','2022-11-05 10:33:42.392508',0,'中野　正明','4A76397C384897BC3ABEBD8527CE64D5B88EC6730DF98FA1039B40543836B39F','2022-11-05 11:43:41.895790','TV'),(14,0,'A014','2022-11-05 13:51:03.182249',0,'中下　智明','626927E56D07BDDC98A939BEC8BAE88CBC94B4EDB5628E39BFC0FFC5CFB24161','2022-11-28 06:53:32.666189','乳房検査室'),(15,0,'A015','2022-11-28 06:55:35.135099',0,'木村　浩二','3654E48E79E5444C5A21C23BEC816A009C7DF928590F627D12509AFD255A89A0','2022-11-28 06:55:35.135099','MR検査室');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `examinations`
--

DROP TABLE IF EXISTS `examinations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `examinations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `examinationItem` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gfh1axa8pspve333q46bnt7u4` (`examinationItem`),
  UNIQUE KEY `UK_p0p8qcol3ixaowg7ksmk75304` (`examinationItem`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `examinations`
--

LOCK TABLES `examinations` WRITE;
/*!40000 ALTER TABLE `examinations` DISABLE KEYS */;
INSERT INTO `examinations` VALUES (2,'CT検査'),(5,'MR検査'),(3,'X線TV検査'),(4,'乳腺X線検査'),(1,'単純X線検査');
/*!40000 ALTER TABLE `examinations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jmdns`
--

DROP TABLE IF EXISTS `jmdns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jmdns` (
  `id` int NOT NULL AUTO_INCREMENT,
  `jmdnCode` varchar(255) NOT NULL,
  `generalName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_oit24k8q8cr4j0dyow8fvsnp` (`jmdnCode`),
  UNIQUE KEY `UK_77q9gt3p2u3i5jg76r3r1i17w` (`jmdnCode`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jmdns`
--

LOCK TABLES `jmdns` WRITE;
/*!40000 ALTER TABLE `jmdns` DISABLE KEYS */;
INSERT INTO `jmdns` VALUES (19,'12913000','植込み型心臓ペースメーカ'),(20,'35643000','人工内耳'),(21,'37307000','振せん用脳電気刺激装置'),(22,'36035004','冠動脈ステント'),(23,'36315000','全人工股関節'),(24,'35666000','人工股関節大腿骨コンポーネント'),(25,'70421010','脳動脈瘤手術用クリップ'),(26,'35911104','長期的使用注入用植込みポート'),(27,'70488000','大動脈用ステントグラフト'),(28,'44611003','グルコースモニタシステム');
/*!40000 ALTER TABLE `jmdns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packageinserts`
--

DROP TABLE IF EXISTS `packageinserts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packageinserts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `acceptabilityOfCtExam` varchar(255) DEFAULT NULL,
  `acceptabilityOfMrExam` varchar(255) DEFAULT NULL,
  `acceptabilityOfManmaExam` varchar(255) DEFAULT NULL,
  `acceptabilityOfTvExam` varchar(255) DEFAULT NULL,
  `acceptabilityOfXrayExam` varchar(255) DEFAULT NULL,
  `approvalNumber` varchar(255) NOT NULL,
  `createdAt` date NOT NULL,
  `deviceName` varchar(255) NOT NULL,
  `JmdnId` int NOT NULL,
  `updatedAt` date DEFAULT NULL,
  `deleteFlag` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp7y6wdk2jkqgltpsitxvbtrip` (`JmdnId`),
  CONSTRAINT `FKmqp0obrwn4d0846op474hsr64` FOREIGN KEY (`JmdnId`) REFERENCES `jmdns` (`id`),
  CONSTRAINT `FKp7y6wdk2jkqgltpsitxvbtrip` FOREIGN KEY (`JmdnId`) REFERENCES `jmdns` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packageinserts`
--

LOCK TABLES `packageinserts` WRITE;
/*!40000 ALTER TABLE `packageinserts` DISABLE KEYS */;
INSERT INTO `packageinserts` VALUES (29,'条件付き可能','条件付き可能','不可能','条件付き可能','可能','228BZX003300','2022-11-27','ペースメーカA',19,'2022-11-27',0),(30,'条件付き可能','条件付き可能','不可能','条件付き可能','可能','229BZX003070','2022-11-27','ペースメーカB',19,'2022-11-27',0),(31,'条件付き可能','条件付き可能','不可能','条件付き可能','可能','301BZX000020','2022-11-27','ペースメーカC',19,'2022-11-27',0),(32,'条件付き可能','不可能','不可能','条件付き可能','可能','215BZY002270','2022-11-27','ペースメーカD',19,'2022-11-27',0),(33,'条件付き可能','不可能','不可能','条件付き可能','可能','215BZY002260','2022-11-27','ペースメーカE',19,'2022-11-27',0),(34,'可能','条件付き可能','可能','可能','可能','211BZY004640','2022-11-27','人工内耳A',20,'2022-11-27',0),(35,'可能','不可能','可能','可能','可能','211BZY006820','2022-11-27','人工内耳B',20,'2022-11-27',0),(36,'可能','不可能','可能','可能','可能','203BZY000600','2022-11-27','人工内耳C',20,'2022-11-27',0),(37,'条件付き可能','不可能','条件付き可能','条件付き可能','条件付き可能','229BZX002340','2022-11-27','人工内耳D',20,'2022-11-27',0),(38,'可能','不可能','可能','可能','可能','229BZX001990','2022-11-27','DBS装置A',21,'2022-11-27',0),(39,'可能','不可能','可能','可能','可能','225BZX004510','2022-11-27','DBS装置B',21,'2022-11-27',0),(40,'可能','条件付き可能','可能','可能','可能','229BZX001660','2022-11-27','DBS装置C',21,'2022-11-27',0),(41,'可能','条件付き可能','可能','可能','可能','223BZX00141A','2022-11-27','冠動脈ステントA',22,'2022-11-27',0),(42,'可能','条件付き可能','可能','可能','可能','302BZX002130','2022-11-27','冠動脈ステントB',22,'2022-11-27',0),(43,'可能','条件付き可能','可能','可能','可能','217BZY000360','2022-11-27','冠動脈ステントC',22,'2022-11-27',0),(44,'可能','条件付き可能','可能','可能','可能','222BZX000770','2022-11-27','冠動脈ステントD',22,'2022-11-27',0),(45,'可能','条件付き可能','可能','可能','可能','224BZX002860','2022-11-27','人工股関節A',23,'2022-11-27',0),(46,'可能','条件付き可能','可能','可能','可能','204BZZ004420','2022-11-27','人工股関節B',24,'2022-11-27',0),(47,'可能','条件付き可能','可能','可能','可能','219BZX011700','2022-11-27','脳動脈クリップA',25,'2022-11-27',0),(48,'可能','条件付き可能','可能','可能','可能','220BZX006980','2022-11-27','脳動脈クリップB',25,'2022-11-27',0),(49,'可能','条件付き可能','不可能','可能','可能','226BZX005140','2022-11-27','ポートA',26,'2022-11-27',0),(50,'可能','条件付き可能','不可能','可能','可能','302BZX002140','2022-11-27','ポートB',26,'2022-11-27',0),(51,'可能','条件付き可能','可能','可能','可能','225BZX004270','2022-11-27','大動脈ステントA',27,'2022-11-27',0),(52,'可能','条件付き可能','可能','可能','可能','226BZX000330','2022-11-27','大動脈ステントB',27,'2022-11-27',0),(53,'可能','条件付き可能','可能','可能','可能','303BZI000160','2022-11-27','大動脈ステントC',27,'2022-11-27',0),(54,'可能','不可能','可能','可能','可能','228BZX002120','2022-11-27','リブレ',28,'2022-11-27',0),(56,'条件付き可能','条件付き可能','不可能','条件付き可能','可能','301BZX000030','2022-11-28','ペースメーカF',19,'2022-11-28',1),(59,'条件付き可能','条件付き可能','不可能','条件付き可能','可能','301BZX000030','2022-11-28','ペースメーカF',19,'2022-11-28',0);
/*!40000 ALTER TABLE `packageinserts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patientdevices`
--

DROP TABLE IF EXISTS `patientdevices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patientdevices` (
  `id` int NOT NULL AUTO_INCREMENT,
  `createdAt` date DEFAULT NULL,
  `deleteFlag` int DEFAULT NULL,
  `implantedAt` date DEFAULT NULL,
  `updatedAt` date DEFAULT NULL,
  `approvalNumber` int NOT NULL,
  `patientId` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfpo6soyo7g9jekfcnmrayinn0` (`approvalNumber`),
  KEY `FK6djchf7e2r765q8ahcae8g8v2` (`patientId`),
  CONSTRAINT `FK3r5e7e9oau77ahyyqdh6mr95l` FOREIGN KEY (`approvalNumber`) REFERENCES `packageinserts` (`id`),
  CONSTRAINT `FK6djchf7e2r765q8ahcae8g8v2` FOREIGN KEY (`patientId`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKfpo6soyo7g9jekfcnmrayinn0` FOREIGN KEY (`approvalNumber`) REFERENCES `packageinserts` (`id`),
  CONSTRAINT `FKtfmaal4kn7l9tmcl6x74odvyl` FOREIGN KEY (`patientId`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patientdevices`
--

LOCK TABLES `patientdevices` WRITE;
/*!40000 ALTER TABLE `patientdevices` DISABLE KEYS */;
INSERT INTO `patientdevices` VALUES (111,'2022-11-27',0,'2020-11-02','2022-11-27',29,1),(112,'2022-11-27',0,'2015-04-15','2022-11-27',35,2),(113,'2022-11-27',0,'2020-03-05','2022-11-27',32,3),(114,'2022-11-27',0,'2012-03-10','2022-11-27',34,4),(115,'2022-11-27',0,'2018-03-12','2022-11-27',36,5),(116,'2022-11-27',0,'2015-05-14','2022-11-27',47,1),(117,'2022-11-27',0,'2020-05-12','2022-11-27',38,6),(118,'2022-11-27',0,'2018-05-23','2022-11-27',40,7),(119,'2022-11-27',0,'2020-02-12','2022-11-27',41,8),(120,'2022-11-27',0,'2020-05-22','2022-11-27',43,9),(121,'2022-11-27',0,'2019-01-14','2022-11-27',44,10),(122,'2022-11-27',0,'2011-05-14','2022-11-27',47,11),(123,'2022-11-27',0,'2013-10-11','2022-11-27',48,12),(124,'2022-11-27',0,'2020-12-25','2022-11-27',49,13),(125,'2022-11-27',0,'2019-08-12','2022-11-27',50,14),(126,'2022-11-27',0,'2017-06-14','2022-11-27',39,15),(127,'2022-11-27',0,'2022-05-10','2022-11-27',42,16),(128,'2022-11-27',0,'2019-05-14','2022-11-27',47,29),(129,'2022-11-27',0,'2014-06-20','2022-11-27',37,30),(130,'2022-11-27',0,'2010-05-14','2022-11-27',33,31),(131,'2022-11-27',0,'2013-08-22','2022-11-27',50,32),(132,'2022-11-27',0,'2011-11-21','2022-11-27',30,33),(133,'2022-11-27',0,'2015-10-11','2022-11-27',31,34),(134,'2022-11-27',0,'2010-03-10','2022-11-27',31,35),(135,'2022-11-27',0,'2012-03-11','2022-11-27',36,36),(136,'2022-11-27',0,'2015-08-11','2022-11-27',45,47),(137,'2022-11-27',0,'2000-01-01','2022-11-27',46,48),(138,'2022-11-27',0,'2001-02-27','2022-11-27',49,49),(139,'2022-11-27',0,'1995-03-28','2022-11-27',33,50),(140,'2022-11-27',0,'1998-03-28','2022-11-27',42,51),(141,'2022-11-27',0,'2017-05-12','2022-11-27',38,52),(142,'2022-11-27',0,'2011-05-14','2022-11-27',47,53),(143,'2022-11-27',0,'2011-01-14','2022-11-27',44,54),(144,'2022-11-27',0,'2020-08-05','2022-11-27',32,55),(145,'2022-11-27',0,'2012-03-16','2022-11-27',34,37),(146,'2022-11-27',0,'1990-08-12','2022-11-27',50,38),(147,'2022-11-27',0,'1998-05-28','2022-11-27',33,39),(148,'2022-11-27',0,'2010-01-01','2022-11-27',46,40),(149,'2022-11-27',0,'1975-02-27','2022-11-27',49,41),(150,'2022-11-28',1,'2010-01-14','2022-11-28',44,42),(151,'2022-11-28',0,'2010-01-14','2022-11-28',44,42),(152,'2022-11-28',0,'2008-03-10','2022-11-28',31,43);
/*!40000 ALTER TABLE `patientdevices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patientexaminations`
--

DROP TABLE IF EXISTS `patientexaminations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patientexaminations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `createdAt` date DEFAULT NULL,
  `examinationDate` date NOT NULL,
  `reservationTime` time DEFAULT NULL,
  `examinationId` int NOT NULL,
  `patientId` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm30iub3j0oav31fn9hmwroapf` (`examinationId`),
  KEY `FKsl1mvytteqodgv7q3vbv21x9r` (`patientId`),
  CONSTRAINT `FKkk5ohw26aiadmcbaraa32x62y` FOREIGN KEY (`patientId`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKl1mm8kj7q9345t23njj1cgxna` FOREIGN KEY (`examinationId`) REFERENCES `examinations` (`id`),
  CONSTRAINT `FKm30iub3j0oav31fn9hmwroapf` FOREIGN KEY (`examinationId`) REFERENCES `examinations` (`id`),
  CONSTRAINT `FKsl1mvytteqodgv7q3vbv21x9r` FOREIGN KEY (`patientId`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patientexaminations`
--

LOCK TABLES `patientexaminations` WRITE;
/*!40000 ALTER TABLE `patientexaminations` DISABLE KEYS */;
INSERT INTO `patientexaminations` VALUES (4,'2022-11-18','2022-11-01','09:00:00',1,1),(6,'2022-11-18','2022-11-10','09:00:00',2,3),(7,'2022-11-18','2022-11-01','10:00:00',5,17),(8,'2022-11-18','2022-11-01','09:00:00',2,4),(9,'2022-11-18','2022-11-01','10:00:00',2,5),(10,'2022-11-18','2022-11-01','11:00:00',2,1),(11,'2022-11-18','2022-11-01','14:00:00',3,7),(12,'2022-11-18','2022-11-01','14:00:00',3,7),(13,'2022-11-18','2022-11-01','10:00:00',2,5),(14,'2022-11-18','2022-11-01','11:00:00',2,1),(17,'2022-11-18','2022-11-01','10:00:00',2,5),(18,'2022-11-18','2022-11-01','10:00:00',1,5),(19,'2022-11-21','2022-11-01','13:00:00',2,6),(20,'2022-11-21','2022-11-01','15:00:00',3,8),(21,'2022-11-21','2022-11-01','16:00:00',3,9),(22,'2022-11-21','2022-11-01','09:00:00',4,10),(23,'2022-11-21','2022-11-01','10:00:00',4,18),(24,'2022-11-21','2022-11-01','11:00:00',4,19),(25,'2022-11-21','2022-11-01','11:00:00',5,11),(26,'2022-11-21','2022-11-01','12:00:00',5,12),(27,'2022-11-21','2022-11-01','13:00:00',5,20),(28,'2022-11-21','2022-11-02','10:00:00',1,21),(29,'2022-11-21','2022-11-02','10:00:00',1,22),(30,'2022-11-21','2022-11-02','10:00:00',1,23),(31,'2022-11-21','2022-11-02','10:00:00',2,24),(32,'2022-11-21','2022-11-02','11:00:00',2,25),(33,'2022-11-21','2022-11-02','12:00:00',2,26),(34,'2022-11-21','2022-11-02','13:00:00',2,27),(35,'2022-11-21','2022-11-02','14:00:00',3,28),(36,'2022-11-21','2022-11-02','14:00:00',3,13),(37,'2022-11-21','2022-11-02','15:00:00',3,14),(38,'2022-11-21','2022-11-02','12:00:00',4,15),(39,'2022-11-21','2022-11-02','12:00:00',4,16),(40,'2022-11-21','2022-11-02','13:00:00',4,29),(41,'2022-11-21','2022-11-02','11:00:00',5,30),(42,'2022-11-21','2022-11-02','12:00:00',5,31),(43,'2022-11-21','2022-11-02','13:00:00',5,32),(44,'2022-11-21','2022-11-04','10:00:00',5,33),(45,'2022-11-21','2022-11-04','13:00:00',5,34),(46,'2022-11-21','2022-11-04','14:00:00',5,35),(47,'2022-11-21','2022-11-04','12:00:00',4,36),(48,'2022-11-21','2022-11-04','11:00:00',4,37),(49,'2022-11-21','2022-11-04','13:00:00',4,38),(50,'2022-11-21','2022-11-04','10:00:00',3,39),(51,'2022-11-21','2022-11-04','09:00:00',3,40),(52,'2022-11-21','2022-11-04','10:00:00',3,41),(53,'2022-11-21','2022-11-04','10:00:00',2,42),(54,'2022-11-21','2022-11-04','11:00:00',2,43),(55,'2022-11-21','2022-11-04','12:00:00',2,44),(56,'2022-11-21','2022-11-04','13:00:00',2,45),(57,'2022-11-21','2022-11-04','16:00:00',1,46),(58,'2022-11-21','2022-11-04','17:00:00',1,47),(59,'2022-11-21','2022-11-04','16:00:00',1,48),(60,'2022-11-21','2022-11-06','11:00:00',5,49),(61,'2022-11-21','2022-11-06','10:00:00',5,50),(62,'2022-11-21','2022-11-06','11:00:00',5,51),(63,'2022-11-21','2022-11-06','12:00:00',5,52),(64,'2022-11-21','2022-11-06','10:00:00',2,53),(65,'2022-11-21','2022-11-06','11:00:00',4,54),(66,'2022-11-21','2022-11-06','11:00:00',4,55),(72,'2022-11-25','2022-11-01','11:00:00',5,46),(74,'2022-11-28','2022-11-01','09:00:00',5,56),(76,'2022-11-28','2022-11-01','11:00:00',5,56),(77,'2022-11-28','2022-11-01','11:00:00',4,57),(78,'2022-11-28','2022-11-01','12:00:00',2,58);
/*!40000 ALTER TABLE `patientexaminations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patientId` int NOT NULL,
  `patientName` varchar(255) NOT NULL,
  `patientNameKana` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_427e3ubwhw8n7a4id3mmrmjgj` (`patientId`),
  UNIQUE KEY `UK_tad3ptljsjoyanc3y56qdchf6` (`patientId`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,24619380,'松井　彩香','まつい　あやか'),(2,85096247,'新井　麻衣','あらい　まい'),(3,15683097,'森本　達也','もりもと　たつや'),(4,93801765,'吉岡　真由美','よしおか　まゆみ'),(5,60954873,'藤原　哲也','ふじわら　てつや'),(6,17504289,'坂本　明','さかもと　あきら'),(7,98023541,'村田　美咲','むらた　みさき'),(8,94675012,'森田　健太','もりた　けんた'),(9,4371259,'村田　竜也','むらた　りゅうや'),(10,17398462,'高橋　誠','たかはし　まこと'),(11,58461932,'山本　誠','やまもと　まこと'),(12,15384960,'田中　健一','たなか　けんいち'),(13,36257081,'井上　太郎','いのうえ　たろう'),(14,35876492,'松本　秀樹','まつもと　ひでき'),(15,93516472,'高橋　和弘','たかはし　かずひろ'),(16,61057984,'竹内　聡','たけうち　さとし'),(17,91478526,'井上　ゆき','いのうえ　ゆき'),(18,27601584,'後藤　聡子','ごとう　さとこ'),(19,46518720,'遠藤　桂子','えんどう　けいこ'),(20,89061347,'佐藤　邦博','さとう　くにひろ'),(21,78423059,'阿部　貴行','あべ　たかゆき'),(22,71248359,'内田　博幸','うちだ　ひろゆき'),(23,59132867,'米田　裕一','よねだ　ゆういち'),(24,80624539,'古川　将太','ふるかわ　しょうた'),(25,91380567,'宮里　達也','みやざと　たつや'),(26,71320654,'岩田　二郎','いわた　じろう'),(27,12870956,'齋藤　未来','さいとう　みく'),(28,71065928,'坂井　美由紀','さかい　みゆき'),(29,32584167,'阿部　茂','あべ　しげる'),(30,36928517,'中西　大介','なかにし　だいすけ'),(31,23169874,'松田　慎一','まつだ　しんいち'),(32,46752083,'久保田　真也','くぼた　しんや'),(33,68527340,'藤村　豊','ふじむら　ゆたか'),(34,57409281,'平井　恵理','ひらい　えり'),(35,94257680,'吉田　ちはる','よしだ　ちはる'),(36,50739481,'岡田　ゆり','おかだ　ゆり'),(37,83542019,'相澤　達哉','あいざわ　たつや'),(38,13209847,'田島　久','たじま　ひさし'),(39,53926701,'村上　辰雄','むらかみ　たつお'),(40,3572986,'宮本　知宏','みやもと　ともひろ'),(41,54012638,'西野　隆一','にしの　りゅういち'),(42,50671489,'小山　光太郎','こやま　こうたろう'),(43,16504729,'竹内　政和','たけうち　まさかず'),(44,25708413,'井上　晴雄','いのうえ　はるお'),(45,38072491,'藤田　真美','ふじた　まみ'),(46,47613902,'西山　久美子','にしやま　くみこ'),(47,24501367,'柴田　沙紀','しばた　さき'),(48,43510986,'川口　真澄','かわぐち　ますみ'),(49,31846092,'岩田　友里','いわた　ゆり'),(50,52618930,'川嶋　舞','かわしま　まい'),(51,34801927,'山本　真理子','やまもと　まりこ'),(52,26958704,'渡辺　明子','わたなべ　あきこ'),(53,87360512,'内田　香織','うちだ　かおり'),(54,68251904,'佐藤里佳','さとう　りか'),(55,49857163,'木村　祥子','きむら　さちこ'),(56,78423050,'高藤　一','たかとう　はじめ'),(57,43510980,'北口　真澄','きたぐち　ますみ'),(58,68527330,'木村　豊','きむら　ゆたか');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-28 13:55:50
