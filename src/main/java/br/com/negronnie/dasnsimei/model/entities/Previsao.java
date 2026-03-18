package br.com.negronnie.dasnSimei.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("PR")
public class Previsao extends MovimentoFinanceiro{
    public Previsao() {}

    public Previsao(BigDecimal valor, String descricao) {
        super(valor, descricao);
    }
}
