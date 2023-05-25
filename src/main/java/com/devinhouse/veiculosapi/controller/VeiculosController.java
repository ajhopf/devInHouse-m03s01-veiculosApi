package com.devinhouse.veiculosapi.controller;

import com.devinhouse.veiculosapi.dtos.VeiculoRequest;
import com.devinhouse.veiculosapi.dtos.VeiculoResponse;
import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.service.VeiculoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping
public class VeiculosController {
    @Autowired
    private VeiculoService veiculoService;

    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrarVeiculo(@RequestBody @Valid VeiculoRequest veiculoRequest) {
        Veiculo veiculo = modelMapper.map(veiculoRequest, Veiculo.class);
        veiculo = veiculoService.inserir(veiculo);
        VeiculoResponse veiculoResponse = modelMapper.map(veiculo, VeiculoResponse.class);
        return ResponseEntity.created(URI.create(veiculoRequest.getPlaca())).body(veiculoResponse);
    }


}
