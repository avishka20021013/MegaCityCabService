-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 14, 2025 at 04:14 AM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `megacitydb`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
CREATE TABLE IF NOT EXISTS `bookings` (
  `bookingID` int NOT NULL AUTO_INCREMENT,
  `clientID` int NOT NULL,
  `cabID` int NOT NULL,
  `driverStatus` varchar(20) NOT NULL,
  `bookedDates` text NOT NULL,
  `bookingDate` date NOT NULL,
  `billAmount` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bookingID`),
  KEY `clientID` (`clientID`),
  KEY `cabID` (`cabID`)
) ENGINE=MyISAM AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`bookingID`, `clientID`, `cabID`, `driverStatus`, `bookedDates`, `bookingDate`, `billAmount`) VALUES
(25, 18, 52, 'withDriver', '2025-03-21, 2025-03-22', '2025-03-14', 8000.00),
(26, 18, 52, 'withDriver', '2025-03-28, 2025-03-29', '2025-03-14', 8000.00);

-- --------------------------------------------------------

--
-- Table structure for table `cabs`
--

DROP TABLE IF EXISTS `cabs`;
CREATE TABLE IF NOT EXISTS `cabs` (
  `cabID` int NOT NULL AUTO_INCREMENT,
  `cabNumber` varchar(20) NOT NULL,
  `model` varchar(50) NOT NULL,
  `category` varchar(50) NOT NULL,
  `seats` int NOT NULL,
  `ownerName` varchar(100) NOT NULL,
  `ownerContact` varchar(15) NOT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cabID`),
  UNIQUE KEY `cabNumber` (`cabNumber`)
) ENGINE=MyISAM AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cabs`
--

INSERT INTO `cabs` (`cabID`, `cabNumber`, `model`, `category`, `seats`, `ownerName`, `ownerContact`, `imageUrl`) VALUES
(52, 'CAB2021', 'Toyota Etios', 'Economy', 4, 'Silva', '0777777777', 'cab2.jpg'),
(53, 'CAB2020', 'Honda Amaze', 'Standard', 4, 'Jhon', '0771234567', 'cab1.png');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `categoryID` int NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `driverCost` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`categoryID`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`categoryID`, `category`, `price`, `driverCost`) VALUES
(10, 'Economy', 3000.00, 1000.00),
(11, 'Standard', 4500.00, 1200.00),
(12, 'Luxury', 7000.00, 2500.00);

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `clientID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `nic` varchar(20) NOT NULL,
  `email` varchar(191) NOT NULL,
  `username` varchar(191) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`clientID`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`clientID`, `firstName`, `lastName`, `address`, `nic`, `email`, `username`, `password`) VALUES
(18, 'AvishkaEdit', 'Dilshan', '545, galdola, kotapola', '200228701981', 'avishkadilshan20021013@gmail.com', 'avishka', '1234'),
(20, 'Avishka', 'Dilshan', 'magahena', '4545454565', 'aaa@gmail.com', 'qqq', 'aaa');

-- --------------------------------------------------------

--
-- Table structure for table `tax_and_discount`
--

DROP TABLE IF EXISTS `tax_and_discount`;
CREATE TABLE IF NOT EXISTS `tax_and_discount` (
  `id` int NOT NULL,
  `tax_percentage` decimal(5,2) NOT NULL DEFAULT '0.00',
  `discount_percentage` decimal(5,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tax_and_discount`
--

INSERT INTO `tax_and_discount` (`id`, `tax_percentage`, `discount_percentage`) VALUES
(1, 10.00, 10.00);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
