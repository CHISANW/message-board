package messageboard.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String comment;

    private LocalDateTime dateTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BoardDto boardDto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MemberDto memberDto;
}
