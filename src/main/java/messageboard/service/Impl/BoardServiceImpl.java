package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.repository.BoardRepository;
import messageboard.repository.MemberRepository;
import messageboard.service.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    @Override
    public Board save(BoardDto boardDto) {

        Board build = Board.builder()
                .title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(boardDto.getWriter())
                .password(boardDto.getPassword())
                .content(boardDto.getContent())
                .build();

        Board save = boardRepository.save(build);
        return save;
    }
}
