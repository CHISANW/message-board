package messageboard.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
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

    private MemberDto memberDto;


}
