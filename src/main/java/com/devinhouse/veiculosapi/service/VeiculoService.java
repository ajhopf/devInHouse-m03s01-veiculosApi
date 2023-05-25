package com.devinhouse.veiculosapi.service;

import com.devinhouse.veiculosapi.exception.RegistroExistenteException;
import com.devinhouse.veiculosapi.exception.VeiculoComMultasException;
import com.devinhouse.veiculosapi.exception.VeiculoNaoEncontradoException;
import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Veiculo listarVeiculoPelaPlaca(String placa) {
        return repository.findById(placa)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(placa));
    }

    public void excluir(String placa) {
        Optional<Veiculo> veiculoOptional = repository.findById(placa);
        if (veiculoOptional.isEmpty()) {
            throw new VeiculoNaoEncontradoException(placa);
        }
        Veiculo veiculo = veiculoOptional.get();
        if (veiculo.getQtdMultas() > 0) {
            throw new VeiculoComMultasException(placa);
        }
        repository.delete(veiculo);
    }

    public Veiculo adicionarMulta(String placa) {
        Optional<Veiculo> veiculoOptional = repository.findById(placa);
        if (veiculoOptional.isEmpty()) {
            throw new VeiculoNaoEncontradoException(placa);
        }
        Veiculo veiculo = veiculoOptional.get();
        Integer multas = veiculo.getQtdMultas() + 1;
        veiculo.setQtdMultas(multas);
        veiculo = repository.save(veiculo);
        return veiculo;
    }
}
