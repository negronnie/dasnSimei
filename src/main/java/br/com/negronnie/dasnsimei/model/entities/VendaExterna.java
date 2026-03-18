package br.com.negronnie.dasnSimei.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("VE")
public class VendaExterna extends MovimentoFinanceiro{
    public VendaExterna() {}

    public VendaExterna(BigDecimal valor, String descricao) {
        super(valor, descricao);
    }
}
