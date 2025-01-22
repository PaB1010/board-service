package org.anonymous.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.board.validators.BoardConfigValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardAdminController {

    private final Utils utils;

    private final BoardConfigValidator configValidator;

    /**
     * 게시판 설정
     * 등록 & 수정 처리
     *
     * @return
     */
    @PostMapping("/config")
    public JSONData save(@Valid @RequestBody RequestConfig form, Errors erros) {

        configValidator.validate(form, erros);

        if (erros.hasErrors()) {

            throw new BadRequestException(utils.getErrorMessages(erros));
        }

        return new JSONData();
    }

    /**
     * 게시판 설정
     * 목록 조회
     *
     * @return
     */
    @GetMapping("/config")
    public JSONData list(@ModelAttribute BoardConfigSearch search) {

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
    public JSONData update(@RequestBody List<RequestConfig> form) {

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