package com.devinhouse.veiculosapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {
    @Id
    private String placa;
    private String tipo;
    private String cor;
    private Integer qtdMultas;
    private Integer anoDeFabricacao;


}
