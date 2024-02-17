package messageboard.handler.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

    private DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof DisabledException){
            defaultRedirectStrategy.sendRedirect(request, response, "/login-disabled");
        } else {
            defaultRedirectStrategy.sendRedirect(request, response, "/login-error");
        }
    }
}
