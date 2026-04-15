package br.com.negronnie.dasnSimei.repositories;

import br.com.negronnie.dasnSimei.model.entities.Movimento;
import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovimentoFinanceiroRepositoryTest {

    @Autowired
    private MovimentoFinanceiroRepository repository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
    @DisplayName("A")
    void testObterTotalAnual() {
        fail("Not yet implemented");
    }

    @Test
    void testObterTotalMensal() {
        fail("Not yet implemented");
    }

    @Test
    void testObterTotalTrimestre() {
        fail("Not yet implemented");
    }

    @Test
    void testObterTotalCategoria() {
        fail("Not yet implemented");
    }

    @Test
    void testFindMovimentoFinanceiroByDescricaoContainingIgnoreCase() {
        fail("Not yet implemented");
    }
}