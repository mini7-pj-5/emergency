package com.aivle.mini7.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardPostResponse {
    private Long id;             // 게시글 ID
    private String hospitalName; // 병원 이름
    private String category;     // 카테고리
    private String content;      // 내용
    private String fileName;     // 첨부 파일명
    private boolean deleteRequested; // 삭제 요청 여부
}

