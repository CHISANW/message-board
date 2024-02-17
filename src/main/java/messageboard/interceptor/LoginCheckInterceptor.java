package messageboard.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.member.Member;
import messageboard.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestURI = request.getRequestURI();
//
//        HttpSession session = request.getSession(false);
//
//        Member loginMember = (Member) session.getAttribute("loginMember");
//        if (session == null || loginMember == null || loginMember.getUsername() == null) {
//            log.info("미사용자 요청");
//            response.sendRedirect("/login?redirectURL=" + requestURI);
//            return false;
//        }
//        return true;
//    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User){
            String name = authentication.getName();
            Member byLoginId = memberRepository.findByLoginId(name);
            modelAndView.addObject("member",byLoginId);
        }
    }
}
