package messageboard.service;

import messageboard.entity.Member;

public interface BardLikeService {

    void deleteByBoardId(Long boardId);
    boolean isBoardCheck(Member loginMember, Long boardId);
}
