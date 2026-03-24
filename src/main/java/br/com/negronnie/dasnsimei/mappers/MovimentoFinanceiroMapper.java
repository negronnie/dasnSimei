package br.com.negronnie.dasnSimei.mappers;

import br.com.negronnie.dasnSimei.dtos.MovimentoFinanceiroDTO;
import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;

public class MovimentoFinanceiroMapper {

    public MovimentoFinanceiroDTO toDto(MovimentoFinanceiro entity) {
        if (entity == null) return null;

        return new MovimentoFinanceiroDTO(
                entity.getId(),
                entity.getData(),
                entity.getValor(),
                entity.getDescricao()
        );
    }
}