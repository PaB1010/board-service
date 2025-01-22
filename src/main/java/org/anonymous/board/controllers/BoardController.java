package org.anonymous.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.board.validators.BoardValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final Utils utils;

    private final BoardValidator boardValidator;

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
    public JSONData save(@Valid @RequestBody RequestBoard form, Errors errors) {

        String mode = form.getMode();

        mode = StringUtils.hasText(mode) ? mode : "write";

        commonProcess(form.getBid(), mode);

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {

            throw new BadRequestException(utils.getErrorMessages(errors));
        }

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

        commonProcess(seq, "view");

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

        commonProcess(bid, "list");

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

        commonProcess(seq, "delete");

        return new JSONData();
    }

    /**
     * 게시글 번호로 공통 처리
     *
     * @param seq
     * @param mode
     */
    private void commonProcess(Long seq, String mode) {


    }

    /**
     * 게시판 아이디로 공통 처리
     *
     * Base Method
     *
     * @param bid
     * @param mode
     */
    private void commonProcess(String bid, String mode) {


    }


}