package com.partymember.controller;

import com.partymember.entity.PartyMember;
import com.partymember.service.PartyMemberService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ExcelImportController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportController.class);

    @Autowired
    private PartyMemberService partyMemberService;

    /**
     * 显示Excel导入页面
     */
    @GetMapping("/import")
    public String showImportPage(Model model) {
        return "excel-import";
    }

    /**
     * 处理Excel文件上传和导入
     */
    @PostMapping("/import")
    @ResponseBody
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ImportResult("请选择要上传的文件", 0, 0, new ArrayList<>()));
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                return ResponseEntity.badRequest().body(new ImportResult("请上传Excel文件(.xlsx或.xls格式)", 0, 0, new ArrayList<>()));
            }

            List<PartyMember> importedMembers = parseExcelFile(file);
            int successCount = 0;
            int errorCount = 0;
            List<String> errors = new ArrayList<>();

            for (PartyMember member : importedMembers) {
                try {
                    // 检查是否已存在相同身份证号的党员
                    if (member.getIdCardNumber() != null && !member.getIdCardNumber().isEmpty()) {
                        if (partyMemberService.findByIdCardNumber(member.getIdCardNumber()).isPresent()) {
                            errors.add("身份证号 " + member.getIdCardNumber() + " 已存在，跳过导入");
                            errorCount++;
                            continue;
                        }
                    }
                    
                    partyMemberService.savePartyMember(member);
                    successCount++;
                } catch (Exception e) {
                    logger.error("保存党员信息失败: {}", member.getName(), e);
                    errors.add("保存 " + member.getName() + " 的信息时出错: " + e.getMessage());
                    errorCount++;
                }
            }

            String message = String.format("导入完成！成功导入 %d 条记录，失败 %d 条记录", successCount, errorCount);
            
            return ResponseEntity.ok().body(new ImportResult(message, successCount, errorCount, errors));

        } catch (Exception e) {
            logger.error("Excel导入过程中发生错误", e);
            return ResponseEntity.internalServerError().body(new ImportResult("导入失败: " + e.getMessage(), 0, 0, new ArrayList<>()));
        }
    }

    /**
     * 解析Excel文件
     */
    private List<PartyMember> parseExcelFile(MultipartFile file) throws IOException {
        List<PartyMember> members = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            
            // 跳过标题行，从第二行开始读取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                PartyMember member = parseRowToPartyMember(row);
                if (member != null && member.getName() != null && !member.getName().trim().isEmpty()) {
                    members.add(member);
                }
            }
        }
        
        return members;
    }

    /**
     * 将Excel行数据转换为PartyMember对象
     */
    private PartyMember parseRowToPartyMember(Row row) {
        try {
            PartyMember member = new PartyMember();
            
            // 根据Excel列的顺序映射字段
            // 假设Excel列顺序为：序号、姓名、性别、民族、政治面貌、入党时间、身份证号、出生日期、籍贯、现居住地、联系电话、入学时间、学历、专业、年级、组织
            
            member.setSerialNumber(getCellValueAsString(row.getCell(0))); // 序号
            member.setName(getCellValueAsString(row.getCell(1))); // 姓名
            member.setGender(getCellValueAsString(row.getCell(2))); // 性别
            member.setEthnicGroup(getCellValueAsString(row.getCell(3))); // 民族
            member.setPoliticalStatus(getCellValueAsString(row.getCell(4))); // 政治面貌
            member.setJoinDate(parseDate(getCellValueAsString(row.getCell(5)))); // 入党时间
            member.setIdCardNumber(getCellValueAsString(row.getCell(6))); // 身份证号
            member.setBirthDate(parseDate(getCellValueAsString(row.getCell(7)))); // 出生日期
            member.setBirthplace(getCellValueAsString(row.getCell(8))); // 籍贯
            member.setResidence(getCellValueAsString(row.getCell(9))); // 现居住地
            member.setPhoneNumber(getCellValueAsString(row.getCell(10))); // 联系电话
            member.setAdmissionDate(parseDate(getCellValueAsString(row.getCell(11)))); // 入学时间
            member.setDegree(getCellValueAsString(row.getCell(12))); // 学历
            member.setMajor(getCellValueAsString(row.getCell(13))); // 专业
            member.setClassOfYear(getCellValueAsString(row.getCell(14))); // 年级
            member.setOrganization(getCellValueAsString(row.getCell(15))); // 组织
            
            return member;
        } catch (Exception e) {
            logger.error("解析Excel行数据时出错", e);
            return null;
        }
    }

    /**
     * 获取单元格值作为字符串
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 处理数字类型，避免科学计数法
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * 解析日期字符串
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        // 尝试多种日期格式
        String[] patterns = {
            "yyyy-MM-dd",
            "yyyy/MM/dd", 
            "yyyy年MM月dd日",
            "MM/dd/yyyy",
            "dd/MM/yyyy"
        };
        
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException e) {
                // 继续尝试下一个格式
            }
        }
        
        logger.warn("无法解析日期: {}", dateStr);
        return null;
    }

    /**
     * 导入结果类
     */
    public static class ImportResult {
        private String message;
        private int successCount;
        private int errorCount;
        private List<String> errors;

        public ImportResult(String message, int successCount, int errorCount, List<String> errors) {
            this.message = message;
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.errors = errors;
        }

        // Getters
        public String getMessage() { return message; }
        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
        public List<String> getErrors() { return errors; }
    }
}
