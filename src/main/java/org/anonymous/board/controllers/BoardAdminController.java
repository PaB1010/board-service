package org.anonymous.board.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardAdminController {

    /**
     * 게시판 설정
     * 등록 & 수정 처리
     *
     * @return
     */
    @PostMapping("/config")
    public JSONData save() {

        return new JSONData();
    }

    /**
     * 게시판 설정
     * 목록 조회
     *
     * @return
     */
    @GetMapping("/config")
    public JSONData list() {

        return new JSONData();
    }

    /**
     * 게시판 설정
     * 단일 | 목록
     * 일괄 수정
     *
     * @return
     */
    @PatchMapping("/config")
    public JSONData update() {

        return new JSONData();
    }

    /**
     * 게시판 설정
     * 단일 | 목록
     * 삭제처리
     *
     * @param bids
     * @return
     */
    @DeleteMapping("/config")
    public JSONData delete(@RequestParam("bid") List<String> bids) {

        return new JSONData();
    }
}