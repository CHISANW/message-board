package messageboard.controller.ex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
public class ErrorPageController {

    @RequestMapping("/error-page/404")
    public String errorPage404(){
        log.info("404오류");
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(){
        log.info("500오류");
        return "error-page/500";
    }

}
