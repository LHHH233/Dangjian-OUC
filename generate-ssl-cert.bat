@echo off
echo 正在生成SSL证书...
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src\main\resources\keystore.p12 -validity 3650 -storepass changeit -keypass changeit -dname "CN=localhost, OU=IT, O=PartyMember, L=Beijing, ST=Beijing, C=CN"
echo SSL证书生成完成！
echo 证书位置: src\main\resources\keystore.p12
echo 证书密码: changeit
pause

