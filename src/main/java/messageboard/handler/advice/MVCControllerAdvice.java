package messageboard.handler.advice;

import lombok.extern.slf4j.Slf4j;
import messageboard.Exception.NotFindPageException;
import messageboard.handler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.auth.login.LoginException;

@Slf4j
@ControllerAdvice(basePackages = "messageboard.controller")
public class MVCControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResult loginHandler(LoginException e){
        log.error("[exception] ex",e);
       return new ErrorResult("login-EX", e.getMessage());
    }

    @ExceptionHandler(NotFindPageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResult NotFindException(NotFindPageException e){
        log.error("[exception] ex",e);
        return new ErrorResult("NotFount-EX", e.getMessage());
    }
}
