# 党员管理系统

基于Spring Boot和HTTPS的党员信息管理系统，支持党员信息查询、管理和证明文档导出功能。

## 功能特性

### 党员查询端
- 通过姓名和身份证号查询党员信息
- 显示详细的党员信息
- 支持导出党员证明文档（Word格式）

### 管理员端
- 管理员登录认证
- 添加、编辑、删除党员信息
- 党员信息列表展示和搜索
- 批量管理党员信息

## 技术栈

- **后端**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **前端**: Thymeleaf, HTML5, CSS3, JavaScript
- **数据库**: MySQL 8.0
- **文档生成**: Apache POI
- **安全**: HTTPS, BCrypt密码加密

## 快速开始

### 环境要求

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd party-member-management
   ```

2. **配置数据库**
   - 创建MySQL数据库
   - 执行 `src/main/resources/sql/schema.sql` 初始化数据库
   - 修改 `src/main/resources/application.yml` 中的数据库连接信息

3. **生成SSL证书**
   ```bash
   keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 3650
   ```
   密码设置为: `changeit`

4. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

5. **访问系统**
   - 查询端: https://localhost:8443/
   - 管理员端: https://localhost:8443/admin/login
   - 默认管理员账号: admin / admin123

## 系统架构

### 数据库设计

党员信息表包含以下字段：
- 基本信息：姓名、性别、民族、政治面貌、入党日期
- 身份信息：身份证号、出生日期、出生地、家庭住址、手机号
- 教育信息：入学日期、学历、专业、年级
- 组织信息：所在党组织、序号

### 安全配置

- 使用HTTPS协议确保数据传输安全
- Spring Security提供身份认证和授权
- 管理员功能需要登录认证
- 查询功能对公众开放

### 文档导出

- 使用Apache POI生成Word文档
- 支持党员证明文档模板化生成
- 自动填充党员信息到文档模板

## API接口

### 查询接口
- `GET /` - 首页（重定向到查询页面）
- `GET /query` - 查询页面
- `POST /query` - 处理查询请求
- `GET /proof/query` - 从查询页面导出证明

### 管理接口
- `GET /admin/login` - 管理员登录页面
- `POST /admin/login` - 处理登录请求
- `GET /admin` - 管理员首页
- `GET /admin/add` - 添加党员页面
- `POST /admin/add` - 处理添加请求
- `GET /admin/edit/{id}` - 编辑党员页面
- `POST /admin/edit` - 处理编辑请求
- `GET /admin/delete/{id}` - 删除党员
- `GET /admin/search` - 搜索党员
- `GET /proof/{id}` - 导出党员证明

## 部署说明

### 生产环境配置

1. **修改数据库配置**
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://your-db-host:3306/party_member_db
       username: your-username
       password: your-password
   ```

2. **配置HTTPS证书**
   - 将生产环境的SSL证书替换 `keystore.p12`
   - 修改 `application.yml` 中的证书配置

3. **安全加固**
   - 修改默认管理员密码
   - 配置防火墙规则
   - 定期备份数据库

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/party-member-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 开发说明

### 项目结构
```
src/main/java/com/partymember/
├── config/          # 配置类
├── controller/      # 控制器
├── entity/          # 实体类
├── repository/      # 数据访问层
└── service/         # 业务逻辑层

src/main/resources/
├── templates/       # Thymeleaf模板
├── sql/            # 数据库脚本
└── application.yml # 配置文件
```

### 扩展功能

- 可以添加更多查询条件
- 支持Excel导入导出
- 添加数据统计和报表功能
- 集成消息通知系统

## 许可证

MIT License

## 联系方式

如有问题或建议，请联系开发团队。
