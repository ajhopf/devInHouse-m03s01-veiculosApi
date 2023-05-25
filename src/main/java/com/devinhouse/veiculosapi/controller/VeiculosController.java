package com.devinhouse.veiculosapi.controller;

import com.devinhouse.veiculosapi.dtos.VeiculoRequest;
import com.devinhouse.veiculosapi.dtos.VeiculoResponse;
import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.service.VeiculoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
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

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> consultarVeiculos() {
        List<Veiculo> veiculos = veiculoService.listarVeiculos();
        List<VeiculoResponse> veiculosResponse = veiculos.stream().map(v -> modelMapper.map(v, VeiculoResponse.class)).toList();
        return ResponseEntity.ok(veiculosResponse);
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VeiculoResponse> consultarPorPlaca(@PathVariable String placa) {
        Veiculo veiculo = veiculoService.listarVeiculoPelaPlaca(placa);
        VeiculoResponse veiculoResponse = modelMapper.map(veiculo, VeiculoResponse.class);
        return ResponseEntity.ok(veiculoResponse);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity deletarVeiculo(@PathVariable String placa) {
        veiculoService.excluir(placa);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{placa}/multas")
    public ResponseEntity<VeiculoResponse> adicionarMulta(@PathVariable String placa) {
        Veiculo veiculo = veiculoService.adicionarMulta(placa);
        VeiculoResponse veiculoResponse = modelMapper.map(veiculo, VeiculoResponse.class);
        return ResponseEntity.ok().body(veiculoResponse);
    }


}
