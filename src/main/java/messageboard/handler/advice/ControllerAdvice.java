package messageboard.handler.advice;

import lombok.extern.slf4j.Slf4j;
import messageboard.Exception.CommentException;
import messageboard.handler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "messageboard.controller")
public class ControllerAdvice {


    @ExceptionHandler
    public ResponseEntity<ErrorResult> commentHandler(CommentException e){
        log.error("[exception] ex",e);
        log.info("[exception getMessage]={}",e.getMessage());
        ErrorResult errorResult = new ErrorResult("Comment-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
