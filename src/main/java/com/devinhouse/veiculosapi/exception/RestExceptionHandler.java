package com.devinhouse.veiculosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RegistroExistenteException.class)
    public ResponseEntity<Object> handleRegistroExistenteException(RegistroExistenteException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Registro já cadastrado! Placa: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<Object> handleRegistroNaoEncontradoException(VeiculoNaoEncontradoException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Placa não encontrada: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(VeiculoComMultasException.class)
    public ResponseEntity<Object> handleVeiculoComMultasException(VeiculoComMultasException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Veículo não deletado pois possui 1 ou mais multas cadastradas. Placa: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
