package br.com.negronnie.dasnSimei.controllers;

import br.com.negronnie.dasnSimei.controllers.docs.MovimentoFinanceiroControllerDocs;
import br.com.negronnie.dasnSimei.dtos.MovimentoFinanceiroDTO;
import br.com.negronnie.dasnSimei.mappers.MovimentoFinanceiroMapper;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import br.com.negronnie.dasnSimei.services.MovimentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/movimentos")
@Tag(name = "Movimentos Financeiros", description = "Rotas para consulta de Movimentos Financeiros")
public class MovimentoFinanceiroController implements MovimentoFinanceiroControllerDocs {


    private final MovimentoFinanceiroRepository movimentoFinanceiroRepository;
    private final MovimentoService service;
    private final MovimentoFinanceiroMapper mapper;

    public MovimentoFinanceiroController(MovimentoFinanceiroRepository movimentoFinanceiroRepository,
                                         MovimentoService service,
                                         MovimentoFinanceiroMapper mapper) {
        this.movimentoFinanceiroRepository = movimentoFinanceiroRepository;
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile arquivo){
        if (arquivo == null || arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo não informado ou vazio.");
        }
        try {
            service.processarArquivoCSV(arquivo);
            return ResponseEntity.ok("Arquivo Processado com Sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar arquivo: " + e.getMessage());
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<List<MovimentoFinanceiroDTO>> obterMovimentos(){
        return ResponseEntity.status(HttpStatus.OK).body(service.listarMovimentos());
    }

    @GetMapping("/movimentos/{contem}")
    @Override
    public ResponseEntity<List<MovimentoFinanceiroDTO>> findByDescricaoContem(@PathVariable String contem){
        return ResponseEntity.status(HttpStatus.OK).body(service.listarMovimentosPorNomeContendo(contem));
    }

    @GetMapping("/totais/{ano}")
    @Override
    public ResponseEntity<BigDecimal> obterTotalAnual(@PathVariable int ano){
        return ResponseEntity.ok(service.totalAnual(ano));
    }

    @GetMapping("/totais/{ano}/{mes}")
    @Override
    public ResponseEntity<BigDecimal> obterTotalMensal(@PathVariable int ano, @PathVariable int mes){
        return ResponseEntity.ok(service.totalMensal(ano, mes));
    }

    @GetMapping("/totais/{ano}/Q{trimestre}")
    @Override
    public ResponseEntity<BigDecimal> obterTotalTrimestre(@PathVariable int ano, @PathVariable int trimestre){
        return ResponseEntity.ok(service.totalTrimestre(ano, trimestre));
    }

    @GetMapping("/totais/{ano}/")
    @Override
    public ResponseEntity<Map<String, BigDecimal>> obterTotalMensal(@PathVariable int ano){
        return ResponseEntity.ok(service.totalMeses(ano));
    }

    @GetMapping("/totais/{ano}/Q")
    @Override
    public ResponseEntity<Map<String, BigDecimal>> obterTotalTrimestre(@PathVariable int ano){
        return ResponseEntity.ok(service.totalTrimestres(ano));
    }

    @GetMapping("/totais/tipo/{categoria}")
    @Override
    public ResponseEntity<BigDecimal> obterTotalCategoria(@PathVariable String categoria){
        return ResponseEntity.ok(service.totalCategoria(categoria));
    }

    @GetMapping("/movimentos/{id}")
    @Override
    public ResponseEntity<MovimentoFinanceiroDTO> obterMovimento(@PathVariable Long id){
        return ResponseEntity.ok(service.obterMovimentoPorId(id));
    }

    @GetMapping("/pagina/{numeroPagina}")
    @Override
    public Page<MovimentoFinanceiroDTO> obterMovimentosPagina(@PathVariable int numeroPagina){
        Pageable page = PageRequest.of(numeroPagina, 10);
        return movimentoFinanceiroRepository.findAll(page).map(mapper::toDto);
    }
    
    @GetMapping("/relatorio/{ano}")
    @Override
    public ResponseEntity<Map<String, Object>> relatorioCompleto(@PathVariable int ano) {
        return ResponseEntity.ok(service.relatorioCompleto(ano));
    }
}
