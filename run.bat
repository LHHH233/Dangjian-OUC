@echo off
echo 正在启动党员管理系统...
echo 请确保MySQL数据库已启动并执行了初始化脚本
echo.
echo 访问地址:
echo 查询端: https://localhost:8443/
echo 管理员端: https://localhost:8443/admin/login
echo 默认管理员账号: admin / admin123
echo.
mvn spring-boot:run
pause

