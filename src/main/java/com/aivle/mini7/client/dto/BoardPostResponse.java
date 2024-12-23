package com.aivle.mini7.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardPostResponse {
    private String hospitalName;
    private String category;
    private String content;
    private String fileName; // 파일명 저장
}
