package com.devinhouse.veiculosapi.service;

import com.devinhouse.veiculosapi.exception.RegistroExistenteException;
import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository repository;

    public Veiculo inserir(Veiculo veiculo) {
        boolean veiculoJaExiste = repository.existsById(veiculo.getPlaca());
        if (veiculoJaExiste) {
            throw new RegistroExistenteException(veiculo.getPlaca());
        }
        veiculo = repository.save(veiculo);
        return veiculo;
    }

    public List<Veiculo> listarVeiculos() {
        return repository.findAll();
    }


}
