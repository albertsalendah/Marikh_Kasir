-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 27, 2020 at 02:10 AM
-- Server version: 10.3.23-MariaDB
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `u4397697_db_marikhpos`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_add_deskripsi`
--

CREATE TABLE `tb_add_deskripsi` (
  `id_add_deskripsi` int(11) NOT NULL,
  `id_deskripsi` int(11) NOT NULL,
  `id_det_penjualan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tb_deskripsi`
--

CREATE TABLE `tb_deskripsi` (
  `id_deskripsi` int(11) NOT NULL,
  `nama_deskripsi` varchar(50) NOT NULL,
  `id_kat_produk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_deskripsi`
--

INSERT INTO `tb_deskripsi` (`id_deskripsi`, `nama_deskripsi`, `id_kat_produk`) VALUES
(1, 'PB', 1),
(2, 'P', 1),
(3, 'S', 1),
(4, 'M', 1),
(5, 'TP', 1),
(6, 'LI', 2),
(7, 'MI', 2),
(8, 'C', 2),
(9, 'N', 2),
(10, 'H+', 2),
(11, 'H', 2),
(12, 'LS', 2);

-- --------------------------------------------------------

--
-- Table structure for table `tb_discount`
--

CREATE TABLE `tb_discount` (
  `id_disc` int(11) NOT NULL,
  `deskripsi_disc` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nominal_disc` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_discount`
--

INSERT INTO `tb_discount` (`id_disc`, `deskripsi_disc`, `nominal_disc`) VALUES
(1, 'Diskon 10%', 10),
(2, 'POTONGAN RP.1000', 1000),
(3, 'DISKON ONGKIR 4000', 4000),
(4, 'DISKON TOTAL BELANJA 5000', 5000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_kat_kas`
--

CREATE TABLE `tb_kat_kas` (
  `id_kat_kas` int(11) NOT NULL,
  `nama_kas` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_kat_kas`
--

INSERT INTO `tb_kat_kas` (`id_kat_kas`, `nama_kas`) VALUES
(1, 'Kas Operational');

-- --------------------------------------------------------

--
-- Table structure for table `tb_kat_pelanggan`
--

CREATE TABLE `tb_kat_pelanggan` (
  `id_kat_pelanggan` int(11) NOT NULL,
  `nama_kat_pelanggan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_kat_pelanggan`
--

INSERT INTO `tb_kat_pelanggan` (`id_kat_pelanggan`, `nama_kat_pelanggan`) VALUES
(1, 'Guest'),
(2, 'MEMBER'),
(3, 'VIP');

-- --------------------------------------------------------

--
-- Table structure for table `tb_kat_pengeluaran`
--

CREATE TABLE `tb_kat_pengeluaran` (
  `id_kat_pengeluaran` int(11) NOT NULL,
  `nama_kat_pengeluaran` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_kat_pengeluaran`
--

INSERT INTO `tb_kat_pengeluaran` (`id_kat_pengeluaran`, `nama_kat_pengeluaran`) VALUES
(1, 'BELANJA HARIAN'),
(2, 'TENAGA KERJA'),
(3, 'LISTRIK'),
(4, 'LAIN-LAIN');

-- --------------------------------------------------------

--
-- Table structure for table `tb_kat_produk`
--

CREATE TABLE `tb_kat_produk` (
  `id_kat_produk` int(11) NOT NULL,
  `nama_kat_produk` varchar(50) CHARACTER SET latin1 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_kat_produk`
--

INSERT INTO `tb_kat_produk` (`id_kat_produk`, `nama_kat_produk`) VALUES
(1, 'FOODS'),
(2, 'DRINKS'),
(3, 'SNACKS'),
(4, 'OTHERS'),
(5, 'PACKS');

-- --------------------------------------------------------

--
-- Table structure for table `tb_method_bayar`
--

CREATE TABLE `tb_method_bayar` (
  `id_method_bayar` int(11) NOT NULL,
  `nama_method` varchar(50) NOT NULL,
  `reguler` tinyint(1) NOT NULL,
  `olfood` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_method_bayar`
--

INSERT INTO `tb_method_bayar` (`id_method_bayar`, `nama_method`, `reguler`, `olfood`) VALUES
(1, 'CASH', 1, 1),
(2, 'OVO', 1, 1),
(3, 'GOPAY', 1, 1),
(6, 'DANA', 0, 0),
(8, 'LINK AJA', 1, 1),
(10, 'KREDIVO', 0, 0),
(11, 'JENIUS', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tb_nota`
--

CREATE TABLE `tb_nota` (
  `nama_toko` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `alamat_toko` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `footer` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_pelanggan`
--

CREATE TABLE `tb_pelanggan` (
  `id_pelanggan` varchar(7) NOT NULL,
  `nama_pelanggan` varchar(50) DEFAULT NULL,
  `hp_pelanggan` varchar(15) DEFAULT NULL,
  `email_pelanggan` varchar(50) DEFAULT NULL,
  `tgl_lahir_pelanggan` date DEFAULT NULL,
  `id_kat_pelanggan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_pelanggan`
--

INSERT INTO `tb_pelanggan` (`id_pelanggan`, `nama_pelanggan`, `hp_pelanggan`, `email_pelanggan`, `tgl_lahir_pelanggan`, `id_kat_pelanggan`) VALUES
('PL0004', 'UMKM PROJECT', NULL, NULL, NULL, 3),
('PL0005', 'NJUZTIN', NULL, NULL, '1988-09-26', 0),
('PL0006', 'SHINTA', NULL, NULL, NULL, 3),
('PL0007', 'KARDO', NULL, NULL, NULL, 3),
('PL0011', 'ANTO', '085240166767', 'ryantoungke27@gmail.com', '2020-06-26', 2),
('PL0016', 'OCHA', '081', 'ocha2', '2020-06-30', 2),
('PL0018', 'ICAD', '123456', 'icad', '2020-07-31', 2);

-- --------------------------------------------------------

--
-- Table structure for table `tb_produk`
--

CREATE TABLE `tb_produk` (
  `id_produk` int(11) NOT NULL,
  `nama_produk` varchar(50) NOT NULL,
  `harga_produk` int(11) NOT NULL,
  `qty_produk` int(11) NOT NULL,
  `diskon_produk` int(11) DEFAULT NULL,
  `id_kat_produk` int(11) NOT NULL,
  `tgl_awal_diskon` date DEFAULT NULL,
  `tgl_akhir_diskon` date DEFAULT NULL,
  `diskon_olfood` int(11) DEFAULT NULL,
  `tgl_awal_diskon_olfood` date DEFAULT NULL,
  `tgl_akhir_diskon_olfood` date DEFAULT NULL,
  `gbr_produk` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_produk`
--

INSERT INTO `tb_produk` (`id_produk`, `nama_produk`, `harga_produk`, `qty_produk`, `diskon_produk`, `id_kat_produk`, `tgl_awal_diskon`, `tgl_akhir_diskon`, `diskon_olfood`, `tgl_awal_diskon_olfood`, `tgl_akhir_diskon_olfood`, `gbr_produk`) VALUES
(1, 'NASI CAMPUR SPESIAL MARIKH', 19000, 0, NULL, 1, NULL, NULL, 24, '2020-08-18', '2020-08-31', NULL),
(2, 'NASI IKAN CAKALANG', 13000, 0, 10, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(3, 'NASI SAMBAL ROA', 13000, 0, 10, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(4, 'Nasi Dabu Roa Telur Marikh', 9000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(5, 'Nasgor Cakalang', 13000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(6, 'Nasgor Roa', 13000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(7, 'NASI PUTIH', 4000, 0, NULL, 1, NULL, NULL, 30, '2020-08-18', '2020-08-31', NULL),
(8, 'Bakmi Gr. Cakalang', 13000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(9, 'Bakmi K. Cakalang', 13000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(10, 'Bakmi Gr. Roa', 13000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(11, 'Bakmi K. Roa', 13000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(12, 'Indomie  Gr. Cakalang', 9000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(13, 'Indomie K. Cakalang', 9000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(14, 'Indomie Gr. Roa', 9000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(15, 'Indomie K. Roa', 9000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(16, 'Cakalang Kemasan 80gr', 13000, 0, 0, 1, NULL, NULL, 25, '2020-08-18', '2020-08-31', NULL),
(17, 'Cakalang Kemasan 150gr', 28000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(18, 'Roa Kemasan 40gr', 13000, 0, 0, 1, NULL, NULL, 25, '2020-08-18', '2020-08-31', NULL),
(19, 'Roa Kemasan 120gr', 40000, 0, 0, 1, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(20, 'Pisang Goreng Dabu Roa', 11000, 0, 0, 3, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(21, 'Singkong Dabu Roa', 11000, 0, 0, 3, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(22, 'Cappucino', 10000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(23, 'Kopi Latte', 10000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(24, 'Kopi Milo', 10000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(25, 'Kopi Aren', 10000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(26, 'CHOCOLATE', 10000, 0, NULL, 2, NULL, NULL, 25, '2020-08-18', '2020-08-31', NULL),
(27, 'Lemonade', 7000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(28, 'Oreo Boom', 12000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(29, 'Choco boom', 12000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(30, 'Choco Beng', 12000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(31, 'Teh Tarik', 9000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(32, 'Teh Kampul', 7000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(33, 'Milo Keju', 12000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(34, 'TEH', 4000, 0, NULL, 2, NULL, NULL, 30, '2020-08-18', '2020-08-31', NULL),
(35, 'Vanila Oreo', 12000, 0, 0, 2, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(36, 'Paket 1 (2 NP +1 C 40gr+1 CP 40gr)', 25000, 0, 0, 5, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(37, 'Paket 2 (2 NCP + 2 NCPM)', 60000, 0, 0, 5, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(38, 'Paket 3 (1 NCP + 1 OREO BOOM)', 30000, 0, 0, 5, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(39, 'Paket 4 (1 Cappu+1 Choco + 1 Oreo Boom)', 37000, 0, 0, 5, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(40, 'Paket 5 (1 Cappu + 1 Choco)', 18000, 0, 0, 5, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(41, 'Paket 8 (4 NCP + 4 NCPM)', 120000, 0, 0, 5, NULL, NULL, 20, '2020-08-18', '2020-08-31', NULL),
(42, 'KRUPUK NASGOR', 1000, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
(43, 'TAHU GORENG', 9000, 0, NULL, 1, NULL, NULL, 30, '2020-08-18', '2020-08-31', NULL),
(44, 'paket 6 (indomie goreng toping roa cakalang +nasi)', 13000, 0, 0, 1, NULL, NULL, 30, '2020-08-18', '2020-08-31', ''),
(45, 'TELUR', 5000, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tb_report_operational`
--

CREATE TABLE `tb_report_operational` (
  `id_report_all` varchar(16) NOT NULL,
  `tgl_trx_operational` date NOT NULL,
  `keterangan` varchar(100) NOT NULL,
  `kredit_op` int(11) NOT NULL,
  `debit_op` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_report_operational`
--

INSERT INTO `tb_report_operational` (`id_report_all`, `tgl_trx_operational`, `keterangan`, `kredit_op`, `debit_op`) VALUES
('OP20200826005', '2020-08-26', 'Pendapatan Virtual ACC', 0, 69760),
('OP20200826002', '2020-08-26', 'Saldo Awal Harian', 0, 200000),
('OP20200826001', '2020-08-26', 'Transfer Akun dari GOPAY', 0, 0),
('OP20200825004', '2020-08-25', 'Pendapatan Virtual ACC', 149920, 149920),
('OP20200826003', '2020-08-26', 'Transfer Akun dari OVO', 0, 0),
('OP20200825003', '2020-08-25', 'Kas Harian', 66580, 0),
('OP20200825001', '2020-08-25', 'Saldo Awal Harian', 0, 195580),
('OP20200826004', '2020-08-26', 'Kas Harian', 208000, 0),
('OP20200824003', '2020-08-24', 'Pendapatan Virtual ACC', 69920, 69920),
('OP20200824002', '2020-08-24', 'Kas Harian', 265500, 0),
('OP20200824001', '2020-08-24', 'Saldo Awal Harian', 0, 400000),
('OP20200824000', '2020-08-24', 'Kas Operational', 400000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tb_report_saldo_harian`
--

CREATE TABLE `tb_report_saldo_harian` (
  `id_report_sh` int(11) NOT NULL,
  `tgl_report_sh` date NOT NULL,
  `saldo_awal` int(11) DEFAULT NULL,
  `saldo_akhir` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_report_saldo_harian`
--

INSERT INTO `tb_report_saldo_harian` (`id_report_sh`, `tgl_report_sh`, `saldo_awal`, `saldo_akhir`) VALUES
(12, '2020-08-26', 200000, 208000),
(11, '2020-08-25', 195580, 66580),
(10, '2020-08-24', 400000, 265500);

-- --------------------------------------------------------

--
-- Table structure for table `tb_setting`
--

CREATE TABLE `tb_setting` (
  `id` int(11) NOT NULL,
  `modal_awal` int(11) NOT NULL,
  `tax` int(2) NOT NULL,
  `gofoodph` int(2) NOT NULL,
  `gofoodec` int(2) NOT NULL,
  `gofoodkomisi` int(2) NOT NULL,
  `grabfoodph` int(2) NOT NULL,
  `grabfoodec` int(2) NOT NULL,
  `grabfoodkomisi` int(2) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_setting`
--

INSERT INTO `tb_setting` (`id`, `modal_awal`, `tax`, `gofoodph`, `gofoodec`, `gofoodkomisi`, `grabfoodph`, `grabfoodec`, `grabfoodkomisi`) VALUES
(1, 400000, 0, 25, 1000, 20, 30, 1000, 20);

-- --------------------------------------------------------

--
-- Table structure for table `tb_trx_detpenjualan`
--

CREATE TABLE `tb_trx_detpenjualan` (
  `id_trx_detpenjualan` varchar(13) NOT NULL,
  `id_trx_penjualan` varchar(13) NOT NULL,
  `qty_terjual` int(11) NOT NULL,
  `harga_terjual` int(11) NOT NULL,
  `diskon_terjual` int(11) DEFAULT NULL,
  `status_pesanan` enum('dalam proses','selesai','batal') NOT NULL,
  `id_produk` int(11) NOT NULL,
  `jenis_transaksi` enum('Dine In','Takeaway','Gofood','Grabfood') NOT NULL,
  `deskripsi` text NOT NULL,
  `catatan_tambahan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_trx_detpenjualan`
--

INSERT INTO `tb_trx_detpenjualan` (`id_trx_detpenjualan`, `id_trx_penjualan`, `qty_terjual`, `harga_terjual`, `diskon_terjual`, `status_pesanan`, `id_produk`, `jenis_transaksi`, `deskripsi`, `catatan_tambahan`) VALUES
('200824001001', '200824001', 1, 18000, 6000, 'dalam proses', 1, 'Gofood', '', ''),
('200824001002', '200824001', 1, 3375, 1875, 'dalam proses', 34, 'Gofood', '', ''),
('200824002001', '200824002', 1, 18000, 6000, 'dalam proses', 1, 'Gofood', '', ''),
('200824003001', '200824003', 1, 11000, 3000, 'dalam proses', 20, 'Gofood', '', ''),
('200824004001', '200824004', 1, 18000, 6000, 'dalam proses', 1, 'Gofood', '', ''),
('200824005001', '200824005', 1, 13000, 3500, 'dalam proses', 2, 'Gofood', '', ''),
('200824006001', '200824006', 1, 1000, 0, 'dalam proses', 42, 'Dine In', '', ''),
('200825001001', '200825001', 1, 18000, 6000, 'dalam proses', 1, 'Gofood', '', ''),
('200825001002', '200825001', 1, 13000, 3500, 'dalam proses', 2, 'Gofood', '', ''),
('200825002001', '200825002', 2, 12125, 8750, 'dalam proses', 16, 'Gofood', '', ''),
('200825002002', '200825002', 1, 9000, 2500, 'dalam proses', 14, 'Gofood', 'TP', ''),
('200825003001', '200825003', 1, 19000, 0, 'dalam proses', 1, 'Takeaway', '', ''),
('200825003002', '200825003', 1, 13000, 0, 'dalam proses', 6, 'Takeaway', 'P', ''),
('200825004001', '200825004', 1, 18000, 6000, 'dalam proses', 1, 'Gofood', '', ''),
('200825005001', '200825005', 2, 9000, 0, 'dalam proses', 13, 'Dine In', '', ''),
('200825005002', '200825005', 2, 5000, 0, 'dalam proses', 45, 'Dine In', '', ''),
('200825005003', '200825005', 1, 11000, 0, 'dalam proses', 20, 'Dine In', '', ''),
('200825006001', '200825006', 3, 13000, 10500, 'dalam proses', 2, 'Gofood', '', ''),
('200825006002', '200825006', 1, 11000, 3000, 'dalam proses', 20, 'Gofood', '', ''),
('200825007001', '200825007', 1, 10000, 0, 'dalam proses', 25, 'Dine In', '', ''),
('200825007002', '200825007', 1, 10000, 0, 'dalam proses', 23, 'Dine In', '', ''),
('200825007003', '200825007', 2, 9000, 0, 'dalam proses', 13, 'Dine In', '', ''),
('200825008001', '200825008', 1, 30000, 7750, 'dalam proses', 38, 'Gofood', '', ''),
('200825009001', '200825009', 1, 13000, 3500, 'dalam proses', 2, 'Gofood', '', ''),
('200826001001', '200826001', 1, 10000, 0, 'dalam proses', 25, 'Dine In', '', ''),
('200826002001', '200826002', 1, 18000, 6000, 'dalam proses', 1, 'Gofood', '', ''),
('200826002002', '200826002', 1, 7750, 3750, 'dalam proses', 43, 'Gofood', '', ''),
('200826002003', '200826002', 1, 11000, 3000, 'dalam proses', 20, 'Gofood', '', ''),
('200826003001', '200826003', 1, 3375, 1875, 'dalam proses', 34, 'Gofood', '', ''),
('200826004001', '200826004', 1, 30000, 7750, 'dalam proses', 38, 'Gofood', '', ''),
('200826004002', '200826004', 1, 11000, 3000, 'dalam proses', 20, 'Gofood', '', ''),
('200826005001', '200826005', 1, 12000, 0, 'dalam proses', 28, 'Dine In', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tb_trx_pengeluaran`
--

CREATE TABLE `tb_trx_pengeluaran` (
  `id_trx_pengeluaran` int(11) NOT NULL,
  `id_kat_pengeluaran` int(11) NOT NULL,
  `nama_pengeluaran` varchar(100) NOT NULL,
  `tgl_pengeluaran` datetime NOT NULL,
  `nominal_pengeluaran` int(11) NOT NULL,
  `qty_pengeluaran` int(11) NOT NULL,
  `total_pengeluaran` int(11) NOT NULL,
  `jenis_post_pengeluaran` enum('Kas Harian','Kas Operational') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_trx_pengeluaran`
--

INSERT INTO `tb_trx_pengeluaran` (`id_trx_pengeluaran`, `id_kat_pengeluaran`, `nama_pengeluaran`, `tgl_pengeluaran`, `nominal_pengeluaran`, `qty_pengeluaran`, `total_pengeluaran`, `jenis_post_pengeluaran`) VALUES
(1, 1, 'IKAN CAKALANG', '2020-08-24 13:00:46', 55000, 1, 55000, 'Kas Harian'),
(2, 1, 'CABE ', '2020-08-24 13:01:10', 6000, 1, 6000, 'Kas Harian'),
(3, 1, 'LEMON', '2020-08-24 13:01:30', 5000, 1, 5000, 'Kas Harian'),
(4, 1, 'JERUK NIPIS', '2020-08-24 13:01:53', 5000, 1, 5000, 'Kas Harian'),
(5, 1, 'TAHU', '2020-08-24 13:02:17', 8000, 1, 8000, 'Kas Harian'),
(6, 1, 'TOMAT', '2020-08-24 13:02:34', 5000, 1, 5000, 'Kas Harian'),
(7, 1, 'LENGKUAS', '2020-08-24 13:02:49', 2000, 1, 2000, 'Kas Harian'),
(8, 1, 'SERAI', '2020-08-24 13:03:09', 2000, 1, 2000, 'Kas Harian'),
(9, 1, 'TELUR', '2020-08-24 13:03:27', 22500, 1, 22500, 'Kas Harian'),
(10, 1, 'MINYAK GORENG', '2020-08-24 13:03:45', 25000, 1, 25000, 'Kas Harian'),
(11, 1, 'IKAN CAKALANG', '2020-08-25 13:56:36', 38000, 1, 38000, 'Kas Harian'),
(12, 1, 'TOMAT', '2020-08-25 13:57:01', 5000, 1, 5000, 'Kas Harian'),
(13, 1, 'BERAS', '2020-08-25 13:57:35', 12000, 4, 48000, 'Kas Harian'),
(14, 1, 'DAUN SINGKONG', '2020-08-25 13:58:09', 5000, 1, 5000, 'Kas Harian'),
(15, 1, 'PISANG KEPOK', '2020-08-25 13:58:44', 25000, 1, 25000, 'Kas Harian'),
(16, 1, 'TEPUNG PISANG', '2020-08-25 13:59:06', 2500, 2, 5000, 'Kas Harian'),
(17, 1, 'CABE ', '2020-08-25 13:59:27', 5000, 1, 5000, 'Kas Harian'),
(18, 1, 'AIR GALON', '2020-08-25 14:00:48', 5000, 3, 15000, 'Kas Harian'),
(19, 1, 'SENDOK PLASTIK', '2020-08-25 22:53:10', 15000, 1, 15000, 'Kas Harian'),
(20, 1, 'SERAI', '2020-08-26 23:13:33', 2000, 1, 2000, 'Kas Harian');

-- --------------------------------------------------------

--
-- Table structure for table `tb_trx_penjualan`
--

CREATE TABLE `tb_trx_penjualan` (
  `id_trx_penjualan` varchar(12) NOT NULL,
  `tgl_trx_input` datetime NOT NULL,
  `tgl_trx_bayar` datetime DEFAULT NULL,
  `tagihan_akhir` int(11) NOT NULL,
  `tagihan_awal` int(11) DEFAULT NULL,
  `total_diskon` int(11) DEFAULT NULL,
  `tax` int(11) DEFAULT NULL,
  `terbayar` int(11) DEFAULT NULL,
  `komisi` int(11) NOT NULL,
  `id_pelanggan` varchar(7) NOT NULL,
  `nama` varchar(32) NOT NULL,
  `no_meja` varchar(20) NOT NULL,
  `id_method_bayar` int(11) DEFAULT NULL,
  `jenis_tagihan` enum('Open bill','Lunas') NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_trx_penjualan`
--

INSERT INTO `tb_trx_penjualan` (`id_trx_penjualan`, `tgl_trx_input`, `tgl_trx_bayar`, `tagihan_akhir`, `tagihan_awal`, `total_diskon`, `tax`, `terbayar`, `komisi`, `id_pelanggan`, `nama`, `no_meja`, `id_method_bayar`, `jenis_tagihan`, `id_user`) VALUES
('200824001', '2020-08-24 13:06:53', '2020-08-24 13:06:53', 23400, 31250, 7900, 0, 23400, 4680, '1', 'JAURDAN', '1521', 3, 'Lunas', 1),
('200824002', '2020-08-24 14:41:21', '2020-08-24 14:41:21', 19000, 25000, 6000, 0, 19000, 3800, '1', 'JENNIFER', '7946', 3, 'Lunas', 1),
('200824003', '2020-08-24 16:03:07', '2020-08-24 16:03:07', 12000, 15000, 3000, 0, 12000, 2400, '1', 'FANNY', '9713', 3, 'Lunas', 1),
('200824004', '2020-08-24 19:27:17', '2020-08-24 19:27:17', 19000, 25000, 6000, 0, 19000, 3800, '1', 'PUTRI ARWANDA', '3642', 3, 'Lunas', 1),
('200824005', '2020-08-24 19:45:28', '2020-08-24 19:45:28', 14000, 17500, 3500, 0, 14000, 2800, '1', 'NOVA', '5592', 3, 'Lunas', 1),
('200824006', '2020-08-24 23:15:06', '2020-08-24 23:15:06', 1000, 1000, 0, 0, 1000, 0, '1', 'TEST', '1', 1, 'Lunas', 1),
('200825001', '2020-08-25 15:04:11', '2020-08-25 15:04:11', 33000, 42500, 9500, 0, 33000, 6600, '1', 'LINDA', '6238', 3, 'Lunas', 1),
('200825002', '2020-08-25 15:57:43', '2020-08-25 15:57:43', 36400, 47500, 11300, 0, 36400, 7280, '1', 'STEVEN', '8742', 3, 'Lunas', 1),
('200825003', '2020-08-25 16:48:18', '2020-08-25 16:48:18', 32000, 32000, 0, 0, 50000, 0, '1', 'TASYA', '1', 1, 'Lunas', 1),
('200825004', '2020-08-25 18:34:50', '2020-08-25 18:34:50', 19000, 25000, 6000, 0, 19000, 3800, '1', 'JENIFER', '1748', 3, 'Lunas', 1),
('200825005', '2020-08-25 18:51:08', NULL, 39000, 39000, 0, 0, NULL, 0, 'PL0004', 'UMKM PROJECT', '1', NULL, 'Open bill', 1),
('200825006', '2020-08-25 19:36:18', '2020-08-25 19:36:18', 54000, 67500, 13500, 0, 54000, 10800, '1', 'NIKI', '1468', 3, 'Lunas', 1),
('200825007', '2020-08-25 20:04:22', '2020-08-26 15:50:43', 38000, 38000, 0, 0, 50000, 0, 'PL0005', 'NJUZTIN', '1', 1, 'Lunas', 1),
('200825008', '2020-08-25 20:49:12', '2020-08-25 20:49:12', 31000, 38750, 7800, 0, 31000, 6200, '1', 'JODHY', '3728', 3, 'Lunas', 1),
('200825009', '2020-08-25 21:06:57', '2020-08-25 21:06:57', 14000, 17500, 3500, 0, 14000, 2800, '1', 'NOVALEN', '3318', 3, 'Lunas', 1),
('200826001', '2020-08-26 15:51:17', '2020-08-26 15:51:17', 10000, 10000, 0, 0, 20000, 0, 'PL0005', 'NJUZTIN', '1', 1, 'Lunas', 1),
('200826002', '2020-08-26 17:42:16', '2020-08-26 17:42:16', 39800, 52500, 12800, 0, 39800, 7960, '1', 'LOISA', '8826', 3, 'Lunas', 1),
('200826003', '2020-08-26 19:57:53', '2020-08-26 19:57:53', 4400, 6250, 1900, 0, 4400, 880, '1', 'ZIDN', '7324', 3, 'Lunas', 1),
('200826004', '2020-08-26 21:39:49', '2020-08-26 21:39:49', 43000, 53750, 10800, 0, 43000, 8600, '1', 'JESIKA', '7962', 3, 'Lunas', 1),
('200826005', '2020-08-26 21:56:56', NULL, 12000, 12000, 0, 0, NULL, 0, '1', 'TASYA', '1', NULL, 'Open bill', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tb_trx_transferkas`
--

CREATE TABLE `tb_trx_transferkas` (
  `id_trx_transfer_kas` int(11) NOT NULL,
  `tgl_trx_transferkas` date NOT NULL,
  `dari_idkas` int(2) NOT NULL,
  `ke_idkas` int(2) NOT NULL,
  `kredit` int(11) NOT NULL,
  `debit` int(11) NOT NULL,
  `saldo_akhir_kas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_trx_virtual_acc`
--

CREATE TABLE `tb_trx_virtual_acc` (
  `id_trx_virtual_acc` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `tgl_trx_virtual_acc` date NOT NULL,
  `id_method_bayar` int(11) NOT NULL,
  `kredit` int(11) NOT NULL,
  `debit` int(11) NOT NULL,
  `saldo_akhir_acc` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_trx_virtual_acc`
--

INSERT INTO `tb_trx_virtual_acc` (`id_trx_virtual_acc`, `tgl_trx_virtual_acc`, `id_method_bayar`, `kredit`, `debit`, `saldo_akhir_acc`) VALUES
('OP20200824001', '2020-08-24', 3, 69920, 0, 0),
('OP20200826003', '2020-08-26', 3, 149920, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `hp_user` int(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `tgl_lahir_user` date NOT NULL,
  `jenis_user` enum('admin','kasir') NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id_user`, `username`, `hp_user`, `password`, `tgl_lahir_user`, `jenis_user`) VALUES
(1, 'admin', 1234, 'admin', '1994-09-09', 'admin'),
(2, 'kasir', 12345, 'kasir', '1994-09-09', 'kasir');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_add_deskripsi`
--
ALTER TABLE `tb_add_deskripsi`
  ADD PRIMARY KEY (`id_add_deskripsi`),
  ADD KEY `id_det_penjualan` (`id_det_penjualan`),
  ADD KEY `id_deskripsi` (`id_deskripsi`);

--
-- Indexes for table `tb_deskripsi`
--
ALTER TABLE `tb_deskripsi`
  ADD PRIMARY KEY (`id_deskripsi`),
  ADD KEY `id_kat_produk` (`id_kat_produk`);

--
-- Indexes for table `tb_discount`
--
ALTER TABLE `tb_discount`
  ADD PRIMARY KEY (`id_disc`);

--
-- Indexes for table `tb_kat_kas`
--
ALTER TABLE `tb_kat_kas`
  ADD PRIMARY KEY (`id_kat_kas`);

--
-- Indexes for table `tb_kat_pelanggan`
--
ALTER TABLE `tb_kat_pelanggan`
  ADD PRIMARY KEY (`id_kat_pelanggan`);

--
-- Indexes for table `tb_kat_pengeluaran`
--
ALTER TABLE `tb_kat_pengeluaran`
  ADD PRIMARY KEY (`id_kat_pengeluaran`);

--
-- Indexes for table `tb_kat_produk`
--
ALTER TABLE `tb_kat_produk`
  ADD PRIMARY KEY (`id_kat_produk`);

--
-- Indexes for table `tb_method_bayar`
--
ALTER TABLE `tb_method_bayar`
  ADD PRIMARY KEY (`id_method_bayar`);

--
-- Indexes for table `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`),
  ADD KEY `id_kat_user_pk` (`id_kat_pelanggan`) USING BTREE;

--
-- Indexes for table `tb_produk`
--
ALTER TABLE `tb_produk`
  ADD PRIMARY KEY (`id_produk`),
  ADD KEY `id_kat_produk_fk` (`id_kat_produk`);

--
-- Indexes for table `tb_report_operational`
--
ALTER TABLE `tb_report_operational`
  ADD PRIMARY KEY (`id_report_all`);

--
-- Indexes for table `tb_report_saldo_harian`
--
ALTER TABLE `tb_report_saldo_harian`
  ADD PRIMARY KEY (`id_report_sh`);

--
-- Indexes for table `tb_setting`
--
ALTER TABLE `tb_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_trx_detpenjualan`
--
ALTER TABLE `tb_trx_detpenjualan`
  ADD PRIMARY KEY (`id_trx_detpenjualan`),
  ADD KEY `id_produk` (`id_produk`),
  ADD KEY `id_penjualan` (`id_trx_penjualan`);

--
-- Indexes for table `tb_trx_pengeluaran`
--
ALTER TABLE `tb_trx_pengeluaran`
  ADD PRIMARY KEY (`id_trx_pengeluaran`),
  ADD KEY `id_kat_pengeluaran` (`id_kat_pengeluaran`);

--
-- Indexes for table `tb_trx_penjualan`
--
ALTER TABLE `tb_trx_penjualan`
  ADD PRIMARY KEY (`id_trx_penjualan`),
  ADD KEY `id_method_bayar` (`id_method_bayar`),
  ADD KEY `id_user` (`id_pelanggan`) USING BTREE;

--
-- Indexes for table `tb_trx_virtual_acc`
--
ALTER TABLE `tb_trx_virtual_acc`
  ADD PRIMARY KEY (`id_trx_virtual_acc`),
  ADD KEY `id_method_bayar` (`id_method_bayar`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_add_deskripsi`
--
ALTER TABLE `tb_add_deskripsi`
  MODIFY `id_add_deskripsi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tb_deskripsi`
--
ALTER TABLE `tb_deskripsi`
  MODIFY `id_deskripsi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `tb_discount`
--
ALTER TABLE `tb_discount`
  MODIFY `id_disc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tb_kat_pelanggan`
--
ALTER TABLE `tb_kat_pelanggan`
  MODIFY `id_kat_pelanggan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tb_kat_pengeluaran`
--
ALTER TABLE `tb_kat_pengeluaran`
  MODIFY `id_kat_pengeluaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tb_kat_produk`
--
ALTER TABLE `tb_kat_produk`
  MODIFY `id_kat_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tb_method_bayar`
--
ALTER TABLE `tb_method_bayar`
  MODIFY `id_method_bayar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tb_produk`
--
ALTER TABLE `tb_produk`
  MODIFY `id_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `tb_report_saldo_harian`
--
ALTER TABLE `tb_report_saldo_harian`
  MODIFY `id_report_sh` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `tb_setting`
--
ALTER TABLE `tb_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tb_trx_pengeluaran`
--
ALTER TABLE `tb_trx_pengeluaran`
  MODIFY `id_trx_pengeluaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_add_deskripsi`
--
ALTER TABLE `tb_add_deskripsi`
  ADD CONSTRAINT `tb_add_deskripsi_ibfk_1` FOREIGN KEY (`id_deskripsi`) REFERENCES `tb_deskripsi` (`id_deskripsi`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_deskripsi`
--
ALTER TABLE `tb_deskripsi`
  ADD CONSTRAINT `tb_deskripsi_ibfk_1` FOREIGN KEY (`id_kat_produk`) REFERENCES `tb_kat_produk` (`id_kat_produk`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_produk`
--
ALTER TABLE `tb_produk`
  ADD CONSTRAINT `tb_produk_ibfk_1` FOREIGN KEY (`id_kat_produk`) REFERENCES `tb_kat_produk` (`id_kat_produk`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_trx_detpenjualan`
--
ALTER TABLE `tb_trx_detpenjualan`
  ADD CONSTRAINT `tb_trx_detpenjualan_ibfk_1` FOREIGN KEY (`id_trx_penjualan`) REFERENCES `tb_trx_penjualan` (`id_trx_penjualan`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tb_trx_detpenjualan_ibfk_2` FOREIGN KEY (`id_produk`) REFERENCES `tb_produk` (`id_produk`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_trx_pengeluaran`
--
ALTER TABLE `tb_trx_pengeluaran`
  ADD CONSTRAINT `tb_trx_pengeluaran_ibfk_1` FOREIGN KEY (`id_kat_pengeluaran`) REFERENCES `tb_kat_pengeluaran` (`id_kat_pengeluaran`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
