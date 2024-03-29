package messageboard.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import messageboard.entity.Board;
import messageboard.entity.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    @Id@GeneratedValue
    @Column(name = "member_Id")
    private Long id;

    private String username;
    private String loginId;

    private String password;

    private String email;

    //생년월일
    private int year;

    private int month;

    private int day;

    private String phoneNumber;

    private boolean verified;

    private String loginType;



    @JsonIgnore
    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member" ,cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments =new ArrayList<>();
}


