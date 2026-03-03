package br.com.negronnie.dasnsimei;

import br.com.negronnie.dasnsimei.entities.*;
import br.com.negronnie.dasnsimei.infra.DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class impostoDeRenda {
    static void main(String[] args) {

        Locale.setDefault(Locale.US);
        Scanner input = new Scanner(System.in);
        LinkedHashSet<ArquivosCSV> arquivos = new LinkedHashSet<>();

        System.out.print("Quantos arquivos (CSV) serão contabilizados? R: ");
        int quantidadeDeArquivos = input.nextInt();
        input.nextLine(); // Limpeza de buffer

        for (int i = 0; i < quantidadeDeArquivos; i++) {
            String caminho = input.nextLine();
            arquivos.add(new ArquivosCSV(caminho));
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DAO<Object> dao = new DAO<>();

        try {
            dao.startTransaction();
            for (ArquivosCSV arquivo : arquivos) {
                try (BufferedReader br = new BufferedReader(new FileReader(arquivo.getCaminho()))) {
                    String linhas = br.readLine();
                    File csv = new File(arquivo.getCaminho());
                    if (csv.getName().startsWith("nu")) {
                        linhas = br.readLine();
                        while (linhas != null) {
                            for (String linha : linhas.split("\n")) {
                                String[] campos = linha.split(",");
                                if (campos[1].charAt(0) != '-') {
                                    BigDecimal valor = new BigDecimal(campos[1]);
                                    dao.incluir(new Movimento(LocalDate.parse(campos[0], fmt), valor, campos[2], campos[3]));
                                }
                                linhas = br.readLine();
                            }
                        }
                    }
                    if (csv.getName().startsWith("si")) {
                        System.out.println("sicoob");
                        while (linhas != null) {
                            for (String linha : linhas.split("\n")) {
                                String[] campos = linha.split(",");
                                if (campos[1].charAt(0) != '-') {
                                    BigDecimal valor = new BigDecimal(campos[1]);
                                    dao.incluir(new Movimento(LocalDate.parse(campos[0], fmt), valor, campos[2], campos[3]));
                                }
                                linhas = br.readLine();
                            }
                        }
                    }
                    if (csv.getName().startsWith("pr")) {
                        while (linhas != null) {
                            for (String linha : linhas.split("\n")) {
                                String[] campos = linha.split(",");
                                BigDecimal valor = new BigDecimal(campos[0]);
                                System.out.println(valor + "pr");
                                dao.incluir(new Previsao(valor, campos[1]));
                                linhas = br.readLine();
                            }
                        }
                    }
                    if (csv.getName().startsWith("ve")) {
                        while (linhas != null) {
                            for (String linha : linhas.split("\n")) {
                                String[] campos = linha.split(",");
                                BigDecimal valor = new BigDecimal(campos[0]);
                                System.out.println(valor + "ve");
                                dao.incluir(new VendaExterna(valor, campos[1]));
                                linhas = br.readLine();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            dao.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.fechar();
        }

        System.out.println("Dados Cadastrados com Sucesso!");

        DAO<Movimento> daoL = new DAO<>(Movimento.class);
        List<Movimento> movimentos = daoL.listarTodos();
        BigDecimal soma = BigDecimal.ZERO;
        for (Movimento movimento : movimentos) {
            soma = soma.add(movimento.getValor());
        }
        daoL.fechar();

        DAO<Previsao> daoPr = new DAO<>(Previsao.class);
        List<Previsao> previsoes = daoPr.listarTodos();
        BigDecimal somaPrevisao = BigDecimal.ZERO;
        for (Previsao previsao : previsoes) {
            somaPrevisao = somaPrevisao.add(previsao.getValor());
        }
        daoPr.fechar();


        DAO<VendaExterna> daoVe = new DAO<>(VendaExterna.class);
        List<VendaExterna> vendas = daoVe.listarTodos();
        BigDecimal somaVendas = BigDecimal.ZERO;
        for (VendaExterna venda : vendas) {
            somaVendas = somaVendas.add(venda.getValor());
        }
        daoVe.fechar();

        BigDecimal
                jan = BigDecimal.ZERO,
                fev = BigDecimal.ZERO,
                mar = BigDecimal.ZERO,
                abr = BigDecimal.ZERO,
                mai = BigDecimal.ZERO,
                jun = BigDecimal.ZERO,
                jul = BigDecimal.ZERO,
                ago = BigDecimal.ZERO,
                set = BigDecimal.ZERO,
                out = BigDecimal.ZERO,
                nov = BigDecimal.ZERO,
                dez = BigDecimal.ZERO;

        for (MovimentoFinanceiro movimento : movimentos) {
            switch (movimento.getData().getMonthValue()) {
                case 1:
                    jan = jan.add(movimento.getValor());
                    break;
                case 2:
                    fev = fev.add(movimento.getValor());
                    break;
                case 3:
                    mar = mar.add(movimento.getValor());
                    break;
                case 4:
                    abr = abr.add(movimento.getValor());
                    break;
                case 5:
                    mai =mai.add(movimento.getValor());
                    break;
                case 6:
                    jun = jun.add(movimento.getValor());
                    break;
                case 7:
                    jul = jul.add(movimento.getValor());
                    break;
                case 8:
                    ago = ago.add(movimento.getValor());
                    break;
                case 9:
                    set = set.add(movimento.getValor());
                    break;
                case 10:
                    out = out.add(movimento.getValor());
                    break;
                case 11:
                    nov = nov.add(movimento.getValor());
                    break;
                case 12:
                    dez = dez.add(movimento.getValor());
                    break;
            }
        }

        System.out.println();
        System.out.printf("Faturamento (PJ): R$%,.2f%n", soma);
        System.out.printf("Valores em Aberto (PJ): R$%,.2f%n", somaPrevisao);
        System.out.println();
        System.out.printf("Faturamento Garantido (Executado + Previsto): R$%,.2f", soma.add(somaPrevisao));
        System.out.println();
        System.out.printf("Possível Faturamento proveniente de Vendas (PF): R$%,.2f", somaVendas);
        System.out.println();
        System.out.printf("Faturamento Total (PJ+PF): R$%,.2f", soma.add(somaPrevisao).add(somaVendas));
        System.out.println();
        System.out.println();
        System.out.print("Deseja visualizar o faturamento Mensal? (s/n) ");
        String resposta4 = input.next();
        if (resposta4.charAt(0) == 's' || resposta4.charAt(0) == 'S'){
            System.out.println();
            System.out.println("Faturamento Mensal:");
            System.out.println();
            System.out.println("Janeiro: " + String.format("%,.2f", jan));
            System.out.println("Fevereiro: " + String.format("%,.2f", fev));
            System.out.println("Março: " + String.format("%,.2f", mar));
            System.out.println("Abril: " + String.format("%,.2f", abr));
            System.out.println("Maio: " + String.format("%,.2f", mai));
            System.out.println("Junho: " + String.format("%,.2f", jun));
            System.out.println("Julho: " + String.format("%,.2f", jul));
            System.out.println("Agosto: " + String.format("%,.2f", ago));
            System.out.println("Setembro: " + String.format("%,.2f", set));
            System.out.println("Outubro: " + String.format("%,.2f", out));
            System.out.println("Novembro: " + String.format("%,.2f", nov));
            System.out.println("Dezembro: " + String.format("%,.2f", dez));
            System.out.println();
            System.out.println();
        }
        System.out.print("Deseja visualizar o faturamento Trimestral? (s/n) ");
        String resposta5 = input.next();
        if (resposta5.charAt(0) == 's' || resposta5.charAt(0) == 'S'){
            System.out.println();
            System.out.println();
            System.out.println("Faturamento Trimestral:");
            System.out.println();
            System.out.println("Primeiro: " + String.format("%,.2f", jan.add(fev).add(mar)));
            System.out.println("Segundo: " + String.format("%,.2f", abr.add(mai).add(jun)));
            System.out.println("Terceiro: " + String.format("%,.2f", jul.add(ago).add(set)));
            System.out.println("Quarto: " + String.format("%,.2f", out.add(nov).add(dez)));
            System.out.println();
            System.out.println();
        }
        System.out.print("Deseja visualizar a lista completa de movimentos? (s/n) ");
        String resposta = input.next();
        if (resposta.charAt(0) == 's' || resposta.charAt(0) == 'S'){
            System.out.println();
            System.out.println();
            System.out.println("Lista de Movimentos");
            movimentos.sort(Comparator.comparing(MovimentoFinanceiro::getData));
            for (MovimentoFinanceiro movimento : movimentos){
                System.out.println(movimento);
            }
            System.out.println();
        }

        System.out.print("Deseja visualizar a previsão de faturamento? (s/n) ");
        String resposta2 = input.next();
        if (resposta2.charAt(0) == 's' || resposta2.charAt(0) == 'S'){
            if (previsoes.isEmpty()) {
                System.out.println();
                System.out.println("Não há valores à receber");
                System.out.println();
            } else {
                System.out.println();
                System.out.println();
                System.out.printf("Pagamentos em Aberto (R$%,.2f)\n", somaPrevisao);
                previsoes.sort(Comparator.comparing(MovimentoFinanceiro::getValor).reversed());
                for (MovimentoFinanceiro movimento : previsoes){
                    System.out.println(movimento);
                }
                System.out.println();
            }
        }

        System.out.print("Deseja visualizar os itens à venda? (s/n) ");
        String resposta3 = input.next();

        if (resposta3.charAt(0) == 's' || resposta3.charAt(0) == 'S'){
            if (vendas.isEmpty()){
                System.out.println();
                System.out.println("Nenhum item à venda!");
                System.out.println();
            } else {
                System.out.println();
                System.out.printf("Itens à Venda (R$%,.2f)\n", somaVendas);
                vendas.sort(Comparator.comparing(MovimentoFinanceiro::getValor).reversed());
                for (MovimentoFinanceiro movimento : vendas){
                    System.out.println(movimento);
                }
            }
        }
        input.close();
    }
}
