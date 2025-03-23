package by.rymtsou.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView  handleException(Exception e, Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        mav.addObject("exception", e.getMessage());
        mav.setStatus(HttpStatus.BAD_REQUEST);
        return mav;
    }
}
