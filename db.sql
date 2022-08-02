-- MariaDB dump 10.19  Distrib 10.8.3-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: tiwdb
-- ------------------------------------------------------
-- Server version	10.8.3-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `tiwdb`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tiwdb` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;

USE `tiwdb`;

--
-- Table structure for table `opzioni`
--

DROP TABLE IF EXISTS `opzioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `opzioni` (
  `codice` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `tipo` enum('in offerta','normale') NOT NULL,
  PRIMARY KEY (`codice`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opzioni`
--

LOCK TABLES `opzioni` WRITE;
/*!40000 ALTER TABLE `opzioni` DISABLE KEYS */;
INSERT INTO `opzioni` VALUES
(1,'colore','in offerta'),
(2,'scritta','normale');
/*!40000 ALTER TABLE `opzioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preventivi`
--

DROP TABLE IF EXISTS `preventivi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preventivi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codice_prodotto` int(11) NOT NULL,
  `id_utente` int(11) NOT NULL,
  `id_impiegato` int(11) DEFAULT NULL,
  `prezzo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_preventivi_1_idx` (`codice_prodotto`),
  KEY `fk_preventivi_2_idx` (`id_utente`),
  KEY `fk_preventivi_3_idx` (`id_impiegato`),
  CONSTRAINT `fk_preventivi_1` FOREIGN KEY (`codice_prodotto`) REFERENCES `prodotti` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_preventivi_2` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_preventivi_3` FOREIGN KEY (`id_impiegato`) REFERENCES `utenti` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preventivi`
--

LOCK TABLES `preventivi` WRITE;
/*!40000 ALTER TABLE `preventivi` DISABLE KEYS */;
/*!40000 ALTER TABLE `preventivi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preventivi_opzioni`
--

DROP TABLE IF EXISTS `preventivi_opzioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preventivi_opzioni` (
  `id_preventivo` int(11) NOT NULL,
  `codice_opzione` int(11) NOT NULL,
  PRIMARY KEY (`id_preventivo`,`codice_opzione`),
  KEY `fk_preventivi_opzioni_2_idx` (`codice_opzione`),
  CONSTRAINT `fk_preventivi_opzioni_1` FOREIGN KEY (`id_preventivo`) REFERENCES `preventivi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_preventivi_opzioni_2` FOREIGN KEY (`codice_opzione`) REFERENCES `opzioni` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preventivi_opzioni`
--

LOCK TABLES `preventivi_opzioni` WRITE;
/*!40000 ALTER TABLE `preventivi_opzioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `preventivi_opzioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotti`
--

DROP TABLE IF EXISTS `prodotti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodotti` (
  `codice` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `immagine` varchar(45) NOT NULL,
  PRIMARY KEY (`codice`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotti`
--

LOCK TABLES `prodotti` WRITE;
/*!40000 ALTER TABLE `prodotti` DISABLE KEYS */;
INSERT INTO `prodotti` VALUES
(1,'maglietta','img'),
(2,'pantaloni','img'),
(3,'camicia','img'),
(4,'felpa','img'),
(5,'jeans','img');
/*!40000 ALTER TABLE `prodotti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotti_opzioni`
--

DROP TABLE IF EXISTS `prodotti_opzioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodotti_opzioni` (
  `codice_prodotto` int(11) NOT NULL,
  `codice_opzione` int(11) NOT NULL,
  PRIMARY KEY (`codice_prodotto`,`codice_opzione`),
  KEY `fk_prodotti_opzioni_2_idx` (`codice_opzione`),
  CONSTRAINT `fk_prodotti_opzioni_1` FOREIGN KEY (`codice_prodotto`) REFERENCES `prodotti` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_prodotti_opzioni_2` FOREIGN KEY (`codice_opzione`) REFERENCES `opzioni` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotti_opzioni`
--

LOCK TABLES `prodotti_opzioni` WRITE;
/*!40000 ALTER TABLE `prodotti_opzioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `prodotti_opzioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utenti` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `password` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `impiegato` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES
(8,'mario','passmario','Mario','Rossi',1),
(9,'marco','passmarco','Marco','Bianchi',0),
(10,'jonathan','passjonathan','Jonathan','Sciarrabba',1),
(11,'paolo','passpaolo','Paolo','Verdi',0);
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-02 17:12:40
