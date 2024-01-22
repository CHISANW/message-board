package messageboard.listner;

import lombok.RequiredArgsConstructor;
import messageboard.entity.Board;
import messageboard.event.ViewsEvent;
import messageboard.service.Impl.BoardServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickViewListener implements ApplicationListener<ViewsEvent> {

    private final BoardServiceImpl boardService;
    @Override
    public void onApplicationEvent(ViewsEvent event) {
        Board board = event.getBoard();
        boardService.countViews(board.getId());
    }
}
