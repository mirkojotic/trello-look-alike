package us.jotic.trello.validation;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.FieldError;

// By default @ControllerAdvice apply globally to all @Controller annotated classes
// needless to say that this includes @RestController as it is a combination of 
// @Controller and @ResponseBody
// this annotation is how we let Spring know we want to handle all 
// MethodArgumentNotValidException here
@ControllerAdvice
public class ValidationRestErrorHandler {

	// Strategy interface for resolving messages, 
	// with support for the parameterization and internationalization of such messages.
    private MessageSource messageSource;
    
    // Autowiring MessageSource interface
    // Implementation class => DelegatingMessageSource
    @Autowired
    public ValidationRestErrorHandler(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }
   
    // defining exception handler for all MethodArgumentNotValidException
    // this exception occurs when validation constraints are not satisfied
    // we're building validation errors when this happens and returning them
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError processValidationError(MethodArgumentNotValidException ex) {
    	// MethodArgumentNotValidException.getBindingResult returns results of failed validation
    	BindingResult result = ex.getBindingResult();
    	
    	List<FieldError> fieldErrors = result.getFieldErrors();
    	return processFieldErrors(fieldErrors);
    }
   
    // processing validation error messages
    public ValidationError processFieldErrors(List<FieldError> fieldErrors) {
    	// our data transfer object will hold all validation error messages
    	ValidationError dto = new ValidationError(); 
    	for(FieldError fieldError : fieldErrors) {
    		String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
    		// adding a field error to our data transfer object
    		dto.addFieldError(fieldError.getField(), localizedErrorMessage);
    	}
    	return dto;
    }
   
    public String resolveLocalizedErrorMessage(FieldError fieldError) {
    	// this is where we would return a i18n translation of validation messages
    	// it's out of scope but you can check out how here:
    	// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/
    	// coincidentally ( :D ) this is the main resource for this post
    	Locale currentLocale = LocaleContextHolder.getLocale();
    	// messageSource is implementation of messageSource interface
    	// class in question is DelegatingMessageSource
    	return messageSource.getMessage(fieldError, currentLocale);
    }
}
