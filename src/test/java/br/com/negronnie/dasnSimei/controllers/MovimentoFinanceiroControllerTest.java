package br.com.negronnie.dasnSimei.controllers;

import br.com.negronnie.dasnSimei.dtos.MovimentoFinanceiroDTO;
import br.com.negronnie.dasnSimei.mappers.MovimentoFinanceiroMapper;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import br.com.negronnie.dasnSimei.services.MovimentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is; // necessário para o matcher is() usado dentro do jsonPath
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MovimentoFinanceiroController.class)
class MovimentoFinanceiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovimentoService service;

    @MockitoBean
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @MockitoBean
    private MovimentoFinanceiroMapper mapper;

    @Test
    @DisplayName("Upload do CSV | Deve retornar 200 Com mensagem de Sucesso")
    void upload() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "file",
                "nu_extrato.csv",
                "text/csv",
                "data,valor\n2025-01-01,2458.36".getBytes()
        );

        doNothing().when(service).processarArquivoCSV(any());

        ResultActions response = mockMvc.perform(
                multipart("/v1/movimentos/upload").file(arquivo)
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Arquivo Processado com Sucesso!"));
    }

    @Test
    @DisplayName("Retornar todos os Movimentos")
    void obterMovimentos() throws Exception {
        List<MovimentoFinanceiroDTO> movimentos = new ArrayList<>();
        movimentos.add(new MovimentoFinanceiroDTO(
                1L,
                LocalDate.of(2025, 1, 1),
                new BigDecimal("1025.33"),
                "Recebimento no PIX"
        ));
        movimentos.add(new MovimentoFinanceiroDTO(
                2L,
                LocalDate.of(2025, 1, 2),
                new BigDecimal("144.18"),
                "Recebimento no Cartão"
        ));

        when(service.listarMovimentos())
                .thenReturn(movimentos);

        ResultActions response = mockMvc.perform(get("/v1/movimentos"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(movimentos.size())));
    }

    @Test
    @DisplayName("Retornar Movimentos Contendo String na Descricao")
    void findByDescricaoContem() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Valor Total Anual")
    void obterTotalAnual() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Valor Total de Mês Selecionado")
    void obterTotalMensal() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Valor Total do Trimestre Selecionado")
    void obterTotalTrimestre() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Valor Total de Todos os Meses")
    void testObterTotalMensal() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Valor Total do Trimestre Selecionado")
    void testObterTotalTrimestre() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Valor Total da Categoria Informada")
    void obterTotalCategoria() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar o Movimento Selecionado pelo ID")
    void obterMovimento() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar Lista de Movimetnos Contidos na Página")
    void obterMovimentosPagina() {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Retornar Relatório Completo")
    void relatorioCompleto() {
        fail("Not yet implemented");
    }
}