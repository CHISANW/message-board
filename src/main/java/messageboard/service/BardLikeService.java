package messageboard.service;

import messageboard.entity.member.Member;

public interface BardLikeService {

    void deleteByBoardId(Long boardId);
    boolean isBoardCheck(Member loginMember, Long boardId);
}
