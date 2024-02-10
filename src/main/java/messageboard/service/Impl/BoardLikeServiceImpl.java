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
    public boolean isBoardCheck(String memberUsername){

        Member byUsername = memberService.findByUsername(memberUsername);
        if (byUsername.getUsername()==null){
            throw new RuntimeException("체크오류");
        }

        Long memberId = byUsername.getId();
        Board_Like boardLike = boardLIkeRepository.findMemberId(memberId);

        boolean likeCheck = boardLike.isLike_check();  //true 일떄
        if (likeCheck){
            return true;
        }

        return false;
    }
}
