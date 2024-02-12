package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.service.Impl.BoardServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardSortController {

    private final BoardServiceImpl boardService;


    @GetMapping("/board/sortType")
    public String likeSon(@RequestParam(value = "sort",defaultValue = "",required = false) String sortType,
                          Model model,
                          HttpSession session,
                          @PageableDefault(size = 10) Pageable pageable,
                          @RequestParam(required = false, defaultValue = "") String title){
//        Page<Board> boards = boardService.search(title, pageable);

        List<Board> objects = new ArrayList<>();

        Member loginMember = (Member) session.getAttribute("loginMember");
        List<Long> sortList1 = getSortList(sortType);
        for (Long aLong : sortList1) {
            Board byBoardId = boardService.findByBoardId(aLong);

            objects.add(byBoardId);
        }

        model.addAttribute("sortBoardList",objects);
        model.addAttribute("loginMember",loginMember);

        return "board/sortBoard";
    }

    private List<Long> getSortList(String sortType) {
        switch (sortType){
            case "likeType":
                return boardService.likeSon();
            case "manyViewType":
                return boardService.manyViewsDesc();
            case "manyCountType":
                return boardService.manyCommentDesc();
            case "lastBoardType":
                return  boardService.lastBoardDesc();
            default:
                throw new RuntimeException("잘못된 요청입니다.");
        }
    }

}
