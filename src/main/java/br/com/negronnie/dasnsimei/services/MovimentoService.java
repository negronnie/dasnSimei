package br.com.negronnie.dasnSimei.services;

import br.com.negronnie.dasnSimei.model.entities.Movimento;
import br.com.negronnie.dasnSimei.model.entities.Previsao;
import br.com.negronnie.dasnSimei.model.entities.VendaExterna;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MovimentoService {

    @Autowired
    private MovimentoFinanceiroRepository movimentoFinanceiroRepository;

    @Transactional
    public void processarArquivoCSV(MultipartFile arquivo) throws IOException {
        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            throw new IOException("Nome do arquivo não encontrado (originalFilename vazio).");
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;

            if(nomeArquivo.startsWith("nu")){
                br.readLine();
                while((linha = br.readLine()) != null){
                    String[] campos = linha.split(",");
                    if (campos.length < 4) continue;
                    if (!campos[1].isBlank() && campos[1].charAt(0) != '-') {
                        BigDecimal valor = new BigDecimal(campos[1]);
                        movimentoFinanceiroRepository.save(new Movimento(LocalDate.parse(campos[0], fmt), valor, campos[2], campos[3]));
                    }
                }
            }
            else if (nomeArquivo.startsWith("si")) {
                while ((linha = br.readLine()) != null) {
                    String[] campos = linha.split(",");
                    if (campos.length < 4) continue;
                    if (!campos[1].isBlank() && campos[1].charAt(0) != '-') {
                        BigDecimal valor = new BigDecimal(campos[1]);
                        movimentoFinanceiroRepository.save(new Movimento(LocalDate.parse(campos[0], fmt), valor, campos[2], campos[3]));
                    }
                }
            }
            else if(nomeArquivo.startsWith("pr")){
                while ((linha = br.readLine()) != null) {
                    String[] campos = linha.split(",");
                    if (campos.length < 2) continue;
                    BigDecimal valor = new BigDecimal(campos[0]);
                    movimentoFinanceiroRepository.save(new Previsao(valor, campos[1]));
                }
            }
            else if(nomeArquivo.startsWith("ve")){
                while ((linha = br.readLine()) != null) {
                    String[] campos = linha.split(",");
                    if (campos.length < 2) continue;
                    BigDecimal valor = new BigDecimal(campos[0]);
                    movimentoFinanceiroRepository.save(new VendaExterna(valor, campos[1]));
                }
            } else {
                throw new IOException("Tipo de arquivo não reconhecido. Esperado prefixo: nu/si/pr/ve.");
            }
        }
    }

    public BigDecimal totalAnual(int ano){
        return movimentoFinanceiroRepository.obterTotalAnual(ano);
    }

    public BigDecimal totalMensal(int ano, int mes){
        return movimentoFinanceiroRepository.obterTotalMensal(ano, mes);
    }

}
