package br.com.negronnie.dasnSimei.controllers;

import br.com.negronnie.dasnSimei.dtos.MovimentoFinanceiroDTO;
import br.com.negronnie.dasnSimei.mappers.MovimentoFinanceiroMapper;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import br.com.negronnie.dasnSimei.services.MovimentoService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/movimentos")
public class MovimentoFinanceiroController {

    @Autowired
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @Autowired
    private MovimentoService service;

    @Autowired
    private MovimentoFinanceiroMapper mapper;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    // sem wrapper http
//    @GetMapping
//    public Iterable<MovimentoFinanceiroDTO> obterMovimentos() {
//        return StreamSupport.stream(movimentoFinanceiroRepository.findAll().spliterator(), false)
//                .map(mapper::toDto)
//                .toList();
//    }


    // com wrapper http
    @GetMapping
    public ResponseEntity<List<MovimentoFinanceiroDTO>> obterMovimentos(){
        return ResponseEntity.status(HttpStatus.OK).body(service.listarMovimentos());
    }

    @GetMapping("/totais/{ano}")
    public ResponseEntity<BigDecimal> obterTotalAnual(@PathVariable int ano){
        return ResponseEntity.ok(service.totalAnual(ano));
    }

    @GetMapping("/totais/{ano}/{mes}")
    public ResponseEntity<BigDecimal> obterTotalMensal(@PathVariable int ano, @PathVariable int mes){
        return ResponseEntity.ok(service.totalMensal(ano, mes));
    }

    @GetMapping("/totais/{ano}/Q{trimestre}")
    public ResponseEntity<BigDecimal> obterTotalTrimestre(@PathVariable int ano, @PathVariable int trimestre){
        return ResponseEntity.ok(service.totalTrimestre(ano, trimestre));
    }

    @GetMapping("/totais/{ano}/")
    public ResponseEntity<Map<String, BigDecimal>> obterTotalMensal(@PathVariable int ano){
        return ResponseEntity.ok(service.totalMeses(ano));
    }

    @GetMapping("/relatorio/{ano}")
    public ResponseEntity<Map<String, Object>> relatorioCompleto(@PathVariable int ano) {
        return ResponseEntity.ok(service.relatorioCompleto(ano));
    }

    @GetMapping("/totais/{ano}/Q")
    public ResponseEntity<Map<String, BigDecimal>> obterTotalTrimestre(@PathVariable int ano){
        return ResponseEntity.ok(service.totalTrimestres(ano));
    }

    @GetMapping("/totais/tipo/{categoria}")
    public ResponseEntity<BigDecimal> obterTotalCategoria(@PathVariable String categoria){
        System.out.println("categoria no controller: " + categoria);
        return ResponseEntity.ok(service.totalCategoria(categoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentoFinanceiroDTO> obterMovimento(@PathVariable Long id){
        return service.obterMovimentoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pagina/{numeroPagina}")
    public Page<MovimentoFinanceiroDTO> obterMovimentosPagina(@PathVariable int numeroPagina){
        Pageable page = PageRequest.of(numeroPagina, 10);
        return movimentoFinanceiroRepository.findAll(page).map(mapper::toDto);
    }
}
