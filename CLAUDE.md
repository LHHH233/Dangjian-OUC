# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在本仓库中工作时提供指导。

## 项目概述

党员管理系统 — 基于 Spring Boot 的党员信息管理 Web 应用，支持公开查询和管理员增删改查操作。全中文界面。

## 构建与运行命令

```bash
mvn spring-boot:run              # 启动开发服务器（端口 8080）
mvn clean package                # 构建 JAR 包（target/party-member-management-0.0.1-SNAPSHOT.jar）
mvn test                         # 运行测试
```

环境要求：Java 17+、Maven 3.6+、MySQL 8.0+，数据库 `party_member_db` 运行在 localhost:3306（root/123456）。

## 架构

标准 Spring Boot 分层 MVC 架构，使用 Thymeleaf 服务端渲染：

- **实体层**：单一 JPA 实体 `PartyMember`（16 个字段：姓名、性别、民族、政治面貌、身份证号、出生日期、手机号、家庭住址、入学日期、学历、专业、年级、入党日期、党组织、序号 + 时间戳）
- **数据访问层**：`PartyMemberRepository` 继承 `JpaRepository`，包含自定义 JPQL 查询（`searchByNameAndIdCard` 使用 `LIKE` 配合 `@Query`）
- **服务层**：`PartyMemberService` 处理增删改查 + 通过 Apache POI（`XWPFDocument`）生成 Word 证明文档
- **控制器层**：
  - `PartyMemberController` — 公开查询（`/query`）和管理员党员管理（`/admin/*`），包括 Excel 导出和批量删除
  - `AdminController` — 登录/仪表盘及统计信息
  - `ExcelImportController` — Excel 文件上传导入（`/admin/import`）
  - `SimplePdfController` — 备用 Word 证明文档生成
- **配置层**：`SecurityConfig`（Spring Security 内存用户，BCrypt 加密）、`DataInitializer`（首次启动自动填充 3 条示例数据）

## 安全模型

- 公开端点：`/`、`/query`、`/proof/**`、`/simple-proof/**`、`/test-pdf`、静态资源
- 受保护端点：`/admin/**` 需要 ADMIN 角色（表单登录页 `/admin/login`）
- CSRF 已禁用；HTTPS 配置存在但在 `application.yml` 中已注释

## 关键技术细节

- Hibernate `ddl-auto: update` — 数据库表结构自动管理，无需手动迁移
- 文档生成使用 Apache POI 生成 Word（.docx），中文字体为宋体
- 项目引入了 iText7/html2pdf 依赖用于 PDF 生成
- Thymeleaf 模板位于 `src/main/resources/templates/`（共 9 个 HTML 文件）
- `id_card_number` 在数据库中有唯一约束
- 所有日期使用 `LocalDate`，文档中格式化为 `yyyy年MM月dd日`

## Git 提交规范

提交信息格式：
```
<type>: <description>
```

| 类型 | 说明 |
|------|------|
| feat | 新功能 |
| fix | Bug 修复 |
| docs | 文档更新 |
| style | 代码格式（不影响代码运行） |
| refactor | 重构 |
| perf | 性能优化 |
| test | 测试 |
| chore | 构建/工具链 |
