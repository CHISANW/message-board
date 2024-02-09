package messageboard.handler.advice;

import lombok.extern.slf4j.Slf4j;
import messageboard.Exception.BoardException;
import messageboard.Exception.CommentException;
import messageboard.Exception.Login_RestException;
import messageboard.Exception.NotFindPageException;
import messageboard.handler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestControllerAdvice(basePackages = "messageboard.controller")
public class ControllerAdvice {


    @ExceptionHandler
    public ResponseEntity<ErrorResult> commentHandler(CommentException e, HttpServletResponse response) throws IOException {
        log.error("[exception] ",e);
        ErrorResult errorResult = new ErrorResult("Comment-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResult loginHandler(LoginException e){
        log.error("[exception] ex",e);
       return new ErrorResult("login-EX", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> login_restHandler(Login_RestException e){
        log.error("[exception] ex",e);
        ErrorResult errorResult = new ErrorResult("Login-rest-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResult);
    }

    @ExceptionHandler
    public ErrorResult notFindPage(NotFindPageException e,HttpServletResponse response) throws IOException {
        log.error("[404에러]",e);
        response.sendRedirect("/error-404");
        return new ErrorResult("404-EX",e.getMessage());
    }
}
