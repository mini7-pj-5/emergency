package com.aivle.mini7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FAQController {

    @GetMapping("/faq")
    public String faqPage(Model model) {
        // FAQ 데이터를 생성합니다.
        List<Faq> faqList = new ArrayList<>();
        faqList.add(new Faq("How to reset my password?", "Click on 'Forgot Password' and follow the instructions."));
        faqList.add(new Faq("How to contact support?", "You can reach us at support@aivle.com."));
        faqList.add(new Faq("Where can I find the documentation?", "Visit our documentation page at /docs."));

        // FAQ 데이터를 모델에 추가합니다.
        model.addAttribute("faqList", faqList);

        return "faq/faq"; // templates/faq/faq.html 파일 렌더링
    }

    // FAQ 데이터 클래스
    static class Faq {
        private String question;
        private String answer;

        public Faq(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
