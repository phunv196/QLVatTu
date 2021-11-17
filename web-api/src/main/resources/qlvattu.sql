/*
Navicat MySQL Data Transfer

Source Server         : Phunv
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : qlvattu

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2021-11-17 07:17:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for delivery_bill
-- ----------------------------
DROP TABLE IF EXISTS `delivery_bill`;
CREATE TABLE `delivery_bill` (
  `delivery_bill_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `date_delivery_bill` date DEFAULT NULL,
  `warehouse_id` int(11) DEFAULT NULL,
  `factory_id` int(11) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`delivery_bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of delivery_bill
-- ----------------------------
INSERT INTO `delivery_bill` VALUES ('1', 'XVT-16-11 ', 'Xuất vật tư ', '2021-11-16', '10', '2', '1', 'phunv test');

-- ----------------------------
-- Table structure for delivery_bill_flow
-- ----------------------------
DROP TABLE IF EXISTS `delivery_bill_flow`;
CREATE TABLE `delivery_bill_flow` (
  `delivery_bill_flow_id` int(11) NOT NULL AUTO_INCREMENT,
  `delivery_bill_id` int(11) DEFAULT NULL,
  `supplies_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`delivery_bill_flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of delivery_bill_flow
-- ----------------------------
INSERT INTO `delivery_bill_flow` VALUES ('1', '1', '1', '100', null);
INSERT INTO `delivery_bill_flow` VALUES ('2', '1', '2', '100', null);
INSERT INTO `delivery_bill_flow` VALUES ('3', '1', '7', '100', null);

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `department_id` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', 'KDVH', 'Phòng kinh doanh vận hành', 'khvh@gmail.com', '0987654321', 'tâng 4 tòa nhà test', '1');
INSERT INTO `department` VALUES ('2', 'KT', 'Kế toán', 'kt@gmail.com', '01213456789', 'tâng 3 tòa nhà test', '123123');
INSERT INTO `department` VALUES ('3', 'HCNS', 'Hành chính nhân sự', 'hcns@gmail.com', '0989118218', 'tầng 2 tòa nhà test', 'test');

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
  `employee_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(25) DEFAULT NULL,
  `position_id` int(11) DEFAULT NULL,
  `address` varchar(350) DEFAULT NULL,
  `sex` int(1) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of employees
-- ----------------------------
INSERT INTO `employees` VALUES ('1', '000001', 'Phú', 'Nguyễn Văn', 'Nguyễn Văn Phú', 'phunv@gmail.com', '0972479836', '3', 'test', '1', '1', '1996-08-06');
INSERT INTO `employees` VALUES ('2', '000002', 'Tiến', 'Hoàng Văn', 'Hoàng Văn Tiến', 'tienhv@gmail.com', '0562819812', '1', 'test', '1', '3', '1999-02-12');
INSERT INTO `employees` VALUES ('3', '000003', 'Thắng', 'Mai Văn', 'Mai Văn Thắng', 'thangmv@gmail.com', '0771212636', '2', 'test', '1', '1', '1991-08-24');
INSERT INTO `employees` VALUES ('4', '000004', 'Kim', 'Đinh Ngọc', 'Đinh Ngọc Kim', 'kimdn@gmail.com', '0217715263', '1', 'test', '1', '2', '1994-01-15');
INSERT INTO `employees` VALUES ('5', '000005', 'Nguyên', 'Đinh Trọng', 'Đinh Trọng Nguyên', 'nguyendt@gmail.com', '0864124156', '4', 'test', '1', '3', '1992-11-16');
INSERT INTO `employees` VALUES ('6', '000006', 'Giang', 'Nguyễn Thị', 'Nguyễn Thị Giang', 'giangnt@gmail.com', '0173517127', '4', 'test', '2', '1', '1990-11-25');
INSERT INTO `employees` VALUES ('7', '000007', 'Yến', 'Vũ Hải', 'Vũ Hải Yến', 'yenvh@gmail.com', '0126712526', '2', 'test', '2', '1', '1997-05-05');
INSERT INTO `employees` VALUES ('8', '000008', 'Mạnh', 'Đinh Văn', 'Đinh Văn Mạnh', 'manhdv@gmail.com', '0623415421', '1', 'test', '1', '3', '1997-07-09');
INSERT INTO `employees` VALUES ('9', '000009', 'Phú', 'Hoàng Minh', 'Hoàng Minh Phú', 'phuhm@gmail.com', '0923512135', '2', 'test', '1', '2', '1999-12-12');
INSERT INTO `employees` VALUES ('10', '000010', 'Hiếu', 'Hoàng Chí', 'Hoàng Chí Hiếu', 'hieuhc@gmail.com', '0451542713', '4', 'test', '1', '2', '1995-11-13');
INSERT INTO `employees` VALUES ('11', '000011', 'Đức', 'Nguyễn Văn', 'Nguyễn Văn Đức', 'ducnv@gmail.com', '0351758171', '2', 'test', '1', '1', '1992-05-24');
INSERT INTO `employees` VALUES ('12', '000012', 'Hồng Hạnh', 'Nguyễn Thị', 'Nguyễn Thị Hồng Hạnh', 'hanhnth@gmail.com', '0884521412', '4', 'test', '2', '1', '1998-09-16');
INSERT INTO `employees` VALUES ('13', '000013', 'Lực', 'Đào Văn', 'Đào Văn Lực', 'lucdv@gmail.com', '0673517582', '2', 'test', '1', '3', '1999-08-23');
INSERT INTO `employees` VALUES ('14', '000014', 'Đức', 'Trình Hữu', 'Trình Hữu Đức', 'ducth@gmail.com', '0992635157', '1', 'test', '1', '1', '1990-08-04');
INSERT INTO `employees` VALUES ('15', '000015', 'Thơm', 'Đỗ Thị', 'Đỗ Thị Thơm', 'thomdt@gmail.com', '0248268287', '4', 'test', '2', '2', '1991-09-06');

-- ----------------------------
-- Table structure for factory
-- ----------------------------
DROP TABLE IF EXISTS `factory`;
CREATE TABLE `factory` (
  `factory_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `date_construction` date DEFAULT NULL,
  `date_finish` date DEFAULT NULL,
  PRIMARY KEY (`factory_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of factory
-- ----------------------------
INSERT INTO `factory` VALUES ('1', 'Px-NH', 'Phân xưởng Ngọc Hồi', 'pxsxnh@gmail.com', '79 Ngọc Hồi - Hoàng Liệt - Hoàng Mai - Hà Nội', 'test', '3', '2020-01-01', '2021-12-31');
INSERT INTO `factory` VALUES ('2', 'Px-TT', 'Phân xưởng Thanh Trì', 'pxsxtt@gmail.com', 'Đ. Phan Trọng Tuệ, Tam Hiệp, Thanh Trì, Hà Nội', 'test', '2', '2021-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('3', 'Px-DD', 'Phân xưởng Đống Đa', 'pxsxdd@gmail.com', '1194 Đường Láng, Láng Thượng, Đống Đa, Hà Nội', 'test', '3', '2021-01-01', '2021-12-31');
INSERT INTO `factory` VALUES ('4', 'Px-LB', 'Phân xưởng Long Biên', 'pxsxlb@gmail.com', 'Tòa Nhà Thành Hưng, 104, Nguyễn Văn Cừ, Huyện Gia Lâm, Hà Nội, Việt Nam', 'test', '4', '2020-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('5', 'Px-LN', 'Phân xưởng Lĩnh Nam', 'pxsxln@gmail.com', '551, Lĩnh Nam, Quận Hoàng Mai, Hà Nội', 'test', '5', '2021-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('6', 'Px-HD', 'Phân xưởng Hà Đông', 'pxsxhd@gmail.com', 'Phố Văn La, Phú La, Hà Đông, Hà Nội', 'test', '6', '2020-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('7', 'Px-LD', 'Phân xưởng Linh Đàm', 'pxsxld@gmail.com', 'OCT2-ĐN1 Đặng Xuân Bảng, Đại Kim, Hoàng Mai, Hà Nội, Việt Nam', 'test', '7', '2021-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('8', 'Px-GL', 'Phân xưởng Gia Lâm', 'pxsxgl@gmail.com', 'QL5, Dương Xá, Gia Lâm, Hà Nội, Việt Nam', 'test', '8', '2020-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('9', 'Px-DA', 'Phân xưởng Đông Anh', 'pxsxda@gmail.com', 'Võng La, Đông Anh, Hà Nội, Việt Nam', 'test', '9', '2021-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('10', 'Px-NTL', 'Phân xưởng Nam Từ Liêm', 'pxsxntl@gmail.com', 'Khu E6, Phạm Hùng, Cầu Giấy, Hà Nội, Việt Nam', 'test', '10', '2021-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('11', 'Px-BTL', 'Phân xưởng Bắc Từ Liêm', 'pxsxbtl@gmail.com', '136 Phạm Văn Đồng, Xuân Đỉnh, Bắc Từ Liêm, Hà Nội, Việt Nam', 'test', '11', '2021-01-01', '2022-12-31');
INSERT INTO `factory` VALUES ('12', 'Px-SS', 'Phân xưởng Sóc Sơn', 'pxsxss@gmail.com', 'QL3, Phủ Lỗ, Sóc Sơn, Hà Nội, Việt Nam', 'test', '12', '2021-01-01', '2022-12-31');

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
  `position_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `position` VALUES ('1', 'TP', 'Trưởng phòng', 'phunv â');
INSERT INTO `position` VALUES ('2', 'PP', 'Phó phòng', 'thunguyen');
INSERT INTO `position` VALUES ('3', 'GD', 'Giám đốc', 'yennt');
INSERT INTO `position` VALUES ('4', 'NV', 'Nhân viên', 'linh');

-- ----------------------------
-- Table structure for quality
-- ----------------------------
DROP TABLE IF EXISTS `quality`;
CREATE TABLE `quality` (
  `quality_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`quality_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quality
-- ----------------------------
INSERT INTO `quality` VALUES ('1', 'TOT', 'tốt', 'tốt');
INSERT INTO `quality` VALUES ('2', 'TB', 'trung bình', 'trung bình');
INSERT INTO `quality` VALUES ('3', 'KEM', 'kém', 'kém');

-- ----------------------------
-- Table structure for receipt
-- ----------------------------
DROP TABLE IF EXISTS `receipt`;
CREATE TABLE `receipt` (
  `receipt_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `date_warehousing` date DEFAULT NULL,
  `warehouse_id` int(11) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`receipt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receipt
-- ----------------------------
INSERT INTO `receipt` VALUES ('1', 'NK-PHUNV', 'Nhập vật tư xây dựng', '2021-11-14', '10', '1', 'Nhập kho vật tư xây dựng');

-- ----------------------------
-- Table structure for receipt_flow
-- ----------------------------
DROP TABLE IF EXISTS `receipt_flow`;
CREATE TABLE `receipt_flow` (
  `receipt_flow_id` int(11) NOT NULL AUTO_INCREMENT,
  `receipt_id` int(11) DEFAULT NULL,
  `supplies_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `received` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`receipt_flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receipt_flow
-- ----------------------------
INSERT INTO `receipt_flow` VALUES ('1', '1', '1', '1000', '500', 'test');
INSERT INTO `receipt_flow` VALUES ('3', '1', '2', '1500', null, null);
INSERT INTO `receipt_flow` VALUES ('4', '1', '7', '500', null, null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(25) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ADMIN', 'Quản trị viên', null);
INSERT INTO `role` VALUES ('2', 'SUPPORT', 'Quản lý kho', '');

-- ----------------------------
-- Table structure for species
-- ----------------------------
DROP TABLE IF EXISTS `species`;
CREATE TABLE `species` (
  `species_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`species_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of species
-- ----------------------------
INSERT INTO `species` VALUES ('1', 'VLCA', 'Vật liệu cách âm', 'cách âm');
INSERT INTO `species` VALUES ('2', 'VLCN', 'Vật liệu cách nhiệt', 'Cách nhiệt');
INSERT INTO `species` VALUES ('3', 'VLTA', 'Vật liệu tiêu âm', 'tiêu âm');
INSERT INTO `species` VALUES ('11', 'VLNL', 'Vật liệu ngành lò', 'ngành lò');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `supplier_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(25) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES ('1', 'GreenTech', 'Vật Liệu Xây Dựng Green Tech - Công Ty TNHH Thương Mại Green Tech', 'cskh@greentechco.com.vn', '0283510787', 'B2 Đường Số 3, Khu Phố 4, Phường An Khánh, Thành Phố Thủ Đức, Tp. Hồ Chí Minh (TPHCM)', 'Website: www.greentechco.com.vn');
INSERT INTO `supplier` VALUES ('2', 'NhaXanh', 'Vật Liệu Xây Dựng Nhà Xanh - Công Ty Cổ Phần Đầu Tư Và Phát Triển Xây Dựng Nhà Xanh', 'xaydung@nhaxanh.com.vn', '2,743,515,678', '489 Huỳnh Văn Lũy, P. Phú Mỹ, TP. Thủ Dầu Một, Bình Dương', 'Website: www.nhaxanh.com.vn');
INSERT INTO `supplier` VALUES ('3', 'ThaoTrang', 'Vật Liệu Xây Dựng Và San Lấp Mặt Bằng Thảo Trang - Công Ty TNHH Thương Mại Dịch Vụ Vận Tải Thảo Trang', 'thaotrangvatlieuxaydung@gmail.com', '02513848620', 'Tổ 19, Ấp Trầu, Phước Thiền, Nhơn Trạch, Đồng Nai', 'Website: www.vatlieuxaydungthaotrang.com');
INSERT INTO `supplier` VALUES ('4', 'SongPhuong', 'Vật Liệu Xây Dựng BM Song Phương - Công Ty TNHH MTV BM Song Phương', 'bmsongphuong@gmail.com', '02336292888', 'Số 111 Trần Hưng Đạo, P. 1, TP. Đông Hà, Quảng Trị', 'Website: www.bmsongphuong.bizz.vn');
INSERT INTO `supplier` VALUES ('5', 'MTVVinaBuilt', 'Vật Liệu Xây Dựng Vina Built - Công Ty TNHH MTV Vina Built', 'vinbuilt7777@gmail.com', '0987447766', '394/35 Âu Cơ, P. 10, Q. Tân Bình, Tp. Hồ Chí Minh (TPHCM)', 'Website: www.vinbuilt.bizz.vn');
INSERT INTO `supplier` VALUES ('6', 'DatHoangAnh', 'Vật Liệu Xây Dựng Đạt Hoàng Anh', 'congtydha2019@gmail.com', '02543895810', 'KP. Mỹ Thạnh, P. Mỹ Xuân, TX. Phú Mỹ, Bà Rịa-Vũng Tàu', 'Website: www.dathoanganh.com');
INSERT INTO `supplier` VALUES ('7', 'HiepNga', 'Cơ Sở Sản Xuất Xốp Âm Sàn Bê Tông Hiệp Nga', 'hiepskey@gmail.com', '0398190366', 'Ngõ 10 Mai Hắc Đế, Định Trung, Thành Phố Vĩnh Yên, Vĩnh Phúc', 'Nhà Máy Sản Xuất: Khu Công Nghiệp VSIP Bắc Ninh\nWebsite: www.xopamsanbetong.com');
INSERT INTO `supplier` VALUES ('8', 'ThanhCong', 'Vật Liệu Xây Dựng Thành Công', 'quynhnhu0409@gmail.com', '02763884112', 'Tổ 16, Ấp An Qưới, Xã An Tịnh, Huyện Trảng Bàng, Tây Ninh', 'VPGD: KDC Thành Thành Công, Ấp An Qưới, Xã An Hòa, Huyện Trảng Bàng, Tây Ninh\nWebsite: www.vlxdthuyetquynhnhu.bizz.vn');
INSERT INTO `supplier` VALUES ('9', 'MinhKhangKhanhHoa', 'ật Liệu Xây Dựng Minh Khang Khánh Hòa - Công Ty TNHH VLXD Minh Khang Khánh Hòa', 'vlxdquocthien332@gmail.com', '02583515032', '177 Ngô Gia Tự, TP. Nha Trang, Khánh Hòa', 'Website: www.vatlieuxaydungminhkhang.bizz.vn');
INSERT INTO `supplier` VALUES ('10', 'ChuanViet', 'Vật Liệu Xây Dựng Chuẩn Việt - Công Ty TNHH Tư Vấn Quản Lý Chuẩn Việt', 'nvanchinh192@gmail.com', '0989794768', 'Số 06/1A, Tổ 32, KP3, P.Trảng Dài, TP.Biên Hòa, Đồng Nai', 'Website: www.bvcon.com.vn');

-- ----------------------------
-- Table structure for supplies
-- ----------------------------
DROP TABLE IF EXISTS `supplies`;
CREATE TABLE `supplies` (
  `supplies_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `species_id` int(11) DEFAULT NULL,
  `quality_id` int(11) DEFAULT NULL,
  `supplier_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`supplies_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of supplies
-- ----------------------------
INSERT INTO `supplies` VALUES ('1', 'BTTCA', 'Bông thủy tinh cách âm', 'test', '500000', '1', '1', '8', '15');
INSERT INTO `supplies` VALUES ('2', 'BKCAH', 'Bông khoáng cách âm Huamei', 'test', '520000', '1', '2', '1', '15');
INSERT INTO `supplies` VALUES ('3', 'TKCA', 'Túi khí cách âm Xốp PE – OPP cách âm – chống ồn', 'test', '450000', '1', '1', '1', '11');
INSERT INTO `supplies` VALUES ('4', 'XXCA', 'Xốp XPS cách âm', 'test', '350000', '1', '3', '5', '12');
INSERT INTO `supplies` VALUES ('5', 'CSNCA', 'Cao su non cách âm', 'test', '120000', '1', '1', '10', '12');
INSERT INTO `supplies` VALUES ('6', 'CSLH', 'Cao su lưu hóa', 'test', '150000', '1', '3', '7', '12');
INSERT INTO `supplies` VALUES ('7', 'TCCA', 'Trần – vách thạch cao cách âm', 'test', '500000', '1', '1', '8', '15');
INSERT INTO `supplies` VALUES ('8', 'BBO', 'Bông bảo ôn', 'test', '170000', '2', '2', '1', '12');
INSERT INTO `supplies` VALUES ('9', 'XBO', 'Xốp PE – OPP bảo ôn', 'test', '390000', '2', '1', '5', '11');
INSERT INTO `supplies` VALUES ('10', 'XCN', 'Xốp cách nhiệt XPS – chống thấm', 'test', '340000', '2', '1', '9', '11');
INSERT INTO `supplies` VALUES ('11', 'GBBO', 'Giấy bạc bảo ôn – cách nhiệt', 'test', '500000', '2', '3', '9', '15');
INSERT INTO `supplies` VALUES ('12', 'CSLHBO', 'Cao su lưu hóa bảo ôn – chống rung', 'test', '140000', '2', '1', '5', '12');
INSERT INTO `supplies` VALUES ('13', 'TCCNC', 'Tấm ceramic chịu nhiệt cao', 'test', '470000', '2', '2', '4', '11');
INSERT INTO `supplies` VALUES ('14', 'OGM', 'Ống gió mềm', 'test', '90000', '2', '1', '7', '11');
INSERT INTO `supplies` VALUES ('15', 'BTA', 'Bông tiêu âm', 'test', '400000', '3', '1', '8', '12');
INSERT INTO `supplies` VALUES ('16', 'GTA', 'Gỗ tiêu âm', 'test', '500000', '3', '2', '10', '15');
INSERT INTO `supplies` VALUES ('17', 'TTA', 'Tấm tiêu âm', 'test', '270000', '3', '1', '5', '11');
INSERT INTO `supplies` VALUES ('18', 'LGTA', 'Len gỗ tiêu âm', 'test', '90000', '3', '3', '9', '11');
INSERT INTO `supplies` VALUES ('19', 'MGTA', 'Mút trứng – mút gai tiêu âm', 'test', '50000', '3', '1', '5', '11');
INSERT INTO `supplies` VALUES ('20', 'GCL', 'Gạch chịu lửa', 'test', '20000', '11', '1', '1', '20');
INSERT INTO `supplies` VALUES ('21', 'GC', 'Gạch chèn', 'test', '15000', '11', '2', '7', '20');
INSERT INTO `supplies` VALUES ('22', 'TC', 'Tết chèn', 'test', '70000', '11', '1', '10', '11');
INSERT INTO `supplies` VALUES ('23', 'NTT', 'Nước thủy tinh', 'test', '1500000', '11', '1', '4', '19');
INSERT INTO `supplies` VALUES ('24', 'BXLCL', 'Bột xây lò các loại', 'test', '1200000', '11', '1', '1', '13');

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unit
-- ----------------------------
INSERT INTO `unit` VALUES ('11', 'CAI', 'Cái', 'cái');
INSERT INTO `unit` VALUES ('12', 'KG', 'Kg', 'cân');
INSERT INTO `unit` VALUES ('13', 'TA', 'Tạ', '100kg');
INSERT INTO `unit` VALUES ('14', 'TAN', 'Tấn', '1000kg');
INSERT INTO `unit` VALUES ('15', 'M', 'Mét', 'mét');
INSERT INTO `unit` VALUES ('16', '10M', '10m', '10 mét');
INSERT INTO `unit` VALUES ('17', 'XE', 'Xe', 'xe');
INSERT INTO `unit` VALUES ('18', 'BAO', 'Bao', 'bao');
INSERT INTO `unit` VALUES ('19', 'M3', 'M3', 'khối');
INSERT INTO `unit` VALUES ('20', 'VIEN', 'Viên', 'viên');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) NOT NULL,
  `password` varchar(200) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `full_name` varchar(150) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_users__employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'admin', '8907986f946879897664f0afa6ca054f63cc9b5e', 'ADMIN', '1', 'phunv@gmail.com', 'Nguyễn Văn Phú', '0972479836');
INSERT INTO `users` VALUES ('2', 'thangmv', 'ec01406ecdb1856ac456b13204a19e7380adfd49', 'SUPPORT', '3', 'thangmv@gmail.com', 'Mai Văn Thắng', '0771212636');
INSERT INTO `users` VALUES ('3', 'tienhv', 'dc95b2a8f31dd79753fc0e8fedfd718f0b55f511', 'SUPPORT', '2', 'tienhv@gmail.com', 'Hoàng Văn Tiến', '0562819812');
INSERT INTO `users` VALUES ('4', 'kimdn', 'f6f385ae3f2824d76800c21474698f91585ac5b6', 'SUPPORT', '4', 'kimdn@gmail.com', 'Đinh Ngọc Kim', '0217715263');
INSERT INTO `users` VALUES ('5', 'nguyendt', 'e36f9be1c917520420d26ed002da9279c97b49a2', 'SUPPORT', '5', 'nguyendt@gmail.com', 'Đinh Trọng Nguyên', '0864124156');
INSERT INTO `users` VALUES ('6', 'phunv', '2e95dda497a4a02ed1aac725bc750c5b6117a16d', 'SUPPORT', '1', 'phunv@gmail.com', 'Nguyễn Văn Phú', '0972479836');

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `warehouse_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES ('1', 'Kho-TT', 'Kho Thường tín', 'khott@gmail.com', '0948362547', 'Thường Tín Hà Nội', 'Bình');
INSERT INTO `warehouse` VALUES ('2', 'Kho-PX', 'Kho Phú Xuyên', 'khopx@gmail.com', '0938756218', 'Phú Xuyên Hà Nội', 'Nam');
INSERT INTO `warehouse` VALUES ('3', 'Kho-SS', 'Kho Sóc Sơn', 'khoss@gmail.com', '0917351413', 'Sóc Sơn Hà Nội', 'Tiến');
INSERT INTO `warehouse` VALUES ('4', 'Kho-DA', 'Kho Đông Anh', 'khoda@gmail.com', '0927452416', 'Đông Anh Hà Nội', 'Linh');
INSERT INTO `warehouse` VALUES ('5', 'Kho-HM', 'Kho Hoàng Mai', 'khohm@gmail.com', '0916344753', 'Hoàng Mai Hà Nội', 'Minh');
INSERT INTO `warehouse` VALUES ('6', 'Kho-ST', 'Kho Sơn Tây', 'khost@gmail.com', '0916354764', 'Sơn Tây Hà Nội', 'Vương');
INSERT INTO `warehouse` VALUES ('7', 'Kho-DD', 'Kho Đống Đa', 'khodd@gmail.com', '0981634465', 'Đống Đa Hà Nội', 'Quang ');
INSERT INTO `warehouse` VALUES ('8', 'Kho-TX', 'Kho Thanh Xuân', 'khotx@gmail.com', '0981344652', 'Thanh Xuân Hà Nội', 'Hải');
INSERT INTO `warehouse` VALUES ('9', 'Kho-LD', 'Kho Linh Đàm', 'khold@gmail.com', '0918264653', 'Linh Đàm Hà Nội', 'Dũng');
INSERT INTO `warehouse` VALUES ('10', 'Kho-HL', 'Kho Hoàng Liệt', 'khohl@gmail.com', '0916254654', 'Hoàng Liệt Hà Nội', null);

-- ----------------------------
-- Table structure for warehouse_card
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_card`;
CREATE TABLE `warehouse_card` (
  `warehouse_card_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `supplies_id` int(11) DEFAULT NULL,
  `warehouse_id` int(11) DEFAULT NULL,
  `date_created` date DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`warehouse_card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_card
-- ----------------------------
INSERT INTO `warehouse_card` VALUES ('1', 'Tk-BTTCA', 'Thông tin xuất nhập:BTTCA', '1', '10', '2021-11-14', 'test', '1');

-- ----------------------------
-- Table structure for warehouse_card_flow
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_card_flow`;
CREATE TABLE `warehouse_card_flow` (
  `warehouse_card_flow_id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_card_id` int(11) DEFAULT NULL,
  `delivery_bill_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `receipt_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  `create_at` date DEFAULT NULL,
  PRIMARY KEY (`warehouse_card_flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_card_flow
-- ----------------------------
INSERT INTO `warehouse_card_flow` VALUES ('1', '1', null, null, '1', '500', '2', '2021-11-14');
SET FOREIGN_KEY_CHECKS=1;
