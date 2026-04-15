package br.com.negronnie.dasnSimei.repositories;

import br.com.negronnie.dasnSimei.model.entities.Movimento;
import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import br.com.negronnie.dasnSimei.model.entities.VendaExterna;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovimentoFinanceiroRepositoryTest {

    @Autowired
    private MovimentoFinanceiroRepository repository;

    @Test
    @DisplayName("Persistir Movimento Financeiro")
    void testMovimentoFinanceiro_quandoForSalvo_devePersistirNoBanco() {
        Movimento mov = new Movimento(
                LocalDate.of(2025, 1, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");

        Movimento movimentoSalvo = repository.save(mov);
        assertNotNull(movimentoSalvo);
        assertTrue(movimentoSalvo.getId() > 0);
        assertEquals(movimentoSalvo.getDescricao(), mov.getDescricao());
        assertEquals(movimentoSalvo.getValor(), mov.getValor());
        assertEquals(movimentoSalvo.getData(), mov.getData());
    }

    @Test
    @DisplayName("Retornar Lista de Movimentos Financeiros")
    void testMovimentoFinanceiro_quandoConsultar_deveTrazerListaDeMovimentos() {
        Movimento mov1 = new Movimento(
                LocalDate.of(2025, 1, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");
        Movimento mov2 = new Movimento(
                LocalDate.of(2026, 9, 24),
                new BigDecimal("6894.12"),
                "soiergjdlergt",
                "Venda de produto");

        repository.save(mov1);
        repository.save(mov2);

        List<MovimentoFinanceiro> listaMovimentos = repository.findAll();

        assertNotNull(listaMovimentos);
        assertEquals(2, listaMovimentos.size());
    }

    @Test
    @DisplayName("Retornar Movimento por ID")
    void testMovimentoFinanceiro_quandoReceberId_deveRetornarMovimentoComID() {
        Movimento mov1 = new Movimento(
                LocalDate.of(2025, 1, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");

        repository.save(mov1);
        MovimentoFinanceiro movSalvo = repository.findById(mov1.getId()).get();

        assertNotNull(movSalvo);
        assertEquals(movSalvo.getId(), mov1.getId());
    }

    @Test
    @DisplayName("Retornar Valor Total do Ano Selecionado")
    void testObterTotalAnual() {
        Movimento mov1 = new Movimento(
                LocalDate.of(2025, 1, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");

        repository.save(mov1);
        BigDecimal totalAno = repository.obterTotalAnual(2025);

        assertNotNull(totalAno);
        assertEquals(totalAno, mov1.getValor());
    }

    @Test
    @DisplayName("Retornar Valor Total do Mês Selecionado")
    void testObterTotalMensal() {
        Movimento mov1 = new Movimento(
                LocalDate.of(2025, 2, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");

        repository.save(mov1);
        BigDecimal totalMes = repository.obterTotalMensal(2025,2);

        assertNotNull(totalMes);
        assertEquals(totalMes, mov1.getValor());
    }

    @Test
    @DisplayName("Retornar Valor Total do Trimestre Selecionado")
    void testObterTotalTrimestre() {
        Movimento mov1 = new Movimento(
                LocalDate.of(2025, 2, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");

        repository.save(mov1);
        BigDecimal totalTrimestre = repository.obterTotalTrimestre(2025,1,3);

        assertNotNull(totalTrimestre);
        assertEquals(totalTrimestre, mov1.getValor());
    }

    @Test
    @DisplayName("Retornar Valor Total da Categoria Selecionada")
    void testObterTotalCategoria() {
        VendaExterna mov1 = new VendaExterna(
                new BigDecimal("2157.03"),
                "Venda");

        repository.save(mov1);
        BigDecimal totalCategoria = repository.obterTotalCategoria("vendas");
        // Queria validar este teste usando o campo do banco pra avaliar o tipo conforme o discriminatorValue

        assertNotNull(totalCategoria);
        assertEquals(totalCategoria, mov1.getValor());
    }

    @Test
    @DisplayName("Retornar Movimento Financeiro Contendo Texto Parcialmente")
    void testFindMovimentoFinanceiroByDescricaoContainingIgnoreCase() {
        Movimento mov1 = new Movimento(
                LocalDate.of(2025, 1, 1),
                new BigDecimal("2157.03"),
                "Tasdoifjsdf",
                "Movimento financeiro");

        String statement = "Finan";
        repository.save(mov1);

        List <MovimentoFinanceiro> movSalvos = repository.findMovimentoFinanceiroByDescricaoContainingIgnoreCase(statement);

        assertNotNull(movSalvos);
        assertEquals(1, movSalvos.size());
        assertEquals(mov1.getDescricao().contains(statement), movSalvos.get(0).getDescricao().contains(statement));
    }
}