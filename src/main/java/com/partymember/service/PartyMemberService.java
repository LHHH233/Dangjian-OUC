package com.partymember.service;

import com.partymember.entity.PartyMember;
import com.partymember.repository.PartyMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PartyMemberService {

    private static final Logger logger = LoggerFactory.getLogger(PartyMemberService.class);
    

    @Autowired
    private PartyMemberRepository partyMemberRepository;

    /**
     * Save or update a party member's information.
     * @param partyMember The PartyMember entity to save.
     * @return The saved PartyMember entity.
     */
    public PartyMember savePartyMember(PartyMember partyMember) {
        return partyMemberRepository.save(partyMember);
    }

    /**
     * Get all party members.
     * @return A list of all PartyMember entities.
     */
    public List<PartyMember> getAllPartyMembers() {
        return partyMemberRepository.findAll();
    }

    /**
     * Find a party member by their database ID.
     * @param id The database ID.
     * @return An Optional containing the PartyMember if found.
     */
    public Optional<PartyMember> getPartyMemberById(Long id) {
        return partyMemberRepository.findById(id);
    }

    /**
     * Find a party member by their exact name and ID card number.
     * @param name The member's name.
     * @param idCardNumber The member's ID card number.
     * @return An Optional containing the PartyMember if found.
     */
    public Optional<PartyMember> findByNameAndIdCard(String name, String idCardNumber) {
        return partyMemberRepository.findByNameAndIdCardNumber(name, idCardNumber);
    }

    /**
     * Find a party member by their exact ID card number.
     * @param idCardNumber The member's ID card number.
     * @return An Optional containing the PartyMember if found.
     */
    public Optional<PartyMember> findByIdCardNumber(String idCardNumber) {
        return partyMemberRepository.findByIdCardNumber(idCardNumber);
    }

    /**
     * Find all members belonging to a specific organization.
     * @param organization The name of the organization.
     * @return A list of matching PartyMember entities.
     */
    public List<PartyMember> findByOrganization(String organization) {
        return partyMemberRepository.findByOrganization(organization);
    }

    /**
     * Find all members with a specific political status.
     * @param politicalStatus The political status (e.g., "中共正式党员").
     * @return A list of matching PartyMember entities.
     */
    public List<PartyMember> findByPoliticalStatus(String politicalStatus) {
        return partyMemberRepository.findByPoliticalStatus(politicalStatus);
    }

    /**
     * Search for party members with a name containing the given string (case-insensitive).
     * @param name The search term for the name.
     * @return A list of matching PartyMember entities.
     */
    public List<PartyMember> searchByName(String name) {
        return partyMemberRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search for party members with names and ID card numbers containing the given strings.
     * @param name The search term for the name.
     * @param idCardNumber The search term for the ID card number.
     * @return A list of matching PartyMember entities.
     */
    public List<PartyMember> searchByNameAndIdCard(String name, String idCardNumber) {
        return partyMemberRepository.searchByNameAndIdCard(name, idCardNumber);
    }

    /**
     * Delete a party member by their database ID.
     * @param id The database ID of the member to delete.
     */
    public void deletePartyMember(Long id) {
        partyMemberRepository.deleteById(id);
    }

    /**
     * Generate a Word proof document for a given party member.
     * @param partyMember The PartyMember entity to generate the document for.
     * @return A byte array containing the Word file.
     * @throws IOException If the document cannot be created.
     */
    public byte[] generateProofDocument(PartyMember partyMember) throws IOException {
        logger.info("开始生成Word证明文档，党员姓名: {}", partyMember.getName());
        
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
            
            String mainContent = String.format(
                "兹证明%s同志，%s，%s族，%s生，身份证号：%s，" +
                "系中国海洋大学三亚海洋研究院%s级计算机专业%s。" +
                "经查，该同志于%s加入中国共产党，现为%s。",
                getOrDefault(partyMember.getName(), "XX"),
                getOrDefault(partyMember.getGender(), "男/女"),
                getOrDefault(partyMember.getEthnicGroup(), "汉"),
                formatDate(partyMember.getBirthDate(), "yyyy年MM月dd日"),
                getOrDefault(partyMember.getIdCardNumber(), "XXXXXXXXXXXXXXXXXX"),
                getOrDefault(partyMember.getClassOfYear(), "XX"),
                getOrDefault(partyMember.getDegree(), "硕士/博士研究生"),
                formatDate(partyMember.getJoinDate(), "yyyy年MM月dd日"),
                getOrDefault(partyMember.getPoliticalStatus(), "中共正式/预备党员")
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
            
            logger.info("Word文档生成完成，大小: {} bytes", outputStream.size());
        } catch (Exception e) {
            logger.error("Word生成过程中发生错误", e);
            throw new IOException("Word生成失败: " + e.getMessage(), e);
        }
        
        return outputStream.toByteArray();
    }
    
    // Helper Methods

    private String getOrDefault(String value, String defaultValue) {
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private String formatDate(LocalDate date, String format) {
        if (date == null) {
            return "____年__月__日";
        }
        return date.format(DateTimeFormatter.ofPattern(format));
    }
}