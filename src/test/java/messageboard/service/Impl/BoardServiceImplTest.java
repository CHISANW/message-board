package messageboard.service.Impl;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.service.BoardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties =  {"spring.config.location=classpath:application-test.yml"})
@Slf4j
@Transactional
class BoardServiceImplTest {

    @Autowired
    BoardServiceImpl boardService;
    @Autowired
    MemberServiceImpl memberService;




    @Test
    @Rollback(value = false)
    void test(){

        //사용자 정보 등록후
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("1234");
        memberDto.setPassword("123");

        memberService.saveDto(memberDto);

        MemberDto memberDtoa = new MemberDto();
        memberDtoa.setUsername("아이디2");
        memberDtoa.setPassword("아이디2");

        memberService.saveDto(memberDtoa);

        MemberDto memberDtob = new MemberDto();
        memberDtob.setUsername("아이디3");
        memberDtob.setPassword("아이디3");

        memberService.saveDto(memberDtob);

        MemberDto memberDtoc = new MemberDto();
        memberDtoc.setUsername("아이디4");
        memberDtoc.setPassword("아이디4");

        memberService.saveDto(memberDtoc);


        BoardDto boardDto = new BoardDto();
        boardDto.setContent("12312");
        boardDto.setPassword("111");
        boardDto.setMemberDto(memberDto);
        boardDto.setTitle("제목");
        boardDto.setWriter("민우");

        Board save = boardService.save(boardDto);

        MemberDto memberDto1 = new MemberDto();
        memberDto1.setUsername("1234");
        memberDto1.setPassword("123");


        BoardDto boardDto1 = new BoardDto();
        boardDto1.setId(save.getId());
        boardDto1.setContent(save.getContent());
        boardDto1.setPassword(save.getPassword());
        boardDto1.setMemberDto(memberDto1);
        boardDto1.setTitle(save.getTitle());
        boardDto1.setWriter(save.getWriter());

        boardService.board_like(boardDto1);

        MemberDto memberDto2 = new MemberDto();
        memberDto2.setUsername("아이디2");
        memberDto2.setPassword("아이디2");


        BoardDto boardDto2 = new BoardDto();
        boardDto2.setId(save.getId());
        boardDto2.setContent(save.getContent());
        boardDto2.setPassword(save.getPassword());
        boardDto2.setMemberDto(memberDto2);
        boardDto2.setTitle(save.getTitle());
        boardDto2.setWriter(save.getWriter());

        boardService.board_like(boardDto2);


        MemberDto memberDto3 = new MemberDto();
        memberDto3.setUsername("아이디2");
        memberDto3.setPassword("아이디2");


        BoardDto boardDto3 = new BoardDto();
        boardDto3.setId(save.getId());
        boardDto3.setContent(save.getContent());
        boardDto3.setPassword(save.getPassword());
        boardDto3.setMemberDto(memberDto3);
        boardDto3.setTitle(save.getTitle());
        boardDto3.setWriter(save.getWriter());

        boardService.board_like(boardDto3);

        MemberDto memberDto4 = new MemberDto();
        memberDto4.setUsername("아이디3");
        memberDto4.setPassword("아이디3");


        BoardDto boardDto4 = new BoardDto();
        boardDto4.setId(save.getId());
        boardDto4.setContent(save.getContent());
        boardDto4.setPassword(save.getPassword());
        boardDto4.setMemberDto(memberDto4);
        boardDto4.setTitle(save.getTitle());
        boardDto4.setWriter(save.getWriter());

        boardService.board_like(boardDto4);


        MemberDto memberDto5 = new MemberDto();
        memberDto5.setUsername("아이디4");
        memberDto5.setPassword("아이디4");


        BoardDto boardDto5 = new BoardDto();
        boardDto5.setId(save.getId());
        boardDto5.setContent(save.getContent());
        boardDto5.setPassword(save.getPassword());
        boardDto5.setMemberDto(memberDto5);
        boardDto5.setTitle(save.getTitle());
        boardDto5.setWriter(save.getWriter());

        boardService.board_like(boardDto5);

//
//        MemberDto memberDto6 = new MemberDto();
//        memberDto6.setUsername("아이디4");
//        memberDto6.setPassword("아이디4");
//
//
//        BoardDto board6Dto = new BoardDto();
//        board6Dto.setId(save.getId());
//        board6Dto.setContent(save.getContent());
//        board6Dto.setPassword(save.getPassword());
//        board6Dto.setMemberDto(memberDto6);
//        board6Dto.setTitle(save.getTitle());
//        board6Dto.setWriter(save.getWriter());
//
//        boardService.board_like(board6Dto);         //삭제




    }

}
