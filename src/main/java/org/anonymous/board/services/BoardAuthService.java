package org.anonymous.board.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.board.entities.Board;
import org.anonymous.board.entities.BoardData;
import org.anonymous.board.entities.CommentData;
import org.anonymous.board.exceptions.BoardNotFoundException;
import org.anonymous.board.exceptions.GuestPasswordCheckException;
import org.anonymous.board.services.comment.CommentInfoService;
import org.anonymous.board.services.configs.BoardConfigInfoService;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.global.libs.Utils;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.anonymous.member.constants.Authority;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardAuthService {

    private final BoardConfigInfoService configInfoService;

    private final CommentInfoService commentInfoService;

    private final BoardInfoService infoService;

    private final MemberUtil memberUtil;

    private final Utils utils;

    /**
     * 게시판 권한 체크
     *
     * Base Method
     *
     * @param mode : 필수
     * @param bid : 필수
     * @param seq : 필수 X
     */
    public void check(String mode, String bid, Long seq) {

        if (!StringUtils.hasText(mode) || !StringUtils.hasText(bid)

                || (List.of("edit", "delete", "comment").contains(mode) && (seq == null || seq < 1L))) {

            throw new BadRequestException();
        }

        if (memberUtil.isAdmin()) return;

        Board board = null;

        CommentData comment = null;

        if (mode.equals("comment")) { // 댓글 수정 & 삭제

            comment = commentInfoService.get(seq);

            BoardData data = comment.getData();

            board = data.getBoard();

        } else {

            board = configInfoService.get(bid);
        }

        // 게시판 사용 여부 체크
        if (!board.isOpen()) {

            throw new BoardNotFoundException();
        }

        /**
         * mode 값
         *
         * write & list / bid 체크
         * edit & view / seq 체크
         *
         */
        // 게시글 작성 & 목록 & 조회 & 수정(삭제) 권한 체크
        Authority authority = null;

        // false 일 경우 AlertBackException
        boolean isVerified = true;

        Member member = memberUtil.getMember();

        if (List.of("write", "list").contains(mode)) {

            authority = mode.equals("list") ? board.getListAuthority() : board.getWriteAuthority();

        } else if (mode.equals("view")) {

            authority = board.getViewAuthority();

        } else if (List.of("edit", "delete").contains(mode)) {
            /**
             * 1. 회원 게시글인 경우
             *      작성한 회원 본인만 수정 & 삭제 가능
             *
             * 2. 비회원 게시글인 경우
             *      비회원 비밀번호 확인이 완료된 경우 수정 & 삭제 가능
             */
            BoardData item = infoService.get(seq);

            String createdBy = item.getCreatedBy();

            if (createdBy == null) { // 비회원 게시글
                /**
                 * 비회원 게시글이 인증된 경우 = Session Key(board_게시글번호)가 존재
                 * 인증이 되지 않은 경우 GuestPasswordCheckException 발생 -> 비밀번호 확인 절차
                 */

                // 사용자별로 다르게 Redis 에 저장
                if (utils.getValue(utils.getUserHash() + "_board_" + seq) == null) {

                    utils.saveValue(utils.getUserHash() + "_seq", seq);

                    throw new GuestPasswordCheckException();
                }

                // 미로그인 상태 || 로그인 상태이지만 게시글 작성자가 아닐 경우
            } else if (!memberUtil.isLogin() || !createdBy.equals(member.getEmail())) { // 회원 게시글 - 작성한 회원 본인만 수정 & 삭제 가능 통제

                isVerified = false;
            }

        } else if (mode.equals("comment")) { // 댓글 수정 & 삭제

            String commenter = comment.getCreatedBy();

            if (commenter == null) { // 비회원 작성 댓글

                if (utils.getValue(utils.getUserHash() + "_comment_" + seq) == null) { // 댓글 비회원 인증 X

                    utils.saveValue(utils.getUserHash() + "_cSeq", seq);

                    throw new GuestPasswordCheckException();
                }

            } else if (!memberUtil.isLogin() || !commenter.equals(member.getEmail())) { // 회원 작성 댓글

                isVerified = false;
            }
        }

        // 회원 권한인데 미로그인이거나 || 관리자 권한인데 관리자가 아닐 경우
        if ((authority == Authority.USER && !memberUtil.isLogin()) || (authority == Authority.ADMIN && !memberUtil.isAdmin())) {

            isVerified = false;
        }

        if (!isVerified) throw new UnAuthorizedException();
    }

    public void check(String mode, String bid) {

        check(mode, bid, null);
    }

    public void check(String mode, Long seq) {

        BoardData item = null;

        if (mode.equals("comment")) {

            CommentData comment = commentInfoService.get(seq);

            item = comment.getData();

        } else {

            item = infoService.get(seq);
        }

        Board board = item.getBoard();

        check(mode, board.getBid(), seq);
    }
}