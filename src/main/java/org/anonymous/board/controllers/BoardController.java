package org.anonymous.board.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    /**
     * 게시판 설정
     * 단일 조회
     *
     * @param bid
     * @return
     */
    @GetMapping("/config/{bid}")
    public JSONData config(@PathVariable("bid") String bid) {

        return new JSONData();
    }

    /**
     * 게시글
     * 등록 & 수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public JSONData save() {

        return new JSONData();
    }

    /**
     * 게시글
     * 단일 조회
     * 게시글 조회, 수정시 기초 데이터 활용 (프론트)
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return new JSONData();
    }

    /**
     * 게시글
     * 목록 조회
     *
     * @param bid
     * @return
     */
    @GetMapping("/list/{bid}")
    public JSONData list(@PathVariable("bid") String bid) {

        return new JSONData();
    }

    /**
     * 게시글
     * 단일 삭제
     *
     * 후속 처리로 게시글 반환
     *
     * @param seq
     * @return
     */
    @DeleteMapping("/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {

        return new JSONData();
    }
}