package br.com.negronnie.dasnSimei.controller;

import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import br.com.negronnie.dasnSimei.services.MovimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimentos")
public class MovimentoFinanceiroController {

    @Autowired
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @Autowired
    private MovimentoService service;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile arquivo){
        try {
            service.processarArquivoCSV(arquivo);
            return "OK";
        } catch (IOException e) {
            return "Erro: " + e.getMessage();
        }
    }

    @GetMapping
    public Iterable<MovimentoFinanceiro> obterMovimentos() {
        return movimentoFinanceiroRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<MovimentoFinanceiro> obterMovimento(@PathVariable Long id){
        return movimentoFinanceiroRepository.findById(id); // .orElse(null) sem usar optional
    }

    @GetMapping("/pagina/{numeroPagina}")
    public Iterable<MovimentoFinanceiro> obterMovimentosPagina(@PathVariable int numeroPagina){
        Pageable page = PageRequest.of(numeroPagina, 10);
        return movimentoFinanceiroRepository.findAll(page);
    }




}
