package messageboard.service.Impl;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.CommentDto;
import messageboard.entity.Board;
import messageboard.entity.Comment;
import messageboard.repository.CommentRepository;
import messageboard.service.BoardService;
import messageboard.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties =  {"spring.config.location=classpath:application-test.yml"})
@Slf4j
@Transactional
class CommentServiceImplTest {

    @Autowired CommentService commentService;
    @Autowired
    BoardService boardService;
    @Autowired
    CommentRepository commentRepository;


    @Test
    @Rollback(value = false)
    void test1(){
        BoardDto boardDto = new BoardDto();
        boardDto.setWriter("작성자");
        boardDto.setContent("내용");

        Board save = boardService.save(boardDto);

        log.info("boardSave={}",save);
        log.info("boardDto={}",boardDto);

        BoardDto dto = BoardDto.builder()
                .id(save.getId())
                .writer(save.getWriter())
                .content(save.getContent()).build();



        CommentDto commentDto = CommentDto.builder()
                .comment("댓글 작성")
                .boardDto(dto)
                .build();

        CommentDto commentDto1 = CommentDto.builder()
                .comment("댓글 작성1")
                .boardDto(dto)
                .build();

        Comment commentSave = commentService.save(commentDto);
         commentService.save(commentDto1);

        log.info("comment={}",commentSave);


        List<Comment> byBoardId = commentRepository.findByBoard_Id(1l);
        log.info("Board={}",byBoardId);

        Integer integer = commentRepository.countComment(1l);
        log.info("in={}",integer);

        Long id = commentSave.getId();
        log.info("id={}",id.getClass());
        Long boardId = commentRepository.findBoardId(id);
        log.info("boardId={}",boardId);
    }

}