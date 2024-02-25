package messageboard.config;

import lombok.RequiredArgsConstructor;
import messageboard.handler.security.CustomAuthenticationFailHandler;
import messageboard.handler.security.CustomAuthenticationSuccessHandler;
import messageboard.handler.security.CustomOauthLogoutHandler;
import messageboard.handler.security.OAuth2AuthorizationSuccessHandler;
import messageboard.repository.MemberRepository;
import messageboard.service.security.CustomOauthUserService;
import messageboard.service.security.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationFailHandler customAuthenticationFailHandler;
    private final CustomOauthLogoutHandler customOauthLogoutHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final MemberRepository memberRepository;
    private final CustomOauthUserService customOauthUserService;

    private static String url[] ={"/login","/" ,"/createMember","/board","/board/*","/login-disabled","/login-error","/login-emailVerified","/verify/email",
            "/check/loginId","/check/password/duplicate","/check/password/strength","/check/username/valid","/check/phoneNumber/valid","/check/email/valid",
            "/member/findId","/api/findId","/findId/mailConfirm","/member/findPwd","/api/findPwd","/api/change/Pwd"

    };
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(url).permitAll().anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .failureHandler(customAuthenticationFailHandler).successHandler(customAuthenticationSuccessHandler).loginProcessingUrl("/login")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(customOauthLogoutHandler)
                .and()
                .oauth2Login().loginPage("/login").successHandler(new OAuth2AuthorizationSuccessHandler()).userInfoEndpoint().userService(customOauthUserService);


        http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/img/**","/css/**");
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailService(memberRepository);
    }
}
