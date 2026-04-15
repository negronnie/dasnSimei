package br.com.negronnie.dasnSimei.services;

import br.com.negronnie.dasnSimei.dtos.MovimentoFinanceiroDTO;
import br.com.negronnie.dasnSimei.exceptions.ArquivoInvalidoException;
import br.com.negronnie.dasnSimei.exceptions.ParametroInvalidoException;
import br.com.negronnie.dasnSimei.mappers.MovimentoFinanceiroMapper;
import br.com.negronnie.dasnSimei.model.entities.Movimento;
import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import br.com.negronnie.dasnSimei.exceptions.RecursoNaoEncontradoException;
import br.com.negronnie.dasnSimei.model.entities.Previsao;
import br.com.negronnie.dasnSimei.model.entities.VendaExterna;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovimentoServiceTest {

    @Mock
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @Mock
    private MovimentoFinanceiroMapper movimentoFinanceiroMapper;

    @InjectMocks
    private MovimentoService movimentoService;

    @Captor
    private ArgumentCaptor argumentCaptor;

    // Totalizadores
    private static final BigDecimal ANUAL_ESPERADO = new BigDecimal("12500.75");
    private static final BigDecimal MENSAL_ESPERADO = new BigDecimal("6415.12");
    private static final BigDecimal TRIMESTRAL_ESPERADO = new BigDecimal("6489.12");
    private static final BigDecimal VENDAS_ESPERADO = new BigDecimal("18427.03");
    private static final BigDecimal PREVISAO_ESPERADO = new BigDecimal("4286.10");

    // Objetos
    private static final MovimentoFinanceiroDTO DTO = new MovimentoFinanceiroDTO(
            null,
            LocalDate.of(2025,3,10),
            new BigDecimal("185.10"),
            "PIX Recebido de Santa Luzia"
    );

    private static final MovimentoFinanceiro ENTIDADE = new Movimento(
            LocalDate.of(2025,3,10),
            new BigDecimal("185.10"),
            "IUOWERTISDHFIOUWEHFIHSUDF",
            "PIX Recebido de Santa Luzia"
    );

    // Listagens
    private static final List<MovimentoFinanceiro> LISTA_ENTIDADE = List.of(ENTIDADE);
    private static final List<MovimentoFinanceiroDTO> LISTA_DTO = List.of(DTO);
    private static final List<MovimentoFinanceiro> LISTA_VAZIA = List.of();



    @Test
    @DisplayName("Retornar Total Anual")
    @Order(1)
    void totalAnual_deveRetornarSomaCorretaDoRepositorio() {
        when(movimentoFinanceiroRepository.obterTotalAnual(anyInt())).thenReturn(ANUAL_ESPERADO);
        BigDecimal resultado = movimentoService.totalAnual(2025);
        assertEquals(ANUAL_ESPERADO, resultado,
                "O totalAnual deve retornar exatamente o valor fornecido pelo repositório = " + ANUAL_ESPERADO);
        verify(movimentoFinanceiroRepository).obterTotalAnual(anyInt());
}

    @Test
    @DisplayName("Retornar Nulo ao Receber nulo do Banco (Anual)")
    void totalAnual_quandoRepositorioRetornarNull_deveRepassarNull() {
        when(movimentoFinanceiroRepository.obterTotalAnual(anyInt()))
                .thenReturn(null);
        BigDecimal resultado = movimentoService.totalAnual(1900);
        assertNull(resultado,
                "Quando o repositório retornar null, o service deve repassar null sem alteração");
        verify(movimentoFinanceiroRepository).obterTotalAnual(anyInt());
    }

    @Test
    @DisplayName("Retornar Total Mensal")
    @Order(2)
    void totalMensal_deveRetornarSomaCorretaDoRepositorio() {
        when(movimentoFinanceiroRepository.obterTotalMensal(anyInt(), anyInt())).thenReturn(MENSAL_ESPERADO);
        BigDecimal resultado = movimentoService.totalMensal(2025, 2);
        assertEquals(MENSAL_ESPERADO, resultado,
                "O totalMensal deve retornar o valor fornecido pelo repositório = " + MENSAL_ESPERADO);
        verify(movimentoFinanceiroRepository).obterTotalMensal(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Retornar Nulo ao Receber nulo do Banco (Mensal)")
    void totalMensal_quandoRepositorioRetornarNull_deveRepassarNull() {
        when(movimentoFinanceiroRepository.obterTotalMensal(anyInt(),anyInt()))
                .thenReturn(null);
        BigDecimal resultado = movimentoService.totalMensal(2025, 12);
        assertNull(resultado,
                "Quando o repositório retornar null, o service deve repassar null sem alteração");
        verify(movimentoFinanceiroRepository).obterTotalMensal(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Retornar Lista Contendo")
    @Order(5)
    void movimentoContendo_quandoRecebeParametro_deveRetornarListaDeMovimentosFiltrados() {
        when(movimentoFinanceiroRepository.findMovimentoFinanceiroByDescricaoContainingIgnoreCase(anyString()))
                .thenReturn(LISTA_ENTIDADE);

        when(movimentoFinanceiroMapper.toDto(ENTIDADE)).thenReturn(DTO);

        List<MovimentoFinanceiroDTO> resultado = movimentoService.listarMovimentosPorNomeContendo("pix");
        assertEquals(LISTA_DTO, resultado);
        verify(movimentoFinanceiroRepository).findMovimentoFinanceiroByDescricaoContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("Retornar Total do Trimestre Selecionado (O Primeiro do Ano)")
    @Order(3)
    void totalTrimestre_quandoRecebeParametro_DeveRetortnarOTotalDosMovimentosFiltrados() {
        when(movimentoFinanceiroRepository.obterTotalTrimestre(anyInt(),anyInt(), anyInt()))
                .thenReturn(TRIMESTRAL_ESPERADO);

        BigDecimal resultado = movimentoService.totalTrimestre(2025, 1);
        assertEquals(TRIMESTRAL_ESPERADO, resultado);
        verify(movimentoFinanceiroRepository).obterTotalTrimestre(anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Retornar Total do Quarto Trimestre")
    @Order(4)
    void totalTrimestre_quandoRecebeQuartoTrimestre_deveChamarRepositorioComMeses10a12() {
        when(movimentoFinanceiroRepository.obterTotalTrimestre(anyInt(), anyInt(), anyInt()))
                .thenReturn(TRIMESTRAL_ESPERADO);

        BigDecimal resultado = movimentoService.totalTrimestre(2025, 4);
        assertEquals(TRIMESTRAL_ESPERADO, resultado);
        verify(movimentoFinanceiroRepository).obterTotalTrimestre(anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Retornar Nulo ao receber nulo do Banco (Trimestral)")
    void totalTrimestre_quandoRecebeNulo_DeveRepassarNull() {
        when(movimentoFinanceiroRepository.obterTotalTrimestre(anyInt(), anyInt(), anyInt()))
                .thenReturn(null);

        BigDecimal resultado = movimentoService.totalTrimestre(2025, 4);
        assertNull(resultado,"Quando o repositório retornar null, o service deve repassar null sem alteração");
        verify(movimentoFinanceiroRepository).obterTotalTrimestre(anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Retornar DTO de Movimento Financeiro quando ID for válido.")
    @Order(6)
    void movimentosPorId_quandoRecebeUmIDValido_deveRetornarMovimentoFinanceiroDTO(){
        when(movimentoFinanceiroRepository.findById(anyLong()))
                .thenReturn(Optional.of(ENTIDADE));
        when(movimentoFinanceiroMapper.toDto(ENTIDADE))
                .thenReturn(DTO);

        MovimentoFinanceiroDTO resultado = movimentoService.obterMovimentoPorId(12L);
        assertEquals(DTO, resultado);
        verify(movimentoFinanceiroRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Lançar RecursoNaoEncontradoException quando o ID for inválido")
    void movimentosPorId_quandoRecebeUmIDInvalido_deveLancarRecursoNaoEncontradoException(){
        when(movimentoFinanceiroRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> movimentoService.obterMovimentoPorId(12L));
        verify(movimentoFinanceiroRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Retornar o Valor Total da Categoria Vendas")
    @Order(7)
    void totalCategoria_quandoRecebeVendas_deveRetornarTotalDaCategoriaVendas(){
        when(movimentoFinanceiroRepository.obterTotalCategoria("vendas"))
                .thenReturn(VENDAS_ESPERADO);

        BigDecimal resultado = movimentoService.totalCategoria("vendas");
        assertEquals(VENDAS_ESPERADO, resultado);
        verify(movimentoFinanceiroRepository).obterTotalCategoria("vendas");
    }

    @Test
    @DisplayName("Retornar o Valor Total da Categoria Previsao")
    @Order(8)
    void totalCategoria_quandoRecebePrevisao_deveRetornarTotalDaCategoriaPrevisao(){
        when(movimentoFinanceiroRepository.obterTotalCategoria("previsao"))
                .thenReturn(PREVISAO_ESPERADO);

        BigDecimal resultado = movimentoService.totalCategoria("previsao");
        assertEquals(PREVISAO_ESPERADO, resultado);
        verify(movimentoFinanceiroRepository).obterTotalCategoria("previsao");
    }

    @Test
    @DisplayName("Lançar ParametroInvalidoException ao receber Parâmetro Desconhecido na Busca por Categoria")
    void totalCategoria_quandoRecebeParametroDesconhecido_deveLancarParametroInvalidoException(){
        assertThrows(ParametroInvalidoException.class, () -> movimentoService.totalCategoria("joias"));
        verify(movimentoFinanceiroRepository, never()).obterTotalCategoria(any());
    }

    @Test
    @DisplayName("Listar Todos os Movimentos")
    @Order(9)
    void listarMovimentos_quandoRecebeValorValido_deveRetornarListaMovimentos(){
        when(movimentoFinanceiroRepository.findAll())
                .thenReturn(List.of(ENTIDADE));
       when(movimentoFinanceiroMapper.toDto(ENTIDADE))
               .thenReturn(DTO);
    List<MovimentoFinanceiroDTO> resultado = movimentoService.listarMovimentos();
    assertEquals(LISTA_DTO, resultado);
    verify(movimentoFinanceiroRepository).findAll();
    }

    @Test
    @DisplayName("Retornar Lista Vazia quando não há Movimentos")
    void listarMovimentos_quandoNaoHaMovimentos_deveRetornarListaVazia(){
        when(movimentoFinanceiroRepository.findAll())
                .thenReturn(LISTA_VAZIA);
        List<MovimentoFinanceiroDTO> resultado = movimentoService.listarMovimentos();
        assertEquals(LISTA_VAZIA, resultado);
    }

    @Test
    @DisplayName("Processar CSV 'nu': Pular cabeçalho e Salvar apenas valores Positivos")
    @Order(10)
    void processarArquivoCSV_comPrefixoNu_devePularCabecalhoESalvarApenasPositivos() throws IOException {
        String csv = "Data,Valor,Identificador,Descricao\n" +
                     "10/03/2025,185.10,TXN-001,PIX Recebido de Santa Luzia\n" +
                     "11/03/2025,-50.00,TXN-002,Pagamento\n";

        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename())
                .thenReturn("nu_extrato.csv");
        when(arquivo.getInputStream())
                .thenReturn(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        movimentoService.processarArquivoCSV(arquivo);

        ArgumentCaptor<Movimento> captor = ArgumentCaptor.forClass(Movimento.class);
        verify(movimentoFinanceiroRepository, times(1)).save(captor.capture());

        Movimento salvo = captor.getValue();
        assertEquals(LocalDate.of(2025, 3, 10), salvo.getData());
        assertEquals(new BigDecimal("185.10"), salvo.getValor());
        assertEquals("TXN-001", salvo.getIdentificadorTransacao());
        assertEquals("PIX Recebido de Santa Luzia", salvo.getDescricao());
    }

    @Test
    @DisplayName("Processar CSV 'si': Salvar apenas valores Positivos")
    @Order(11)
    void processarArquivoCSV_comPrefixoSi_deveSalvarApenasPositivos() throws IOException {
        String csv = "10/03/2025,185.10,TXN-001,PIX Recebido de Santa Luzia\n" +
                "11/03/2025,-50.00,TXN-002,Pagamento\n";

        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename())
                .thenReturn("si_extrato.csv");
        when(arquivo.getInputStream())
                .thenReturn(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        movimentoService.processarArquivoCSV(arquivo);

        ArgumentCaptor<Movimento> captor = ArgumentCaptor.forClass(Movimento.class);
        verify(movimentoFinanceiroRepository, times(1)).save(captor.capture());

        Movimento salvo = captor.getValue();
        assertEquals(LocalDate.of(2025, 3, 10), salvo.getData());
        assertEquals(new BigDecimal("185.10"), salvo.getValor());
        assertEquals("TXN-001", salvo.getIdentificadorTransacao());
        assertEquals("PIX Recebido de Santa Luzia", salvo.getDescricao());
    }

    @Test
    @DisplayName("Processar CSV 'pr': Salvar todas as linhas como Previsao")
    @Order(12)
    void processarArquivoCSV_comPrefixoPr_deveSalvarTodasAsLinhasComoPrevisao() throws IOException {
        String csv = "185.10,Previsao de Faturamento\n" +
                     "320.00,Previsao de Venda\n";

        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename())
                .thenReturn("previsao.csv");
        when(arquivo.getInputStream())
                .thenReturn(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        movimentoService.processarArquivoCSV(arquivo);

        ArgumentCaptor<Previsao> captor = ArgumentCaptor.forClass(Previsao.class);
        verify(movimentoFinanceiroRepository, times(2)).save(captor.capture());

        Previsao primeira = captor.getAllValues().get(0);
        assertEquals(new BigDecimal("185.10"), primeira.getValor());
        assertEquals("Previsao de Faturamento", primeira.getDescricao());
    }

    @Test
    @DisplayName("Processar CSV 've': deve salvar todas as linhas como Venda")
    @Order(13)
    void processarArquivoCSV_comPrefixoVe_deveSalvarTodasAsLinhasComoVenda() throws IOException {
        String csv = "185.10,Teclado Casio 61 teclas\n" +
                    "320.00,Guitarra SX Les Paul\n";

        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename())
                .thenReturn("venda.csv");
        when(arquivo.getInputStream())
                .thenReturn(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        movimentoService.processarArquivoCSV(arquivo);

        ArgumentCaptor<VendaExterna> captor = ArgumentCaptor.forClass(VendaExterna.class);
        verify(movimentoFinanceiroRepository, times(2)).save(captor.capture());

        VendaExterna primeira = captor.getAllValues().get(0);
        assertEquals(new BigDecimal("185.10"), primeira.getValor());
        assertEquals("Teclado Casio 61 teclas", primeira.getDescricao());
    }

    @Test
    @DisplayName("Processar CSV com prefixo inválido: deve lançar ArquivoInvalidoException")
    void processarArquivoCSV_comPrefixoInvalido_deveLancarIOException() throws IOException {
        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename()).thenReturn("extrato.csv");
        when(arquivo.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        assertThrows(ArquivoInvalidoException.class, () -> movimentoService.processarArquivoCSV(arquivo));
    }

    @Test
    @DisplayName("Processar CSV 'nu' com apenas valores negativos: Não deve salvar nada")
    void processarArquivoCSV_comApenasValoresNegativos_naoDeveSalvarNada() throws IOException {
        String csv = "Data,Valor,Identificador,Descricao\n" +
                     "10/03/2025,-185.10,TXN-001,Pagamento\n" +
                     "11/03/2025,-50.00,TXN-002,Outra Saida\n";

        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename()).thenReturn("nu_extrato.csv");
        when(arquivo.getInputStream()).thenReturn(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        movimentoService.processarArquivoCSV(arquivo);

        verify(movimentoFinanceiroRepository, never()).save(any());
    }
}