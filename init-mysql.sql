-- 创建数据库
CREATE DATABASE IF NOT EXISTS party_member_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE party_member_db;

-- 创建用户（如果不存在）
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'password';

-- 授权
GRANT ALL PRIVILEGES ON party_member_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;

-- 创建党员信息表
CREATE TABLE IF NOT EXISTS party_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    serial_number VARCHAR(50),
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    ethnic_group VARCHAR(50),
    political_status VARCHAR(100),
    join_date DATE,
    id_card_number VARCHAR(18) UNIQUE,
    birth_date DATE,
    birthplace VARCHAR(200),
    residence VARCHAR(500),
    phone_number VARCHAR(20),
    admission_date DATE,
    degree VARCHAR(50),
    major VARCHAR(100),
    class_of_year VARCHAR(50),
    organization VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入示例数据
INSERT INTO party_members (serial_number, name, gender, ethnic_group, political_status, join_date, id_card_number, birth_date, birthplace, residence, phone_number, admission_date, degree, major, class_of_year, organization) VALUES
('001', '张三', '男', '汉族', '中共党员', '2020-06-15', '110101199001011234', '1990-01-01', '北京市东城区', '北京市朝阳区某某街道123号', '13800138001', '2018-09-01', '本科', '计算机科学与技术', '2018级', '计算机学院学生党支部'),
('002', '李四', '女', '汉族', '中共党员', '2021-03-20', '110101199205152345', '1992-05-15', '北京市西城区', '北京市海淀区某某小区456号', '13800138002', '2019-09-01', '硕士', '软件工程', '2019级', '软件学院学生党支部'),
('003', '王五', '男', '回族', '中共预备党员', '2022-12-01', '110101199503103456', '1995-03-10', '北京市海淀区', '北京市丰台区某某路789号', '13800138003', '2020-09-01', '本科', '信息管理与信息系统', '2020级', '管理学院学生党支部');

