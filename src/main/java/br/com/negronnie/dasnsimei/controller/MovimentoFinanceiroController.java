package br.com.negronnie.dasnSimei.controller;

import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
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
import java.util.Map;

@RestController
@RequestMapping("/api/movimentos")
public class MovimentoFinanceiroController {

    @Autowired
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @Autowired
    private MovimentoService service;

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

    @GetMapping
    public Iterable<MovimentoFinanceiro> obterMovimentos() {
        return movimentoFinanceiroRepository.findAll();
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

    @GetMapping("/totais/{ano}/Q")
    public ResponseEntity<Map<String, BigDecimal>> obterTotalTrimestre(@PathVariable int ano){
        return ResponseEntity.ok(service.totalTrimestres(ano));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovimentoFinanceiro> obterMovimento(@PathVariable Long id){
        return movimentoFinanceiroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pagina/{numeroPagina}")
    public Page<MovimentoFinanceiro> obterMovimentosPagina(@PathVariable int numeroPagina){
        Pageable page = PageRequest.of(numeroPagina, 10);
        return movimentoFinanceiroRepository.findAll(page);
    }
}
