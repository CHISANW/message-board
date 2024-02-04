package messageboard.interceptor;

import lombok.extern.slf4j.Slf4j;
import messageboard.entity.Member;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (session == null || loginMember == null || loginMember.getUsername() == null) {
            log.info("미사용자 요청");
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}
