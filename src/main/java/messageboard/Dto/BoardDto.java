package messageboard.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messageboard.entity.Board;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private LocalDateTime dateTime;
    
    @NotBlank(message = "작성자를 입력하세요.")
    private String writer;
    
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    
    @NotBlank(message = "내용을 입력하세요.")
    private String content;


    public BoardDto ofDto(Board board){
        BoardDto build = BoardDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .password(board.getPassword())
                .dateTime(board.getDateTime()).build();
        return build;
    }

}
