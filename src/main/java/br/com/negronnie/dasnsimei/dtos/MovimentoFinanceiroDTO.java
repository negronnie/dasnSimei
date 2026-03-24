package br.com.negronnie.dasnSimei.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentoFinanceiroDTO(
        Long id,
        LocalDate data,
        BigDecimal valor,
        String descricao)
        implements Serializable {
}