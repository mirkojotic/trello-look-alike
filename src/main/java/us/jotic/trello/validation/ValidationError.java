package us.jotic.trello.validation;

import java.util.List;
import java.util.ArrayList;

public class ValidationError {
	
	private List<FieldError> fieldErrors = new ArrayList<>();

	public ValidationError() {
		
	}
	
	public void addFieldError(String path, String message) {
		FieldError error = new FieldError(path, message);
		fieldErrors.add(error);
	}
	
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}
}
