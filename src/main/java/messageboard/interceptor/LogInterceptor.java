package messageboard.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "logId";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString().substring(0,5);

        request.setAttribute(LOG_ID,uuid);

        //핸들러 정보 출력
        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
        }

        log.info("요청 [{}] [{}] [{}] [{}]",uuid,requestURI, request.getDispatcherType(),handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.info("컨트롤러 실행후 발생! [{}]",handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("응답 [{}] [{}] [{}] [{}]",logId,requestURI,request.getDispatcherType(),handler);

        if (ex!=null){
            log.error("에러발생 error!!",ex);
        }
    }
}
