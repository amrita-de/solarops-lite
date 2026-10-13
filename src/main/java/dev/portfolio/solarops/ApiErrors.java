package dev.portfolio.solarops;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
@RestControllerAdvice public class ApiErrors {
 @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<ProblemDetail> duplicate(){return ResponseEntity.status(409).body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,"Concurrent or conflicting request. Retry with the same payload."));}
}
