package com.devinhouse.veiculosapi.service;

import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository repository;

    public Veiculo inserir(Veiculo veiculo) {
        boolean veiculoJaExiste = repository.existsById(veiculo.getPlaca());
        if (veiculoJaExiste) {
            System.out.println("Veículo com placa já cadastrada. Placa:  " + veiculo.getPlaca());
        }
        veiculo = repository.save(veiculo);
        return veiculo;
    }


}
