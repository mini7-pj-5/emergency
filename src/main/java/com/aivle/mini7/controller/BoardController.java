package com.aivle.mini7.controller;

import com.aivle.mini7.client.dto.BoardPostResponse;
import com.aivle.mini7.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시판 페이지
     * @return 게시판 화면
     */
    @GetMapping("/board")
    public ModelAndView board(@RequestParam(value = "search", required = false) String search) {
        List<BoardPostResponse> boardPosts;

        if (search != null && !search.isEmpty()) {
            boardPosts = boardService.searchPostsByTitleOrHospital(search);
        } else {
            boardPosts = boardService.getAllPosts();
        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName("board/board");
        mv.addObject("boardPosts", boardPosts);

        return mv;
    }

    /**
     * 글쓰기 페이지
     * @return 글쓰기 화면
     */
    @GetMapping("/board/write")
    public String writePage() {
        return "board/board_write";
    }

    /**
     * 게시글 작성 처리
     * @param hospitalName 병원 이름
     * @param category 카테고리
     * @param content 게시글 내용
     * @param file 첨부 파일
     * @return 게시판 페이지로 리다이렉트
     */
    @PostMapping("/board/submit")
    public String submitPost(
            @RequestParam("hospital_name") String hospitalName,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        log.info("New post - Hospital: {}, Category: {}, Content: {}", hospitalName, category, content);

        // 게시글 저장
        boardService.savePost(hospitalName, category, content, file);

        return "redirect:/board";
    }
}
