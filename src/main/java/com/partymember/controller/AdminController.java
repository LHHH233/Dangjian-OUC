package com.partymember.controller;

import com.partymember.service.PartyMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private PartyMemberService partyMemberService;

    @GetMapping("/admin/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        return "admin-login";
    }

    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        try {
            List<com.partymember.entity.PartyMember> allMembers = partyMemberService.getAllPartyMembers();

            // 统计正式党员数量（精确匹配）
            long activeMembers = allMembers.stream()
                .filter(member -> "中共正式党员".equals(member.getPoliticalStatus()))
                .count();

            // 统计预备党员数量（精确匹配）
            long preparatoryMembers = allMembers.stream()
                .filter(member -> "中共预备党员".equals(member.getPoliticalStatus()))
                .count();

            // 统计组织数量
            long organizations = allMembers.stream()
                .filter(member -> member.getOrganization() != null && !member.getOrganization().trim().isEmpty())
                .map(com.partymember.entity.PartyMember::getOrganization)
                .distinct()
                .count();

            model.addAttribute("totalMembers", allMembers.size());
            model.addAttribute("activeMembers", activeMembers);
            model.addAttribute("preparatoryMembers", preparatoryMembers);
            model.addAttribute("organizations", organizations);

        } catch (Exception e) {
            logger.error("获取仪表盘数据失败", e);
            model.addAttribute("totalMembers", 0);
            model.addAttribute("activeMembers", 0);
            model.addAttribute("preparatoryMembers", 0);
            model.addAttribute("organizations", 0);
        }

        return "admin-dashboard";
    }
}
