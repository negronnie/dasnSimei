package br.com.negronnie.dasnSimei.services;

import br.com.negronnie.dasnSimei.model.entities.Movimento;
import br.com.negronnie.dasnSimei.model.entities.Previsao;
import br.com.negronnie.dasnSimei.model.entities.VendaExterna;
import br.com.negronnie.dasnSimei.repositories.MovimentoFinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void processarArquivoCSV(MultipartFile arquivo) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String nomeArquivo = arquivo.getOriginalFilename();
        String linha;

        if(nomeArquivo.startsWith("nu")){
            br.readLine();
            while((linha = br.readLine()) != null){
                String[] campos = linha.split(",");
                if (campos[1].charAt(0) != '-') {
                    BigDecimal valor = new BigDecimal(campos[1]);
                    movimentoFinanceiroRepository.save(new Movimento(LocalDate.parse(campos[0], fmt), valor, campos[2], campos[3]));
                }
            }
        }
        else if (nomeArquivo.startsWith("si")) {
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                if (campos[1].charAt(0) != '-') {
                    BigDecimal valor = new BigDecimal(campos[1]);
                    movimentoFinanceiroRepository.save(new Movimento(LocalDate.parse(campos[0], fmt), valor, campos[2], campos[3]));
                }
            }
        }
        else if(nomeArquivo.startsWith("pr")){
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                BigDecimal valor = new BigDecimal(campos[0]);
                movimentoFinanceiroRepository.save(new Previsao(valor, campos[1]));
            }
        }
        else if(nomeArquivo.startsWith("ve")){
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                BigDecimal valor = new BigDecimal(campos[0]);
                movimentoFinanceiroRepository.save(new VendaExterna(valor, campos[1]));
            }
        }
    }

}
