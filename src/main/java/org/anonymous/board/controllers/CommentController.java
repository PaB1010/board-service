package org.anonymous.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.board.validators.CommentValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentValidator validator;

    private final Utils utils;

    /**
     * 댓글
     * 작성 & 수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public JSONData save(@RequestBody @Valid RequestComment form, Errors errors) {

        validator.validate(form, errors);

        if (errors.hasErrors()) {

            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return new JSONData();
    }

    /**
     * 댓글
     * 단일 조회
     *
     * 수정시 기초 데이터 활용 (프론트)
     * @param seq : 댓글
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return new JSONData();
    }

    /**
     * 댓글
     * 목록 조회
     *
     * @param seq : 게시글
     * @return
     */
    @GetMapping("/list/{seq}")
    public JSONData list(@PathVariable("seq") Long seq) {

        return new JSONData();
    }

    /**
     * 댓글
     * 단일 삭제
     *
     * @param seq : 댓글
     * @return
     */
    @DeleteMapping("/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {

        return new JSONData();
    }
}