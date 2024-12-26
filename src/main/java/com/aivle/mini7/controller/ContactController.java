package com.aivle.mini7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 문의하기 페이지 표시
     *
     * @return contact.html 템플릿 반환
     */
    @GetMapping("/contact")
    public String showContactPage() {
        return "contact"; // src/main/resources/templates/contact.html
    }

    /**
     * 사용자의 문의 내용을 이메일로 전송
     *
     * @param title   문의 제목
     * @param message 문의 내용
     * @return 성공/실패 메시지 반환
     */
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam("title") String title,
            @RequestParam("message") String message) {

        // 이메일 정보 설정
        String toAddress = "ktaivle5@naver.com";
        String fromAddress = "ktaivle5@naver.com";
        String subject = "새로운 문의: " + title;
        String body = "문의 내용:\n\n" + message;

        try {
            // 이메일 메시지 작성
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toAddress);
            mailMessage.setFrom(fromAddress);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);

            // 이메일 전송
            mailSender.send(mailMessage);

            // 성공 메시지 반환
            return ResponseEntity.ok("문의 전송 성공");
        } catch (Exception e) {
            e.printStackTrace();

            // 실패 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문의 전송 실패");
        }
    }
}
