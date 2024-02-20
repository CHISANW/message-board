package messageboard.handler.advice;

import lombok.extern.slf4j.Slf4j;
import messageboard.Exception.*;
import messageboard.handler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(basePackages = "messageboard.controller")
public class RestApiControllerAdvice {


    @ExceptionHandler
    public ResponseEntity<ErrorResult> commentHandler(CommentException e) throws IOException {
        log.error("[exception] ",e);
        ErrorResult errorResult = new ErrorResult("Comment-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
    @ExceptionHandler(NotFindPage_RestException.class)
    public ResponseEntity<ErrorResult> notFindPageRest(NotFindPage_RestException e) {
        log.error("[404에러]", e);
        ErrorResult errorResult = new ErrorResult("Update-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResult);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> login_restHandler(Login_RestException e){
        log.error("[exception] ex",e);
        ErrorResult errorResult = new ErrorResult("Login-rest-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResult);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> board_restHandler(BoardException e){
        log.error("[exception] ex",e);
        ErrorResult errorResult = new ErrorResult("Board-Ex", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResult> badRequest(BadRequestException e){
        log.error("[401 에러]",e);
        ErrorResult errorResult = new ErrorResult("BadRequest-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> serverError(ServerErrorException e){
        log.info("[500 에러]",e);
        ErrorResult errorResult = new ErrorResult("ServerError", e.getMessage());
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
