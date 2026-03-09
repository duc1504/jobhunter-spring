package vn.developer.jobhunter.util.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.developer.jobhunter.domain.RestResponse;

 @RestControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler(value = {UsernameNotFoundException.class,BadCredentialsException.class, IdInvaliException.class})
    public ResponseEntity<RestResponse<Object>> handleAuthExceptions (Exception  ex){
         RestResponse<Object> res = new RestResponse<Object>();
        res.setError(ex.getMessage());
        res.setMessage("Exception occured");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException (MethodArgumentNotValidException  ex){
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult()
      .getFieldErrors()
      .forEach(error -> {
          errors.put(
              error.getField(),
              error.getDefaultMessage()
          );
      });
    RestResponse<Object> res = new RestResponse<Object>();
    res.setMessage(errors);
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
}
@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "error", "NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }
@ExceptionHandler(MissingRequestCookieException.class)
public ResponseEntity<RestResponse<Object>> handleMissingRequestCookieException (Exception  ex){
         RestResponse<Object> res = new RestResponse<Object>();
        res.setError(ex.getMessage());
        res.setMessage("Không có cookie");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


@ExceptionHandler(FileUploadExxceoption.class)
    public ResponseEntity<RestResponse<Object>> handleFileUploadExcception (Exception  ex){
         RestResponse<Object> res = new RestResponse<Object>();
        res.setError(ex.getMessage());
        res.setMessage("Upload occured");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
