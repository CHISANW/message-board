package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import messageboard.entity.Board_Like;
import messageboard.entity.Member;
import messageboard.repository.BoardLIkeRepository;
import messageboard.service.BardLikeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BardLikeService {

    private final MemberServiceImpl memberService;
    private final BoardLIkeRepository boardLIkeRepository;


    /**
     * 기본 값으로는 false 값을 반환한다.
     */
    public boolean isBoardCheck(Member loginMember){

        String loginMemberUsername = loginMember.getUsername();
        Member member = memberService.findByUsername(loginMemberUsername);

        Board_Like boardLike = boardLIkeRepository.findMemberId(member.getId());
        if (boardLike!=null && boardLike.isLike_check()){
            return true;
        }

        return false;
    }
}
