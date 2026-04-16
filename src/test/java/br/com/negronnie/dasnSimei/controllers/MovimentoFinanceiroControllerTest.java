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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is; // necessário para o matcher is() usado dentro do jsonPath
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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
    void findByDescricaoContem() throws Exception {
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
                "Recebimento no PIX"
        ));

        when(service.listarMovimentosPorNomeContendo(anyString()))
                .thenReturn(movimentos);

        ResultActions response = mockMvc.perform(get("/v1/movimentos/contem/{contem}", "PIX"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(movimentos.size())));
    }

    @Test
    @DisplayName("Retornar o Valor Total Anual")
    void obterTotalAnual() throws Exception {
        BigDecimal valorTotalAnual = new BigDecimal("1025.33");

        when(service.totalAnual(anyInt()))
                .thenReturn(valorTotalAnual);
        ResultActions response = mockMvc.perform(get("/v1/movimentos/totais/{ano}", 2025));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(valorTotalAnual.toString()));
    }

    @Test
    @DisplayName("Retornar o Valor Total do Mês Selecionado")
    void obterTotalMensal() throws Exception {
        BigDecimal valorTotalMensal = new BigDecimal("1025.33");

        when(service.totalMensal(anyInt(), anyInt()))
                .thenReturn(valorTotalMensal);
        ResultActions response = mockMvc.perform(get("/v1/movimentos/totais/{ano}/{mes}", 2025, 2));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(valorTotalMensal.toString()));
    }

    @Test
    @DisplayName("Retornar o Valor Total do Trimestre Selecionado")
    void obterTotalTrimestre() throws Exception {
        BigDecimal valorTotalTrimestre = new BigDecimal("1117.99");

        when(service.totalTrimestre(anyInt(), anyInt()))
                .thenReturn(valorTotalTrimestre);
        ResultActions response = mockMvc.perform(get("/v1/movimentos/totais/{ano}/Q{trimestre}", 2025, 1));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(valorTotalTrimestre.toString()));
    }

    @Test
    @DisplayName("Retornar o Valor Total de Todos os Meses")
    void testObterTotalMensal() throws Exception{
        Map<String, BigDecimal> mensais = new LinkedHashMap<>();

        mensais.put("Janeiro", new BigDecimal("1117.99"));
        mensais.put("Fevereiro", new BigDecimal("5438.12"));
        mensais.put("Março", new BigDecimal("2185.36"));
        mensais.put("Abril", new BigDecimal("9824.33"));
        mensais.put("Maio", new BigDecimal("1876.31"));


        when(service.totalMeses(anyInt()))
                .thenReturn(mensais);

        ResultActions response = mockMvc.perform(get("/v1/movimentos/totais/{ano}/", 2025));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$['Janeiro']").value(new BigDecimal("1117.99")))
                .andExpect(jsonPath("$['Fevereiro']").value(new BigDecimal("5438.12")))
                .andExpect(jsonPath("$['Março']").value(new BigDecimal("2185.36")))
                .andExpect(jsonPath("$['Abril']").value(new BigDecimal("9824.33")))
                .andExpect(jsonPath("$['Maio']").value(new BigDecimal("1876.31")));
    }

    @Test
    @DisplayName("Retornar o Valor Total de todos os Trimestres")
    void testObterTotalTrimestre() throws Exception {
        Map<String, BigDecimal> trimestres = new LinkedHashMap<>();
        trimestres.put("Trimestre 1", new BigDecimal("1117.99"));
        trimestres.put("Trimestre 2", new BigDecimal("5433.99"));
        trimestres.put("Trimestre 3", new BigDecimal("4342.99"));
        trimestres.put("Trimestre 4", new BigDecimal("1958.99"));

        when(service.totalTrimestres(anyInt()))
                .thenReturn(trimestres);

        ResultActions response = mockMvc.perform(get("/v1/movimentos/totais/{ano}/Q", 2025));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$['Trimestre 1']").value(new BigDecimal("1117.99")))
                .andExpect(jsonPath("$['Trimestre 2']").value(new BigDecimal("5433.99")))
                .andExpect(jsonPath("$['Trimestre 3']").value(new BigDecimal("4342.99")))
                .andExpect(jsonPath("$['Trimestre 4']").value(new BigDecimal("1958.99")));
    }

    @Test
    @DisplayName("Retornar o Valor Total da Categoria Informada")
    void obterTotalCategoria() throws Exception{
        BigDecimal valorTotalCategoria = new BigDecimal("1538.84");

        when(service.totalCategoria(anyString()))
                .thenReturn(valorTotalCategoria);
        ResultActions response = mockMvc.perform(get("/v1/movimentos/totais/tipo/{categoria}", "vendas"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(valorTotalCategoria.toString()));
    }

    @Test
    @DisplayName("Retornar o Movimento Selecionado pelo ID")
    void obterMovimento() throws Exception {
        long movimentoId = 1L;
        when(service.obterMovimentoPorId(movimentoId))
                .thenReturn(new MovimentoFinanceiroDTO(
                    1L,
                    LocalDate.of(2025, 1, 1),
                    new BigDecimal("1025.33"),
                    "Recebimento no PIX"
                ));

        ResultActions response = mockMvc.perform(get("/v1/movimentos/{id}", movimentoId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(movimentoId))
                .andExpect(jsonPath("$.valor").value(new BigDecimal("1025.33")))
                .andExpect(jsonPath("$.descricao").value("Recebimento no PIX"));
    }


//    @Test
//    @DisplayName("Retornar Lista de Movimentos Contidos na Página")
//    void obterMovimentosPagina() throws Exception{
//          // Teste não implementado, pois haverá mudança de responsabilidade da paginação
//          // da camada do controller para a camada do service.
//    }

    @Test
    @DisplayName("Retornar Relatório Completo")
    void relatorioCompleto() throws Exception {
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("Total", new BigDecimal("89284.17"));

        Map<String, BigDecimal> trimestrais = new LinkedHashMap<>();
        trimestrais.put("Trimestre 1", new BigDecimal("1117.99"));
        trimestrais.put("Trimestre 2", new BigDecimal("5433.99"));
        trimestrais.put("Trimestre 3", new BigDecimal("4342.99"));
        trimestrais.put("Trimestre 4", new BigDecimal("1958.99"));
        resultado.put("Trimestres", trimestrais);

        Map<String, BigDecimal> mensais = new LinkedHashMap<>();
        mensais.put("Janeiro", new BigDecimal("1958.99"));
        mensais.put("Fevereiro", new BigDecimal("4342.99"));
        mensais.put("Março", new BigDecimal("5433.99"));
        mensais.put("Abril", new BigDecimal("1117.99"));
        resultado.put("Mensais", mensais);

        resultado.put("Valor à Venda", new BigDecimal("1958.99"));
        resultado.put("Previsão de Faturamento", new BigDecimal("5433.99"));

        when(service.relatorioCompleto(anyInt()))
                .thenReturn(resultado);

        ResultActions response = mockMvc.perform(get("/v1/movimentos/relatorio/{ano}", 2025));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$['Trimestres']['Trimestre 1']").value(new BigDecimal("1117.99")))
                .andExpect(jsonPath("$['Trimestres']['Trimestre 2']").value(new BigDecimal("5433.99")))
                .andExpect(jsonPath("$['Trimestres']['Trimestre 3']").value(new BigDecimal("4342.99")))
                .andExpect(jsonPath("$['Trimestres']['Trimestre 4']").value(new BigDecimal("1958.99")))
                .andExpect(jsonPath("$['Mensais']['Janeiro']").value(new BigDecimal("1958.99")))
                .andExpect(jsonPath("$['Mensais']['Fevereiro']").value(new BigDecimal("4342.99")))
                .andExpect(jsonPath("$['Mensais']['Março']").value(new BigDecimal("5433.99")))
                .andExpect(jsonPath("$['Mensais']['Abril']").value(new BigDecimal("1117.99")))
                .andExpect(jsonPath("$['Total']").value(new BigDecimal("89284.17")))
                .andExpect(jsonPath("$['Valor à Venda']").value(new BigDecimal("1958.99")))
                .andExpect(jsonPath("$['Previsão de Faturamento']").value(new BigDecimal("5433.99")));
    }
}