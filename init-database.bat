@echo off
echo 正在初始化MySQL数据库...
echo 请确保MySQL服务已启动
echo.

REM 尝试连接MySQL并执行初始化脚本
mysql -u root -p < init-mysql.sql

if %errorlevel% equ 0 (
    echo 数据库初始化成功！
) else (
    echo 数据库初始化失败，请检查MySQL连接信息
    echo 请手动执行以下命令：
    echo mysql -u root -p
    echo 然后复制 init-mysql.sql 中的内容执行
)

pause

