package com.example.onlinemedicine.exception;


import com.example.onlinemedicine.dto.base.ErrorDto;
import com.example.onlinemedicine.exception.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;



@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorDto> myExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage(), 401));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> bindExceptionHandler(BindException e) {
        StringBuilder massages = new StringBuilder();
        e.getAllErrors().forEach(error -> {
            massages.append(error.getDefaultMessage());
            massages.append(" , ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(massages.toString(), 404));
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> DataAlreadyExistsExceptionHandler(DataAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage(), 404));
    }

    @ExceptionHandler(WrongInputException.class)
    public ResponseEntity<ErrorDto> WrongInputExceptionHandler(WrongInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage(), 404));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDto> DataAlreadyExistsExceptionHandler(Throwable e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage(), 404));
    }

        @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
        @ExceptionHandler(value = {NotCorrectPhoneNumber.class, NotCorrectEmail.class})
        public ResponseEntity<NotCorrectEnteredEntityResponse> responseForNotCorrectException(NotCorrectPhoneNumber e){
            NotCorrectEnteredEntityResponse
                    notCorrectEnteredEntityResponse = new NotCorrectEnteredEntityResponse(
                    422, "You entered incorrect value", "Unprocessable entity"
            );

            return new ResponseEntity<>(notCorrectEnteredEntityResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @ExceptionHandler(value = PharmacyNotFound.class)
        @ResponseStatus(value = HttpStatus.NOT_FOUND)
        public NotFoundPharmacyResponse responseForNotExistPharmacy(PharmacyNotFound e){
            return new NotFoundPharmacyResponse(404, e.getMessage(), "Not found");
        }


        @ExceptionHandler(value = NotExistPharmacy.class)
        @ResponseStatus(value = HttpStatus.GONE)
        public NotExistPharmacyResponse responseNotExistPharmacy(NotExistPharmacy e){
            return new NotExistPharmacyResponse(410, e.getMessage(), "Already gone");
        }

        @ExceptionHandler(value = AlreadyExistPharmacy.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public AlreadyExistPharmacyResponse alreadyExistPharmacyResponse(AlreadyExistPharmacy e){
            return new AlreadyExistPharmacyResponse(400, "This pharmacy is already defined", e.getMessage());
        }

        @ExceptionHandler(value = NullPointerException.class)
        @ResponseStatus(value = HttpStatus.NOT_FOUND)
        public NullPointerResponse dataNotFound(NullPointerException e){
            return new NullPointerResponse(404, "Not found data", e.getMessage());
        }




}
