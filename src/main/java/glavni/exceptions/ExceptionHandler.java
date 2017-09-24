package glavni.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@org.springframework.web.bind.annotation.ExceptionHandler(UsernameNePostojiException.class)
	public ModelAndView handleUsernameNePostojiException(UsernameNePostojiException e){
		ModelAndView mav = new ModelAndView();
		mav.addObject("poruka" , e.getMessage());
		mav.setViewName("/error.jsp");
		return mav;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@org.springframework.web.bind.annotation.ExceptionHandler(UsernameVecPostojiException.class)
	public ModelAndView handleUsernameVecPostojiException(UsernameVecPostojiException e){
		ModelAndView mav = new ModelAndView();
		mav.addObject("poruka" , e.getMessage());
		mav.setViewName("/error.jsp");
		return mav;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@org.springframework.web.bind.annotation.ExceptionHandler(EmailNePostojiException.class)
	public ModelAndView handleEmailNePostojiException(EmailNePostojiException e){
		ModelAndView mav = new ModelAndView();
		mav.addObject("poruka" , e.getMessage());
		mav.setViewName("/error.jsp");
		return mav;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@org.springframework.web.bind.annotation.ExceptionHandler(EmailVecPostojiException.class)
	public ModelAndView handleEmailVecPostojiException(EmailVecPostojiException e){
		ModelAndView mav = new ModelAndView();
		mav.addObject("poruka" , e.getMessage());
		mav.setViewName("/error.jsp");
		return mav;
	}
}
