package com.partymember.controller;

import com.partymember.entity.PartyMember;
import com.partymember.service.PartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class SimplePdfController {
    
    @Autowired
    private PartyMemberService partyMemberService;
    
    @GetMapping("/simple-proof/{id}")
    public ResponseEntity<byte[]> generateSimpleProof(@PathVariable Long id) {
        try {
            System.out.println("收到PDF生成请求 - ID: " + id);
            
            Optional<PartyMember> member = partyMemberService.getPartyMemberById(id);
            if (!member.isPresent()) {
                System.out.println("未找到党员信息");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            System.out.println("找到党员信息: " + member.get().getName());
            
            // 创建Word文档
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            try (org.apache.poi.xwpf.usermodel.XWPFDocument document = new org.apache.poi.xwpf.usermodel.XWPFDocument()) {
                
                // 创建标题段落
                org.apache.poi.xwpf.usermodel.XWPFParagraph titlePara = document.createParagraph();
                titlePara.setAlignment(org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER);
                org.apache.poi.xwpf.usermodel.XWPFRun titleRun = titlePara.createRun();
                titleRun.setText("党员身份证明");
                titleRun.setBold(true);
                titleRun.setFontSize(22);
                titleRun.setFontFamily("宋体");

                // 创建空行
                document.createParagraph();

                // 创建主要内容段落
                org.apache.poi.xwpf.usermodel.XWPFParagraph mainPara = document.createParagraph();
                mainPara.setAlignment(org.apache.poi.xwpf.usermodel.ParagraphAlignment.LEFT);
                mainPara.setIndentationFirstLine(600); // 首行缩进2个字符
                
                org.apache.poi.xwpf.usermodel.XWPFRun mainRun = mainPara.createRun();
                mainRun.setFontFamily("宋体");
                mainRun.setFontSize(14);
                
                String name = member.get().getName() != null ? member.get().getName() : "XX";
                String gender = member.get().getGender() != null ? member.get().getGender() : "男/女";
                String ethnicGroup = member.get().getEthnicGroup() != null ? member.get().getEthnicGroup() : "汉";
                String idCard = member.get().getIdCardNumber() != null ? member.get().getIdCardNumber() : "XXXXXXXXXXXXXXXXXX";
                String classOfYear = member.get().getClassOfYear() != null ? member.get().getClassOfYear() : "XX";
                String degree = member.get().getDegree() != null ? member.get().getDegree() : "硕士/博士研究生";
                String birthDate = member.get().getBirthDate() != null ? 
                    member.get().getBirthDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")) : "____年__月__日";
                String joinDate = member.get().getJoinDate() != null ? 
                    member.get().getJoinDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")) : "____年__月__日";
                String politicalStatus = member.get().getPoliticalStatus() != null ? member.get().getPoliticalStatus() : "中共正式/预备党员";
                
                String mainContent = String.format(
                    "兹证明%s同志，%s，%s族，%s生，身份证号：%s，" +
                    "系中国海洋大学三亚海洋研究院%s级计算机专业%s。" +
                    "经查，该同志于%s加入中国共产党，现为%s。",
                    name, gender, ethnicGroup, birthDate, idCard, classOfYear, degree, joinDate, politicalStatus
                );
                
                mainRun.setText(mainContent);

                // 创建附加内容段落
                org.apache.poi.xwpf.usermodel.XWPFParagraph secondaryPara = document.createParagraph();
                secondaryPara.setAlignment(org.apache.poi.xwpf.usermodel.ParagraphAlignment.LEFT);
                secondaryPara.setIndentationFirstLine(600);
                
                org.apache.poi.xwpf.usermodel.XWPFRun secondaryRun = secondaryPara.createRun();
                secondaryRun.setFontFamily("宋体");
                secondaryRun.setFontSize(14);
                secondaryRun.setText("该同志能够认真履行党员义务，积极参加组织生活，按时足额缴纳党费。");

                // 创建"特此证明"段落
                org.apache.poi.xwpf.usermodel.XWPFParagraph proofPara = document.createParagraph();
                proofPara.setAlignment(org.apache.poi.xwpf.usermodel.ParagraphAlignment.LEFT);
                proofPara.setIndentationFirstLine(600);
                
                org.apache.poi.xwpf.usermodel.XWPFRun proofRun = proofPara.createRun();
                proofRun.setFontFamily("宋体");
                proofRun.setFontSize(14);
                proofRun.setText("特此证明。");

                // 创建多个空行
                for (int i = 0; i < 3; i++) {
                    document.createParagraph();
                }

                // 创建签名段落
                org.apache.poi.xwpf.usermodel.XWPFParagraph signingPara = document.createParagraph();
                signingPara.setAlignment(org.apache.poi.xwpf.usermodel.ParagraphAlignment.RIGHT);
                
                org.apache.poi.xwpf.usermodel.XWPFRun signingRun = signingPara.createRun();
                signingRun.setFontFamily("宋体");
                signingRun.setFontSize(14);
                signingRun.setText("中共中国海洋大学三亚海洋研究院委员会");

                // 创建日期段落
                org.apache.poi.xwpf.usermodel.XWPFParagraph datePara = document.createParagraph();
                datePara.setAlignment(org.apache.poi.xwpf.usermodel.ParagraphAlignment.RIGHT);
                
                org.apache.poi.xwpf.usermodel.XWPFRun dateRun = datePara.createRun();
                dateRun.setFontFamily("宋体");
                dateRun.setFontSize(14);
                dateRun.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

                // 保存文档
                document.write(outputStream);
            }
            
            byte[] wordBytes = outputStream.toByteArray();
            System.out.println("Word文档生成成功，大小: " + wordBytes.length + " bytes");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("attachment", member.get().getName() + "_党员身份证明.docx");
            
            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("Word文档生成失败: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
