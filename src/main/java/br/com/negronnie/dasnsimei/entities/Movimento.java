package br.com.negronnie.dasnsimei.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("MV")
public class Movimento extends MovimentoFinanceiro{

    public Movimento() {}

    public Movimento(LocalDate data, BigDecimal valor, String identificador, String descricao) {
        super(data, valor, identificador, descricao);
    }
}
