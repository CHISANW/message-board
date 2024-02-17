package messageboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messageboard.entity.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_Id")
    private Long id;

    @Column(name = "comment_content")
    private String content;

    private LocalDateTime dateTime;
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_Id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;



}
