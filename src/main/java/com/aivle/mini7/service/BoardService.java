package com.aivle.mini7.service;

import com.aivle.mini7.client.dto.BoardPostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    private final List<BoardPostResponse> posts = new ArrayList<>();
    private Long nextId = 1L;
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public List<BoardPostResponse> getAllPosts() {
        return posts;
    }

    public List<BoardPostResponse> searchPostsByTitleOrHospital(String keyword) {
        return posts.stream()
                .filter(post -> post.getHospitalName().contains(keyword)
                        || post.getContent().contains(keyword))
                .collect(Collectors.toList());
    }

    public void savePost(String hospitalName, String category, String content, MultipartFile file) {
        String fileName = null;

        if (file != null && !file.isEmpty()) {
            try {
                File uploadDir = new File(fileStorageLocation.toString());
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
                file.transferTo(targetLocation);
                fileName = file.getOriginalFilename();
            } catch (IOException ex) {
                throw new RuntimeException("파일 저장에 실패했습니다: " + file.getOriginalFilename(), ex);
            }
        }

        BoardPostResponse post = new BoardPostResponse(
                nextId++,
                hospitalName,
                category,
                content,
                fileName,
                false
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

    public void requestDelete(Long id) {
        posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .ifPresent(p -> p.setDeleteRequested(true));
        log.info("Delete requested for post ID: {}", id);
    }
}
