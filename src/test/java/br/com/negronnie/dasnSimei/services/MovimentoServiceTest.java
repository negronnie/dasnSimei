package br.com.negronnie.dasnSimei.services;

import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentoServiceTest {

    @Mock
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @InjectMocks
    private MovimentoService movimentoService;

    private static final BigDecimal TOTAL_ESPERADO = new BigDecimal("12500.75");

    @Test
    void totalAnual_deveRetornarSomaCorretaDoRepositorio() {
        when(movimentoFinanceiroRepository.obterTotalAnual(2025))
                .thenReturn(TOTAL_ESPERADO);
        BigDecimal resultado = movimentoService.totalAnual(2025);
        assertEquals(TOTAL_ESPERADO, resultado,
                () -> "O totalAnual deve retornar exatamente o valor fornecido pelo repositório");
        verify(movimentoFinanceiroRepository).obterTotalAnual(2025);
}

    @Test
    void totalAnual_quandoRepositorioRetornarNull_deveRepassarNull() {
        when(movimentoFinanceiroRepository.obterTotalAnual(1900))
                .thenReturn(null);
        BigDecimal resultado = movimentoService.totalAnual(1900);
        assertNull(resultado,
                () -> "Quando o repositório retornar null, o service deve repassar null sem alteração");
    }
}
