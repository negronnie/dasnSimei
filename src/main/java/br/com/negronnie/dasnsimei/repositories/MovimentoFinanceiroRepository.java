package br.com.negronnie.dasnSimei.repositories;

import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface MovimentoFinanceiroRepository extends JpaRepository<MovimentoFinanceiro, Long> {

    @Query("select sum(m.valor) from MovimentoFinanceiro m where year(m.data) = :ano")
    BigDecimal obterTotalAnual(@Param("ano") int ano);

    @Query("select sum(m.valor) from MovimentoFinanceiro m where month(m.data) = :mes and year(m.data) = :ano")
    BigDecimal obterTotalMensal(@Param("ano") int ano, @Param("mes") int mes);

    @Query("select sum(m.valor) from MovimentoFinanceiro m where year(m.data) = :ano and month(m.data) between :mesInicial and :mesFinal")
    BigDecimal obterTotalTrimestre(@Param("ano") int ano, @Param("mesInicial") int mesInicial, @Param("mesFinal") int mesFinal);

    @Query("select sum(m.valor) from MovimentoFinanceiro m where (:categoria = 'vendas' and type(m) = VendaExterna ) or (:categoria = 'previsao' and type(m) = Previsao )")
    BigDecimal obterTotalCategoria(@Param("categoria") String categoria);
}