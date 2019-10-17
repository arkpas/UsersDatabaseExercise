package arkpas.userdatabase.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String getUsersPageWithErrorMessage (RedirectAttributes redirectAttributes, MaxUploadSizeExceededException e) {
        redirectAttributes.addFlashAttribute("message", "Plik jest za duży! Maksymalny rozmiar wynosi 1MB");
        logger.log(Level.INFO,"File size exceeded", e);
        return "redirect:/users/0";
    }
}
