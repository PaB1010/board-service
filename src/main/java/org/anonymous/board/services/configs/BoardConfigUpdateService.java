package org.anonymous.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.anonymous.board.controllers.RequestConfig;
import org.anonymous.board.entities.Board;
import org.anonymous.board.repositories.BoardRepository;
import org.anonymous.member.constants.Authority;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigUpdateService {

    private final BoardRepository boardRepository;

    public Board process(RequestConfig form) {

        String bid = form.getBid();

        Board board = boardRepository.findById(bid).orElseGet(Board::new);

        addInfo(board, form);

        boardRepository.saveAndFlush(board);

        return board;
    }

    public List<Board> process(List<RequestConfig> items) {

        if (items == null && items.isEmpty()) return null;

        List<Board> processed = new ArrayList<>();

        for (RequestConfig form : items) {

            Board item = boardRepository.findById(form.getBid()).orElseGet(Board::new);

            addInfo(item, form);

            processed.add(item);
        }

        boardRepository.saveAllAndFlush(processed);

        return processed;
    }

    private void addInfo(Board board, RequestConfig form) {

        board.setBid(form.getBid());
        board.setName(form.getName());
        board.setOpen(form.isOpen());
        board.setCategory(form.getCategory());
        board.setRowsPerPage(form.getRowsPerPage() < 1 ? 20 : form.getRowsPerPage());
        board.setPageRanges(form.getPageRanges() < 1 ? 10 : form.getPageRanges());
        board.setPageRangesMobile(form.getPageRangesMobile() < 1 ? 5 : form.getPageRangesMobile());
        board.setUseEditor(form.isUseEditor());
        board.setUseEditorImage(form.isUseEditorImage());
        board.setUseAttachFile(form.isUseAttachFile());
        board.setUseComment(form.isUseComment());
        board.setSkin(StringUtils.hasText(form.getSkin()) ? form.getSkin() : "default");
        board.setListAuthority(Objects.requireNonNullElse(form.getListAuthority(), Authority.ALL));
        board.setViewAuthority(Objects.requireNonNullElse(form.getViewAuthority(), Authority.ALL));
        board.setWriteAuthority(Objects.requireNonNullElse(form.getWriteAuthority(), Authority.ALL));
        board.setCommentAuthority(Objects.requireNonNullElse(form.getCommentAuthority(), Authority.ALL));

        String locationAfterWriting = form.getLocationAfterWriting();
        board.setLocationAfterWriting(StringUtils.hasText(locationAfterWriting) ? locationAfterWriting : "list");

        board.setListUnderView(form.isListUnderView());
    }
}