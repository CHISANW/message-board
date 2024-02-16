package messageboard.Dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private LocalDateTime dateTime;

    private Integer board_like;

    private String writer;
    
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private Integer bord_like;

    private BoardLikeDto boardLikeDto;

    private MemberDto memberDto;

}
