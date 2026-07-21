package bg.softuni.cinevault.config;

import bg.softuni.cinevault.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(ApplicationException ex) {
        log.warn("ApplicationException occurred: {}", ex.getMessage(),ex);

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("errorTitle", ex.getErrorTitle());
        mav.addObject("errorCode", ex.getErrorCode());
        mav.addObject("errorMessage", ex.getMessage());

        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred",ex);

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("errorTitle", "Internal Server Error");
        mav.addObject("errorCode", "500");
        mav.addObject("errorMessage", "Something unexpected happened.");

        return mav;
    }
}
