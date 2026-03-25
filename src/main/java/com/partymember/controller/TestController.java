package com.partymember.controller;

import com.partymember.entity.PartyMember;
import com.partymember.service.PartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    
    @Autowired
    private PartyMemberService partyMemberService;
    
    @GetMapping("/test/data")
    public String testData() {
        try {
            List<PartyMember> members = partyMemberService.getAllPartyMembers();
            return "数据库连接成功！党员总数: " + members.size() + 
                   "\n前3个党员姓名: " + 
                   (members.size() > 0 ? members.get(0).getName() : "无") +
                   (members.size() > 1 ? ", " + members.get(1).getName() : "") +
                   (members.size() > 2 ? ", " + members.get(2).getName() : "");
        } catch (Exception e) {
            return "数据库连接失败: " + e.getMessage();
        }
    }
}