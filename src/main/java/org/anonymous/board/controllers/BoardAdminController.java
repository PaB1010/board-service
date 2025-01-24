package org.anonymous.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.board.entities.Board;
import org.anonymous.board.services.configs.BoardConfigDeleteService;
import org.anonymous.board.services.configs.BoardConfigInfoService;
import org.anonymous.board.services.configs.BoardConfigUpdateService;
import org.anonymous.board.validators.BoardConfigValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardAdminController {

    private final Utils utils;

    private final BoardConfigInfoService infoService;

    private final BoardConfigValidator configValidator;

    private final BoardConfigUpdateService updateService;

    private final BoardConfigDeleteService deleteService;

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

        Board board = updateService.process(form);

        return new JSONData(board);
    }

    /**
     * 게시판 설정
     * 목록 조회
     *
     * @return
     */
    @GetMapping("/config")
    public JSONData list(@ModelAttribute BoardConfigSearch search) {

        ListData<Board> items = infoService.getList(search);

        return new JSONData(items);
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

        List<Board> items = updateService.process(form);

        return new JSONData(items);
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

        List<Board> items = deleteService.process(bids);

        return new JSONData(items);
    }
}