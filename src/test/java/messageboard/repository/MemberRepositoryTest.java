package messageboard.repository;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.service.BoardService;
import messageboard.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Transactional
@SpringBootTest(properties =  {"spring.config.location=classpath:application-test.yml"})
@Slf4j
class MemberRepositoryTest {

    @Autowired MemberService memberService;
    @Autowired
    BoardService boardService;
    @Test
    @Rollback(value = false)
    void test(){
        log.info("테스트 실행");

        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("aaa");

        Member member = memberService.save(memberDto);

        BoardDto boardDto = new BoardDto();
        boardDto.setDateTime(LocalDateTime.now());
        boardDto.setTitle("제목");
        boardDto.setMemberDto(memberDto);
        Board board = boardService.save(boardDto);


        log.info("member={}",member);

        log.info("getMembers={}",board.getMember());
        log.info("getTitle={}",board.getTitle());
        log.info("getDateTime={}",board.getDateTime());
    }


}