package org.anonymous.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.board.entities.Board;
import org.anonymous.board.entities.BoardData;
import org.anonymous.board.services.*;
import org.anonymous.board.services.configs.BoardConfigInfoService;
import org.anonymous.board.validators.BoardValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final Utils utils;

    private final BoardAuthService authService;

    private final BoardInfoService infoService;

    private final BoardValidator boardValidator;

    private final BoardUpdateService updateService;

    private final BoardDeleteService deleteService;

    private final BoardConfigInfoService configInfoService;

    private final BoardViewUpdateService viewUpdateService;

    /**
     * 게시판 설정
     * 단일 조회
     *
     * @param bid
     * @return
     */
    @GetMapping("/config/{bid}")
    public JSONData config(@PathVariable("bid") String bid) {

        Board board = configInfoService.get(bid);

        return new JSONData(board);
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

        BoardData data = updateService.process(form);

        return new JSONData(data);
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

        BoardData data = infoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 게시글
     * 목록 조회
     *
     * @param bid
     * @return
     */
    @GetMapping("/list/{bid}")
    public JSONData list(@PathVariable("bid") String bid, @ModelAttribute BoardSearch search) {

        commonProcess(bid, "list");

        ListData<BoardData> data = infoService.getList(bid, search);

        return new JSONData(data);
    }

    /**
     * 조회수 업데이트 처리
     *
     * @param seq
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/viewcount/{seq}")
    public JSONData updateViewCount(@PathVariable("seq") Long seq) {

        viewUpdateService.process(seq);

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

        // 댓글이 존재하면 삭제 불가
        boardValidator.checkDelete(seq);

        BoardData item = deleteService.delete(seq);

        return new JSONData(item);
    }

    /**
     * 비회원 비밀번호 검증
     *
     * 응답 코드 204 : 검증 성공
     * 응답 코드 401 : 검증 실패
     *
     * @param seq : 게시글 번호
     * @param password
     */
    @PostMapping("/password/{seq}")
    public ResponseEntity<Void> validateGuestPassword(@PathVariable("seq") Long seq, @RequestParam(name = "password", required = false) String password) {

        if (!StringUtils.hasText(password)) {

            throw new BadRequestException(utils.getMessage("NotBlank.password"));
        }

        HttpStatus status = boardValidator.checkGuestPassword(password, seq) ? HttpStatus.NO_CONTENT : HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).build();
    }

    /**
     * 게시글 번호로 공통 처리
     *
     * @param seq
     * @param mode
     */
    private void commonProcess(Long seq, String mode) {

        // 게시판 권한 체크 - 조회, 수정, 삭제
        authService.check(mode, seq);
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

        // 게시판 권한 체크 - 글 목록, 글 작성
        authService.check(mode, bid);
    }
}