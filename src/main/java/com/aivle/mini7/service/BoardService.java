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

    // 게시글 목록 반환
    public List<BoardPostResponse> getAllPosts() {
        return posts;
    }
    public List<BoardPostResponse> searchPostsByTitleOrHospital(String keyword) {
        return posts.stream()
                .filter(post -> post.getHospitalName().contains(keyword)
                        || post.getContent().contains(keyword))
                .collect(Collectors.toList());
    }



    // 게시글 저장
    public void savePost(String hospitalName, String category, String content, MultipartFile file) {
        BoardPostResponse post = new BoardPostResponse(
                hospitalName,
                category,
                content,
                file != null ? file.getOriginalFilename() : null
        );
        posts.add(post);
        log.info("Post saved: {}", post);
    }
}
