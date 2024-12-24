package com.aivle.mini7.controller;

import com.aivle.mini7.dto.LogDto;
import com.aivle.mini7.service.LogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final LogService logService;

    // Admin 페이지
    @GetMapping("")
    public ModelAndView index(Pageable pageable, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");

        if (isLoggedIn == null || !isLoggedIn) {
            // 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
            return new ModelAndView("redirect:/login");
        }

        // 인증된 사용자만 admin 페이지 렌더링
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/index");
        Page<LogDto.ResponseList> logList = logService.getLogList(pageable);
        mv.addObject("logList", logList);

        return mv;
    }
}
