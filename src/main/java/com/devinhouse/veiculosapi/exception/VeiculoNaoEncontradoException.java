package com.devinhouse.veiculosapi.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {
    public VeiculoNaoEncontradoException (String message) {
        super(message);
    }
}
