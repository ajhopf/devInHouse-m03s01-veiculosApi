package com.devinhouse.veiculosapi.service;

import com.devinhouse.veiculosapi.exception.RegistroExistenteException;
import com.devinhouse.veiculosapi.exception.VeiculoComMultasException;
import com.devinhouse.veiculosapi.exception.VeiculoNaoEncontradoException;
import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.repository.VeiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {
    @Mock
    private VeiculoRepository repository;
    @InjectMocks
    private VeiculoService service;

    @Nested
    @DisplayName("Método: inserir")
    class inserir {
        @Test
        @DisplayName("Quando placa já estiver cadastrada, deve retornar erro")
        void inserir_erro() {
            //given
            Veiculo veiculo = new Veiculo();
            veiculo.setPlaca("IXB-1234");
            //when
            Mockito.when(repository.existsById(Mockito.anyString()))
                    .thenReturn(true);
            //then
            assertThrows(RegistroExistenteException.class, () -> service.inserir(veiculo));
        }

        @Test
        @DisplayName("Quando placa não estiver cadastrada, deve retornar o veiculo cadastrado")
        void inserir_sucesso() {
            //given
            Veiculo veiculo = new Veiculo();
            veiculo.setPlaca("IXB-1234");
            Mockito.when(repository.existsById(Mockito.anyString()))
                    .thenReturn(false);
            Mockito.when(repository.save(Mockito.any(Veiculo.class)))
                    .thenReturn(veiculo);
            //when
            Veiculo resultado = service.inserir(veiculo);
            //then
            assertNotNull(resultado);
            assertEquals(veiculo, resultado);
        }
    }

    @Nested
    @DisplayName("Método: listarVeiculos")
    class listarVeiculos {
        @Test
        @DisplayName("Quando repositório não tiver registros cadastrados, deve retornar lista vazia")
        void listarVeiculos_semRegistros() {
            List<Veiculo> veiculos = service.listarVeiculos();
            assertTrue(veiculos.isEmpty());
        }

        @Test
        @DisplayName("Quando repositório contém registros, deve retornar lista com os registros")
        void listarVeiculos_comRegistros() {
            //given
            List<Veiculo> veiculos = List.of(
                    new Veiculo("IXB-1234", "carro", "azul", 2020),
                    new Veiculo("ABC-1234", "disco-voador", "vermelho", 2000)
            );
            Mockito.when(repository.findAll()).thenReturn(veiculos);
            //when
            List<Veiculo> resultado = service.listarVeiculos();
            //then
            assertEquals(veiculos.size(), resultado.size());
        }
    }

    @Nested
    @DisplayName("Método: listarVeiculoPelaPlaca")
    class listarVeiculoPelaPlaca {
        @Test
        @DisplayName("Quando a placa não estiver cadastrada, deve lançar exceção")
        void listarVeiculoPelaPlaca_placaNaoEncontrada() {
            assertThrows(VeiculoNaoEncontradoException.class, () -> service.listarVeiculoPelaPlaca("1234"));
        }

        @Test
        @DisplayName("Quando a placa estiver cadastrada, deve retornar o veiculo")
        void listarVeiculoPelaPlaca_placaEncontrada() {
            //given
            Veiculo veiculo = new Veiculo();
            veiculo.setPlaca("IXB-1234");
            Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(veiculo));
            //when
            Veiculo resultado = service.listarVeiculoPelaPlaca("IXB-1234");
            //then
            assertNotNull(resultado);
        }
    }

    @Nested
    @DisplayName("Método: excluir")
    class excluir {
        @Test
        @DisplayName("Quando veículo for encontrado e não tiver multas cadastrada, deve excluir veiculo")
        void excluir() {
            Veiculo veiculo = new Veiculo("IXB-1234", "carro", "azul", 2020);
            Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(veiculo));
            assertDoesNotThrow(() -> service.excluir(veiculo.getPlaca()));
        }

        @Test
        @DisplayName("Quando veículo for encontrado mas tiver multas cadastradas, deve lançar exceção")
        void excluir_multaCadastrada() {
            Veiculo veiculo = new Veiculo("IXB-1234", "carro", "azul", 2020, 1);
            Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(veiculo));
            assertThrows(VeiculoComMultasException.class, () -> service.excluir(veiculo.getPlaca()));
        }

        @Test
        @DisplayName("Quando veículo não for encontrado, deve lançar exceção")
        void excluir_veiculoNaoEncontrado() {
            assertThrows(VeiculoNaoEncontradoException.class, () -> service.excluir("IXB-1234"));
        }
    }

    @Nested
    @DisplayName("Método: adicionarMulta")
    class adicionarMulta {
        @Test
        @DisplayName("Quando veículo for encontrado, deve incrementar o número de multas em 1")
        void adicionarMulta() {
            Veiculo veiculo = new Veiculo("IXB-1234", "carro", "azul", 2020);
            Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(veiculo));
//            Mockito.when(repository.save(Mockito.any(Veiculo.class))).thenReturn(new Veiculo("IXB-1234", "carro", "azul", 2020, 1));
            Veiculo resultado = service.adicionarMulta(veiculo.getPlaca());
            assertEquals(1, resultado.getQtdMultas());
        }

        @Test
        @DisplayName("Quando veículo não for encontrado, deve lançar exceção")
        void adicionarMulta_veiculoNaoEncontrado() {
            assertThrows(VeiculoNaoEncontradoException.class, () -> service.excluir("IXB-1234"));
        }
    }

}