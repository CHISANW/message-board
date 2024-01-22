package messageboard.event;

import messageboard.entity.Board;
import org.springframework.context.ApplicationEvent;

public class ViewsEvent extends ApplicationEvent {

    private Board board;
    public ViewsEvent(Board board) {
        super(board);
        this.board = board;
    }

    public Board getBoard(){
        return board;
    }
}
