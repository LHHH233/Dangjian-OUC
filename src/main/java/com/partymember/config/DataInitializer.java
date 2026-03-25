package com.partymember.config;

import com.partymember.entity.PartyMember;
import com.partymember.repository.PartyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private PartyMemberRepository partyMemberRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据
        if (partyMemberRepository.count() == 0) {
            // 创建示例党员数据
            PartyMember member1 = new PartyMember();
            member1.setSerialNumber("001");
            member1.setName("张三");
            member1.setGender("男");
            member1.setEthnicGroup("汉族");
            member1.setPoliticalStatus("中共党员");
            member1.setJoinDate(LocalDate.of(2020, 6, 15));
            member1.setIdCardNumber("110101199001011234");
            member1.setBirthDate(LocalDate.of(1990, 1, 1));
            member1.setBirthplace("北京市东城区");
            member1.setResidence("北京市朝阳区某某街道123号");
            member1.setPhoneNumber("13800138001");
            member1.setAdmissionDate(LocalDate.of(2018, 9, 1));
            member1.setDegree("本科");
            member1.setMajor("计算机科学与技术");
            member1.setClassOfYear("2018级");
            member1.setOrganization("计算机学院学生党支部");
            
            PartyMember member2 = new PartyMember();
            member2.setSerialNumber("002");
            member2.setName("李四");
            member2.setGender("女");
            member2.setEthnicGroup("汉族");
            member2.setPoliticalStatus("中共党员");
            member2.setJoinDate(LocalDate.of(2021, 3, 20));
            member2.setIdCardNumber("110101199205152345");
            member2.setBirthDate(LocalDate.of(1992, 5, 15));
            member2.setBirthplace("北京市西城区");
            member2.setResidence("北京市海淀区某某小区456号");
            member2.setPhoneNumber("13800138002");
            member2.setAdmissionDate(LocalDate.of(2019, 9, 1));
            member2.setDegree("硕士");
            member2.setMajor("软件工程");
            member2.setClassOfYear("2019级");
            member2.setOrganization("软件学院学生党支部");
            
            PartyMember member3 = new PartyMember();
            member3.setSerialNumber("003");
            member3.setName("王五");
            member3.setGender("男");
            member3.setEthnicGroup("回族");
            member3.setPoliticalStatus("中共预备党员");
            member3.setJoinDate(LocalDate.of(2022, 12, 1));
            member3.setIdCardNumber("110101199503103456");
            member3.setBirthDate(LocalDate.of(1995, 3, 10));
            member3.setBirthplace("北京市海淀区");
            member3.setResidence("北京市丰台区某某路789号");
            member3.setPhoneNumber("13800138003");
            member3.setAdmissionDate(LocalDate.of(2020, 9, 1));
            member3.setDegree("本科");
            member3.setMajor("信息管理与信息系统");
            member3.setClassOfYear("2020级");
            member3.setOrganization("管理学院学生党支部");
            
            // 保存到数据库
            partyMemberRepository.save(member1);
            partyMemberRepository.save(member2);
            partyMemberRepository.save(member3);
            
            System.out.println("示例数据初始化完成！");
        }
    }
}




