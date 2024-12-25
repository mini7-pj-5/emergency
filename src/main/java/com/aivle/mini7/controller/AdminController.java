package com.aivle.mini7.controller;

import com.aivle.mini7.dto.LogDto;
import com.aivle.mini7.service.LogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final LogService logService;

    // Admin 페이지
    @GetMapping("")
    public String index(
            @PageableDefault(page = 0, size = 10, sort = "datetime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "search", required = false) String search,
            HttpSession session,
            Model model) {

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");

        if (isLoggedIn == null || !isLoggedIn) {
            // 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        Page<LogDto.ResponseList> logList;
        if (search != null && !search.isEmpty()) {
            logList = logService.searchLogs(search, pageable);
        } else {
            logList = logService.getLogList(pageable);
        }

        model.addAttribute("logList", logList);
        model.addAttribute("currentPage", logList.getNumber());
        model.addAttribute("totalPages", logList.getTotalPages());
        model.addAttribute("sort", pageable.getSort());
        model.addAttribute("search", search);

        return "admin/index";
    }
}
