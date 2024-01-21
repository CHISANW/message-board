package messageboard.repository;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
import messageboard.service.BoardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties =  {"spring.config.location=classpath:application-test.yml"})
@Slf4j
@Transactional
class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;
    @Autowired
    BoardService boardService;

    @Test
    @Rollback
    void test(){
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("test1");
        boardDto.setContent("test1");

        boardService.save(boardDto);

        Board byBoardId = boardService.findByBoardId(1l);
        log.info("board={}",byBoardId);

        log.info("id={}",boardDto.getId());

//        Board board = boardService.updateBoard(boardDto);
//
//        log.info("after={}",board);

    }

}