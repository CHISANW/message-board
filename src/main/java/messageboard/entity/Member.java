package messageboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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

    private String password;


    @JsonIgnore
    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member" ,cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments =new ArrayList<>();
}


