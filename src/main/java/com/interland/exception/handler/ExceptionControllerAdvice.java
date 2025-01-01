package com.interland.exception.handler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.interland.exception.DataNotFoundException;
import com.interland.exception.UserAlreadyExistException;
import com.interland.exception.UserNotFoundException;
import com.interland.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class ExceptionControllerAdvice {

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<JSONObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
		JSONObject response = new JSONObject();
		JSONArray details = new JSONArray();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			JSONObject detail = new JSONObject();
			try {
				detail.put(((FieldError) error).getField(), error.getDefaultMessage());
				details.add(detail);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		});
		response.put(Constants.CODE, Constants.VALERRCOD);
		response.put(Constants.MESSAGE, "Validation Failed");
		response.put(Constants.DETAILS, details);

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<JSONArray> handleNullPointerException(NullPointerException e) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(e.getMessage());
		jsonArray.add(e.getClass());
		jsonArray.add(e.getLocalizedMessage());
		return new ResponseEntity<>(jsonArray, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<JSONObject> handleHttpClientErrorExceptions(HttpClientErrorException ex) {
		JSONArray details = new JSONArray();
		JSONObject response = addResponse("VALERRCOD","Client Exception",details);
		response.put(Constants.ACCESS_TOKEN, "No token");
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	} 

	

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<JSONObject> handleDataNotFoundException(DataNotFoundException ex) {
		JSONArray details = new JSONArray();
		JSONObject response = addResponse(Constants.FAILED,"data Not Exist !!!",details);
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<JSONObject> handleUserAlreadyExistException(UserAlreadyExistException ex) {
		JSONArray details = new JSONArray();
		JSONObject response = addResponse(Constants.FAILED,"User already Exist !!!",details);
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<JSONObject> handleUserNotFoundExceptions(UserNotFoundException ex) {
		JSONArray details = new JSONArray();
		JSONObject response = addResponse(Constants.FAILED,"User Not  Found !!!",details);
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}
	JSONObject addResponse(String code,String message,JSONArray details) {
		JSONObject response= new JSONObject();
		response.put(Constants.CODE,code);
		response.put(Constants.MESSAGE, message);
		response.put(Constants.DETAILS, details);
		return response;
	}
	
	
}
