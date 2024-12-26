package com.aivle.mini7.controller;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    // 문의 페이지
    @GetMapping("/contact")
    public String contactPage() {
        // "faq/contact.html" 등을 반환 (뷰 템플릿 경로에 맞게 수정)
        return "faq/contact";
    }

    // 메일 전송
    @PostMapping("/send-email")
    public String sendEmail(
            @RequestParam String fromEmail,
            @RequestParam String subject,
            @RequestParam String body) {

        try {
            // 1) MimeMessage 생성
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 2) MimeMessageHelper로 설정
            //    두 번째 파라미터(false 또는 true)는 멀티파트/HTML 여부, 인코딩 지정 등
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 3) 발신자, 수신자 모두 같은 네이버 주소
            helper.setFrom("shichj1@naver.com");
            helper.setTo("ktaivle5@naver.com");

            // 4) 제목
            helper.setSubject(subject);

            // 5) Reply-To를 사용자가 입력한 이메일로 설정 → 받는 사람이 'Reply' 누르면 fromEmail로 회신
            helper.setReplyTo(fromEmail);

            // 6) 본문: 사용자 이메일을 함께 기재
            String finalBody = "사용자가 입력한 메일: " + fromEmail + "\n\n" + body;
            helper.setText(finalBody);

            // 7) 전송
            mailSender.send(mimeMessage);

            // 성공 시
            return "redirect:/contact?success";

        } catch (Exception e) {
            e.printStackTrace();
            // 실패 시
            return "redirect:/contact?error";
        }
    }
}
