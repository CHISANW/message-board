package messageboard;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("execution(* messageboard.service.Impl.CommentServiceImpl.*(..))")
    private void commentServiceMethods() {}

    @Before("commentServiceMethods() && execution(* messageboard.service.Impl.CommentServiceImpl.save(..)) && args(commentDto)")
    @Order(1)
    public void logMethodStart(CommentDto commentDto) {
        log.info("댓글 저장 내용..={}",commentDto.getComment());
    }

    @Around("commentServiceMethods() && execution(* messageboard.service.Impl.CommentServiceImpl.save(..)) && args(commentDto)")
    @Order(2)
    public Object logSaveMethod(ProceedingJoinPoint joinPoint, CommentDto commentDto) throws Throwable {
        System.out.println("Method execution started...");
        Object result = joinPoint.proceed(); // save 메서드 실행
        Object target = joinPoint.getTarget();
        Object aThis = joinPoint.getThis();
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();

        log.info("[target={}] [args={}] [aThis={}] [signature={}]",target,args,aThis,signature);

        System.out.println("Method execution ended...");
        return result;
    }

    @AfterReturning("commentServiceMethods()")
    @Order(3)
    public void logMethodEnd() {
        System.out.println("댓글 저장 종료...");
    }
}