package br.com.negronnie.dasnSimei.controller;

import br.com.negronnie.dasnSimei.model.entities.MovimentoFinanceiro;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/movimentos")
public class MovimentoFinanceiroController {

    @Autowired
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @PostMapping
    public MovimentoFinanceiro novoMovimento(@Valid MovimentoFinanceiro movimentoFinanceiro) {
        movimentoFinanceiroRepository.save(movimentoFinanceiro);
        return movimentoFinanceiro;
    }

    @GetMapping
    public Iterable<MovimentoFinanceiro> obterMovimentos() {
        return movimentoFinanceiroRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public Optional<MovimentoFinanceiro> obterMovimento(@PathVariable Long id){
        return movimentoFinanceiroRepository.findById(id); // .orElse(null) sem usar optional
    }

    @GetMapping(path = "/pagina/{numeroPagina}")
    public Iterable<MovimentoFinanceiro> obterMovimentosPagina(@PathVariable int numeroPagina){
        Pageable page = PageRequest.of(numeroPagina, 10);
        return movimentoFinanceiroRepository.findAll(page);
    }




}
