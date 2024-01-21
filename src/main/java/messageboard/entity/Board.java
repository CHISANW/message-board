package messageboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id@GeneratedValue
    @Column(name = "board_Id")
    private Long Id;

    private String title;

    private String writer;
    private String password;

    private LocalDateTime dateTime;
    private String content;

    private Integer views;


    public void update(String title, String content){
        this.title=title;
        this.content = content;
    }
}
