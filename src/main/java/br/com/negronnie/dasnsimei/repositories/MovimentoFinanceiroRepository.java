package br.com.negronnie.dasnSimei.repositories;

import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MovimentoFinanceiroRepository extends PagingAndSortingRepository<MovimentoFinanceiro, Long>, CrudRepository<MovimentoFinanceiro, Long> {
}