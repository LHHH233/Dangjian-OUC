package com.partymember.controller;

import com.partymember.entity.PartyMember;
import com.partymember.service.PartyMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class PartyMemberController {

    private static final Logger logger = LoggerFactory.getLogger(PartyMemberController.class);

    @Autowired
    private PartyMemberService partyMemberService;
    
    /**
     * 首页 - 重定向到查询页面
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/query";
    }
    
    /**
     * 党员查询页面
     */
    @GetMapping("/query")
    public String queryPage() {
        return "query";
    }
    
    /**
     * 处理查询请求
     */
    @PostMapping("/query")
    public String query(@RequestParam String name, 
                       @RequestParam String idCardNumber, 
                       Model model) {
        Optional<PartyMember> member = partyMemberService.findByNameAndIdCard(name, idCardNumber);
        if (member.isPresent()) {
            model.addAttribute("member", member.get());
            return "member-details";
        } else {
            model.addAttribute("error", "未找到匹配的党员信息");
            return "query";
        }
    }
    
    
    /**
     * 管理员首页 - 重定向到新的仪表板
     */
    @GetMapping("/admin/members")
    public String adminHome(Model model) {
        List<PartyMember> members = partyMemberService.getAllPartyMembers();
        model.addAttribute("members", members);
        return "admin-home";
    }
    
    /**
     * 添加党员页面
     */
    @GetMapping("/admin/add")
    public String addMemberPage(Model model) {
        model.addAttribute("member", new PartyMember());
        return "add-member";
    }
    
    /**
     * 处理添加党员请求
     */
    @PostMapping("/admin/add")
    public String addMember(@ModelAttribute PartyMember member) {
        partyMemberService.savePartyMember(member);
        return "redirect:/admin/members";
    }
    
    /**
     * 编辑党员页面 - 显示所有党员信息并支持编辑和打印证明
     */
    @GetMapping("/admin/edit")
    public String editMemberPage(Model model) {
        List<PartyMember> members = partyMemberService.getAllPartyMembers();
        model.addAttribute("members", members);
        return "edit-member";
    }
    
    /**
     * 编辑特定党员页面
     */
    @GetMapping("/admin/edit/{id}")
    public String editSpecificMemberPage(@PathVariable Long id, Model model) {
        Optional<PartyMember> member = partyMemberService.getPartyMemberById(id);
        if (member.isPresent()) {
            model.addAttribute("member", member.get());
            return "edit-specific-member";
        }
        return "redirect:/admin/edit";
    }
    
    /**
     * 处理编辑党员请求
     */
    @PostMapping("/admin/edit")
    public String editMember(@ModelAttribute PartyMember member) {
        partyMemberService.savePartyMember(member);
        return "redirect:/admin/members";
    }
    
    /**
     * 删除党员
     */
    @GetMapping("/admin/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        partyMemberService.deletePartyMember(id);
        return "redirect:/admin/members";
    }
    
    /**
     * 搜索党员
     */
    @GetMapping("/admin/search")
    public String searchMembers(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String idCardNumber,
                               Model model) {
        List<PartyMember> members;
        if ((name != null && !name.trim().isEmpty()) || (idCardNumber != null && !idCardNumber.trim().isEmpty())) {
            members = partyMemberService.searchByNameAndIdCard(name, idCardNumber);
        } else {
            members = partyMemberService.getAllPartyMembers();
        }
        model.addAttribute("members", members);
        model.addAttribute("searchName", name);
        model.addAttribute("searchIdCard", idCardNumber);
        return "admin-home";
    }
    
    /**
     * 生成并下载党员证明文档
     */
    @GetMapping("/proof/{id}")
    public ResponseEntity<byte[]> generateProof(@PathVariable Long id) {
        try {
            Optional<PartyMember> member = partyMemberService.getPartyMemberById(id);
            if (member.isPresent()) {
                byte[] documentBytes = partyMemberService.generateProofDocument(member.get());
                String memberName = member.get().getName() != null ? member.get().getName() : "未知";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
                headers.setContentDispositionFormData("attachment",
                    memberName + "_党员身份证明.docx");

                return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.error("生成证明文档失败, id={}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 从查询页面生成证明文档
     */
    @GetMapping("/proof/query")
    public ResponseEntity<byte[]> generateProofFromQuery(@RequestParam String name,
                                                        @RequestParam String idCardNumber) {
        try {
            logger.info("收到Word文档生成请求 - 姓名: {}, 身份证: {}", name, idCardNumber);

            Optional<PartyMember> member = partyMemberService.findByNameAndIdCard(name, idCardNumber);
            if (member.isPresent()) {
                logger.info("找到党员信息，开始生成Word文档");
                byte[] documentBytes = partyMemberService.generateProofDocument(member.get());
                logger.info("Word文档生成成功，大小: {} bytes", documentBytes.length);
                String memberName = member.get().getName() != null ? member.get().getName() : "未知";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
                headers.setContentDispositionFormData("attachment",
                    memberName + "_党员身份证明.docx");

                return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
            } else {
                logger.info("未找到匹配的党员信息");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.error("Word文档生成失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Word文档生成过程中发生未知错误", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 导出Excel文件
     */
    @PostMapping("/admin/export")
    public ResponseEntity<byte[]> exportToExcel() {
        try {
            List<PartyMember> members = partyMemberService.getAllPartyMembers();
            
            // 创建Excel工作簿
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("党员信息");
            
            // 创建标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"序号", "姓名", "性别", "民族", "政治面貌", "入党时间", "身份证号", "出生日期", 
                              "籍贯", "现居住地", "联系电话", "入学时间", "学历", "专业", "年级", "组织"};
            
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 填充数据
            int rowNum = 1;
            for (PartyMember member : members) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(member.getSerialNumber() != null ? member.getSerialNumber() : "");
                row.createCell(1).setCellValue(member.getName() != null ? member.getName() : "");
                row.createCell(2).setCellValue(member.getGender() != null ? member.getGender() : "");
                row.createCell(3).setCellValue(member.getEthnicGroup() != null ? member.getEthnicGroup() : "");
                row.createCell(4).setCellValue(member.getPoliticalStatus() != null ? member.getPoliticalStatus() : "");
                row.createCell(5).setCellValue(member.getJoinDate() != null ? member.getJoinDate().toString() : "");
                row.createCell(6).setCellValue(member.getIdCardNumber() != null ? member.getIdCardNumber() : "");
                row.createCell(7).setCellValue(member.getBirthDate() != null ? member.getBirthDate().toString() : "");
                row.createCell(8).setCellValue(member.getBirthplace() != null ? member.getBirthplace() : "");
                row.createCell(9).setCellValue(member.getResidence() != null ? member.getResidence() : "");
                row.createCell(10).setCellValue(member.getPhoneNumber() != null ? member.getPhoneNumber() : "");
                row.createCell(11).setCellValue(member.getAdmissionDate() != null ? member.getAdmissionDate().toString() : "");
                row.createCell(12).setCellValue(member.getDegree() != null ? member.getDegree() : "");
                row.createCell(13).setCellValue(member.getMajor() != null ? member.getMajor() : "");
                row.createCell(14).setCellValue(member.getClassOfYear() != null ? member.getClassOfYear() : "");
                row.createCell(15).setCellValue(member.getOrganization() != null ? member.getOrganization() : "");
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 转换为字节数组
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            responseHeaders.setContentDispositionFormData("attachment", "党员信息汇总表.xlsx");
            
            return new ResponseEntity<>(outputStream.toByteArray(), responseHeaders, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("导出Excel失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量删除党员
     */
    @PostMapping("/admin/batch-delete")
    public String batchDelete(@RequestParam("memberIds") List<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return "redirect:/admin/members?error=未选择任何党员";
        }
        try {
            for (Long id : memberIds) {
                partyMemberService.deletePartyMember(id);
            }
            return "redirect:/admin/members?message=批量删除成功";
        } catch (Exception e) {
            logger.error("批量删除失败", e);
            return "redirect:/admin/members?error=批量删除失败";
        }
    }

}
