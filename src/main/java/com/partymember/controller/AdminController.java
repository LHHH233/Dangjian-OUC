package com.partymember.controller;

import com.partymember.service.PartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    
    @Autowired
    private PartyMemberService partyMemberService;
    
    @GetMapping("/admin/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        return "admin-login";
    }
    
    @PostMapping("/admin/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // 登录逻辑由Spring Security处理
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        try {
            List<com.partymember.entity.PartyMember> allMembers = partyMemberService.getAllPartyMembers();
            
            // 统计正式党员数量
            long activeMembers = allMembers.stream()
                .filter(member -> member.getPoliticalStatus() != null && 
                        member.getPoliticalStatus().contains("正式党员"))
                .count();
            
            // 统计预备党员数量
            long preparatoryMembers = allMembers.stream()
                .filter(member -> member.getPoliticalStatus() != null && 
                        member.getPoliticalStatus().contains("预备党员"))
                .count();
            
            // 统计组织数量
            long organizations = allMembers.stream()
                .filter(member -> member.getOrganization() != null && !member.getOrganization().trim().isEmpty())
                .map(com.partymember.entity.PartyMember::getOrganization)
                .distinct()
                .count();
            
            // 如果组织数量为0，至少显示1（表示有数据但组织信息不完整）
            if (organizations == 0 && allMembers.size() > 0) {
                organizations = 1;
            }
            
            model.addAttribute("totalMembers", allMembers.size());
            model.addAttribute("activeMembers", activeMembers);
            model.addAttribute("preparatoryMembers", preparatoryMembers);
            model.addAttribute("organizations", organizations);
            
        } catch (Exception e) {
            // 如果获取数据失败，设置默认值
            model.addAttribute("totalMembers", 0);
            model.addAttribute("activeMembers", 0);
            model.addAttribute("preparatoryMembers", 0);
            model.addAttribute("organizations", 0);
        }
        
        return "admin-dashboard";
    }
}

