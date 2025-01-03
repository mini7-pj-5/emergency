package com.aivle.mini7.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    // application.properties 또는 application.yml에 설정된 값 주입
    @Value("${naver.client.id}")
    private String CLIENT_ID;

    @Value("${naver.client.secret}")
    private String CLIENT_SECRET;

    @Value("${naver.redirect.uri}")
    private String REDIRECT_URI;

    // 로그인 페이지
    @GetMapping
    public String loginPage(HttpSession session, Model model) {
        // 랜덤한 state 생성
        String state = UUID.randomUUID().toString();
        session.setAttribute("oauth_state", state);
        model.addAttribute("state", state);
        model.addAttribute("clientId", CLIENT_ID);
        model.addAttribute("redirectUri", REDIRECT_URI);
        return "login/login"; // src/main/resources/templates/login/login.html
    }

    // 콜백 URL: http://localhost:8080/login/callback
    @GetMapping("/callback")
    public String naverCallback(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state,
            HttpSession session,
            Model model
    ) {
        if (code == null || code.isEmpty()) {
            // 'code' 파라미터가 없을 경우 에러 처리
            return "redirect:/error?message=Code_Not_Present";
        }

        // 'state' 검증
        String sessionState = (String) session.getAttribute("oauth_state");
        if (sessionState == null || !sessionState.equals(state)) {
            // state 값이 일치하지 않을 경우 에러 처리
            return "redirect:/error?message=Invalid_State";
        }

        RestTemplate restTemplate = new RestTemplate();

        // 1. Access Token 요청
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";

        UriComponentsBuilder tokenBuilder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("code", code)
                .queryParam("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> tokenRequest = new HttpEntity<>(null, headers);

        ResponseEntity<Map> tokenResponse;
        try {
            tokenResponse = restTemplate.exchange(
                    tokenBuilder.toUriString(),
                    HttpMethod.POST,
                    tokenRequest,
                    Map.class
            );
        } catch (Exception e) {
            // 토큰 요청 실패 시 에러 처리
            e.printStackTrace();
            return "redirect:/error?message=Token_Request_Failed";
        }

        if (tokenResponse.getStatusCode() == HttpStatus.OK && tokenResponse.getBody() != null) {
            String accessToken = (String) tokenResponse.getBody().get("access_token");

            if (accessToken != null && !accessToken.isEmpty()) {
                // 2. 사용자 정보 요청
                String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
                HttpHeaders userInfoHeaders = new HttpHeaders();
                userInfoHeaders.set("Authorization", "Bearer " + accessToken);

                HttpEntity<String> userInfoRequest = new HttpEntity<>("", userInfoHeaders);

                ResponseEntity<Map> userInfoResponse;
                try {
                    userInfoResponse = restTemplate.exchange(
                            userInfoUrl,
                            HttpMethod.GET,
                            userInfoRequest,
                            Map.class
                    );
                } catch (Exception e) {
                    // 사용자 정보 요청 실패 시 에러 처리
                    e.printStackTrace();
                    return "redirect:/error?message=UserInfo_Request_Failed";
                }

                if (userInfoResponse.getStatusCode() == HttpStatus.OK && userInfoResponse.getBody() != null) {
                    Map<String, Object> userInfo = userInfoResponse.getBody();

                    // 인증 성공 플래그 저장
                    session.setAttribute("isLoggedIn", true); // 인증 플래그 저장
                    session.setAttribute("user", userInfo); // 사용자 정보 저장

                    // 인증 후 관리자 페이지로 리다이렉트
                    return "redirect:/admin";
                }

            }
        }

        // 실패 시: 에러 페이지로 리다이렉트
        return "redirect:/error?message=Token_Exchange_Failed";
    }
}