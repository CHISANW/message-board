package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Exception.BoardException;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.service.BoardSortService;
import messageboard.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    private final BoardSortService boardService;
    private final MemberService memberService;
    @GetMapping("/board/sortType")
    public String likeSon(@RequestParam(value = "sortBoard",defaultValue = "",required = false) String sortType,
                          Model model,
                          HttpSession session,
                          @PageableDefault Pageable pageable){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal() instanceof User){
                User user = (User) authentication.getPrincipal();
                Member member = memberService.findByLoginId(user.getUsername());
                model.addAttribute("loginMember",member);
            }

            Page<Board> boards = boardService.TypeSort(sortType, pageable);
            List<Board> content = boards.getContent();
            String typeValue = boardService.TypeValue(sortType);

            int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
            int totalPages = boards.getTotalPages(); // 전체 페이지 수
            int visiblePages = 3;
            int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
            int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시

            model.addAttribute("typeValue",typeValue);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("sortBoardList", content);
            model.addAttribute("page", boards);
            model.addAttribute("sortBoard", sortType);
            return "board/sortBoard";
        }catch (BoardException e){
            e.printStackTrace();
            return "redirect:/board";
        }catch (Exception e){
            log.error("Exception occur",e);
            return "redirect:/error-500";
        }
    }
}
