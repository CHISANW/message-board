package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import messageboard.entity.Board_Like_check;
import messageboard.entity.member.Member;
import messageboard.repository.BoardLIkeRepository;
import messageboard.service.BardLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeServiceImpl implements BardLikeService {

    private final MemberServiceImpl memberService;
    private final BoardLIkeRepository boardLIkeRepository;


    /**
     * 기본 값으로는 false 값을 반환한다.
     */
    public boolean isBoardCheck(Member loginMember,Long boardId){

        String loginMemberUsername = loginMember.getUsername();
        Member member = memberService.findByUsername(loginMemberUsername);

        Board_Like_check boardLike = boardLIkeRepository.findMemberId(member.getId(),boardId);
        if (boardLike!=null && boardLike.isLike_check()){
            return true;
        }

        return false;
    }

    /**
     * 
     * @param boardId 값으로 좋아요 삭제
     */
    @Override
    public void deleteByBoardId(Long boardId) {
        boardLIkeRepository.deleteBoard_Id(boardId);
    }
}
