package com.devinhouse.veiculosapi.controller;

import com.devinhouse.veiculosapi.dtos.VeiculoRequest;
import com.devinhouse.veiculosapi.exception.RegistroExistenteException;
import com.devinhouse.veiculosapi.model.Veiculo;
import com.devinhouse.veiculosapi.service.VeiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class VeiculosControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper = new ModelMapper();
    @MockBean
    private VeiculoService service;

    @Nested
    @DisplayName("Método: cadastrarVeiculo")
    class cadastrarVeiculo {
        @Test
        @DisplayName("Quando incluir veículo com placa não cadastrada, deve retornar o veiculo com sucesso")
        void cadastrarVeiculo() throws Exception {
            VeiculoRequest request = new VeiculoRequest("IXB-1234", "carro", "prata", 2016);
            Veiculo veiculo = modelMapper.map(request, Veiculo.class);
            String requestJson = objectMapper.writeValueAsString(request);
            Mockito.when(service.inserir(Mockito.any(Veiculo.class))).thenReturn(veiculo);

            mockMvc.perform(post("/api/veiculos")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.placa", is(notNullValue())))
                    .andExpect(jsonPath("$.placa", is(veiculo.getPlaca())));
        }

        @Test
        @DisplayName("Quando incluir veículo com placa já cadastrada, deve lançar erro")
        void cadastrarVeiculo_jaCadastrado() throws Exception {
            Mockito.when(service.inserir(Mockito.any(Veiculo.class))).thenThrow(RegistroExistenteException.class);
            VeiculoRequest request = new VeiculoRequest("IXB-1234", "carro", "prata", 2016);
            String requestJson = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/veiculos")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.erro", containsStringIgnoringCase("Registro já cadastrado! Placa: ")));
        }
    }

    @Nested
    @DisplayName("Método: consultarVeiculos")
    class consultarVeiculos {
        @Test
        @DisplayName("Quando tem veículos cadastrados, deve retornar lista com estes veículos")
        void consultarVeiculos() throws Exception {
            List<Veiculo> veiculos = List.of(
                    new Veiculo("IXB-1234", "carro", "prata", 2016),
                    new Veiculo("ABC-1234", "moto", "azul", 2016)
            );

            Mockito.when(service.listarVeiculos()).thenReturn(veiculos);
            mockMvc.perform(get("/api/veiculos").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @DisplayName("Quando não tem veiculos cadastrados, deve retornar lista vazia")
        void consultarVeiculos_vazio() throws Exception {
            mockMvc.perform(get("/api/veiculos").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }
}