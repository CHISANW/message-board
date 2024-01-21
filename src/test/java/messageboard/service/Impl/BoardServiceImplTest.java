package messageboard.service.Impl;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
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
    BoardService boardService;
    
    @Test
    @Rollback(value = false)
    void test(){
        BoardDto boardDto = new BoardDto();
        boardDto.setContent("12312");
        boardDto.setTitle("제목");
        boardDto.setWriter("민우");

        Board save = boardService.save(boardDto);
        log.info("id={}",save.getId());
        assertThat(save).isInstanceOf(Board.class);

        boardService.deleteBoard(1l);



    }

}