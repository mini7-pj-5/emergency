package com.aivle.mini7.service;

import com.aivle.mini7.client.dto.BoardPostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    private final List<BoardPostResponse> posts = new ArrayList<>();
    private Long nextId = 1L; // ID 자동 증가 변수

    // 게시글 목록 반환
    public List<BoardPostResponse> getAllPosts() {
        return posts;
    }

    // 게시글 검색
    public List<BoardPostResponse> searchPostsByTitleOrHospital(String keyword) {
        return posts.stream()
                .filter(post -> post.getHospitalName().contains(keyword)
                        || post.getContent().contains(keyword))
                .collect(Collectors.toList());
    }

    // 게시글 저장
    public void savePost(String hospitalName, String category, String content, MultipartFile file) {
        BoardPostResponse post = new BoardPostResponse(
                nextId++, // 자동 증가 ID 할당
                hospitalName,
                category,
                content,
                (file != null) ? file.getOriginalFilename() : null,
                false // 초기값: 삭제 요청 상태 아님
        );
        posts.add(post);
        log.info("Post saved: {}", post);
    }

    public BoardPostResponse getPostById(Long id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 게시글 삭제 요청
    public void requestDelete(Long id) {
        posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .ifPresent(p -> p.setDeleteRequested(true));
        log.info("Delete requested for post ID: {}", id);
    }
}
