package messageboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board_Like {

    @Id@GeneratedValue
    @Column(name = "board_like_Id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_Id")
    private Board board;

    @OneToOne()
    @JoinColumn(name = "member_Id",unique = true)
    private Member member;


    private boolean like_check;


}
