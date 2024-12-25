package com.aivle.mini7.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String redirectToIndex() {
        log.info("Redirecting to /index");
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        log.info("Rendering index page");
        return "index"; // index.html 렌더링
    }
}
