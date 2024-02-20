package messageboard.service;

import messageboard.Dto.BoardDto;
import messageboard.entity.member.Member;

public interface BardLikeService {

    void deleteByBoardId(BoardDto boardDto);
    boolean isBoardCheck(Member loginMember, Long boardId);
}
