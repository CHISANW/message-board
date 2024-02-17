package messageboard.service.Impl;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.Board;
import messageboard.entity.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties =  {"spring.config.location=classpath:application-test.yml"})
@Transactional
@Slf4j
class BoardLikeServiceImplTest {


    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    BoardLikeServiceImpl boardLikeService;
    @Autowired
    BoardServiceImpl boardService;

    @Test
    @Rollback(value = false)
    void test(){
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("123");
        memberDto.setPassword("123");

        Member member = memberService.saveDto(memberDto);

        MemberDto memberDot1 = new MemberDto();
        memberDot1.setUsername("123");
        memberDot1.setPassword("123");

        BoardDto boardDto = new BoardDto();
        boardDto.setContent("content");
        boardDto.setTitle("title");
        boardDto.setPassword("123");
        boardDto.setDateTime(LocalDateTime.now());
        boardDto.setMemberDto(memberDot1);
        boardDto.setWriter(member.getUsername());

        Board save = boardService.save(boardDto);
        log.info("member={}",member.getUsername());
//        boolean boardCheck = boardLikeService.isBoardCheck(member.getUsername());


        assertThat(member.getUsername()).isEqualTo("123");
        assertThat(save.getContent()).isEqualTo("content");

//        assertThat(boardCheck).isTrue();


    }
}