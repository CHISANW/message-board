package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.LoginDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.Member;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;


    @GetMapping("/createMember")
    public String createMember(Model model){
        model.addAttribute("member",new MemberDto());
        return "member/joinMember";
    }

    @PostMapping("/createMember")
    public String createMemberPost(@Valid @ModelAttribute("member")MemberDto memberDto, BindingResult result){
        if (result.hasErrors()){
            return "member/joinMember";
        }
        memberService.saveDto(memberDto);
        return "redirect:/";
    }

//    @GetMapping("/login")
//    public String getLogin(@RequestParam(defaultValue = "/") String redirectURL,HttpServletRequest request, Model model){
//
//        model.addAttribute("login", new LoginDto());
//
//        String referer = request.getHeader("Referer");
//        request.getSession().setAttribute("referer", referer);
//
//        // 이전 페이지의 URL을 쿼리 매개변수로 전달
//        request.getSession().setAttribute("prevPage",redirectURL);
//        return "member/login";
//    }
//
//    @PostMapping("/login")
//    public String postLogin(@ModelAttribute("login") LoginDto loginDto,
//                            HttpSession session,HttpServletRequest request, Model model){
//
//        boolean login = memberService.login(loginDto);
//
//
//        if (login){
//            String username = loginDto.getUsername();
//            Member member = memberService.findByUsername(username);
//            session.setAttribute("loginMember",member);
//
//            String prevPage = (String) request.getSession().getAttribute("prevPage");
//            String rePrevPage = (String) request.getSession().getAttribute("referer");
//
//
//            // 로그인 후에는 전달된 redirectURL로 리다이렉트
//            return "redirect:" + (prevPage.equals("/") ? rePrevPage : prevPage);
//        }
//
//        model.addAttribute("error", "비밀번호 또는 아이디가 올바르지 않습니다.");
//        return "member/login";
//    }
//
//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request, HttpSession session){
//
//        String referer = request.getHeader("referer");  // 요청한 페이지의 URL 주소
//        session.removeAttribute("loginMember");
//
//        return "redirect:"+referer;
//    }
}
