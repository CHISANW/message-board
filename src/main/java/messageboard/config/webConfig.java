package messageboard.config;

import lombok.RequiredArgsConstructor;
import messageboard.interceptor.LogInterceptor;
import messageboard.interceptor.LoginCheckInterceptor;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class webConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error","error-page/**","/img/**");

//        registry.addInterceptor(new LoginCheckInterceptor(memberRepository))
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/","/createMember","/login","/logout","/css/**","/board","/delete/comment","/*.ico","/error","/board/*","/error-page/*","/error-**","/img/**","/boardWrit","/delete");
    }

}

