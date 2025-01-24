package org.anonymous.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.anonymous.board.entities.Board;
import org.anonymous.board.entities.QBoardData;
import org.anonymous.board.repositories.BoardDataRepository;
import org.anonymous.board.repositories.BoardRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigDeleteService {

    private final BoardRepository boardRepository;

    private final BoardDataRepository boardDataRepository;

    /**
     * 게시판 단일 삭제
     *
     * Base Method
     *
     * @param bid
     * @return
     */
    public Board process(String bid) {

        QBoardData boardData = QBoardData.boardData;

        // 게시글이 존재하면 게시판 삭제 불가
        if (boardDataRepository.count(boardData.board.bid.eq(bid)) > 0L) return null;

        Board board = boardRepository.findById(bid).orElse(null);

        if (board != null) {

            boardRepository.delete(board);
            boardRepository.flush();
        }

        return board;
    }

    /**
     * 게시판 목록 삭제
     *
     * @param bids
     * @return
     */
    public List<Board> process(List<String> bids) {

        List<Board> deleted = new ArrayList<>();

        for (String bid : bids) {

            Board item = process(bid);

            if (item != null) {

                // 삭제된 게시판 정보
                deleted.add(item);
            }
        }

        return deleted;
    }
}