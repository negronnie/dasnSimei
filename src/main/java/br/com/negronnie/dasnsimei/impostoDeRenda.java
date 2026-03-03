package br.com.negronnie.dasnsimei;

import br.com.negronnie.dasnsimei.entities.ArquivosCSV;
import br.com.negronnie.dasnsimei.entities.MovimentoFinanceiro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        List<MovimentoFinanceiro> movimentos = new ArrayList<>();
        List<MovimentoFinanceiro> previsao = new ArrayList<>();
        List<MovimentoFinanceiro> vendas = new ArrayList<>();

        for (ArquivosCSV arquivo : arquivos) {
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo.getCaminho()))) {
                String linhas = br.readLine();
                File csv = new File(arquivo.getCaminho());
                if (csv.getName().startsWith("nu")) {
                    linhas = br.readLine();
                    while(linhas != null){
                        for (String linha : linhas.split("\n")) {
                            String[] campos = linha.split(",");
                            if (campos[1].charAt(0) != '-') {
                                movimentos.add(new MovimentoFinanceiro(LocalDate.parse(campos[0], fmt), Double.parseDouble(campos[1]), campos[2], campos[3]));
                            }
                            linhas = br.readLine();
                        }
                    }
                }
                if (csv.getName().startsWith("si")) {
                    while(linhas != null){
                        for (String linha : linhas.split("\n")) {
                            String[] campos = linha.split(",");
                            if (campos[1].charAt(0) != '-') {
                                movimentos.add(new MovimentoFinanceiro(LocalDate.parse(campos[0], fmt), Double.parseDouble(campos[1]), campos[2], campos[3]));
                            }
                            linhas = br.readLine();
                        }
                    }
                }
                if (csv.getName().startsWith("pr")){
                    while(linhas != null){
                        for (String linha : linhas.split("\n")) {
                            String[] campos = linha.split(",");
                            previsao.add(new MovimentoFinanceiro(Double.parseDouble(campos[0]), campos[1]));
                            linhas = br.readLine();
                        }
                    }
                }
                if (csv.getName().startsWith("ve")){
                    while(linhas != null){
                        for (String linha : linhas.split("\n")) {
                            String[] campos = linha.split(",");
                            vendas.add(new MovimentoFinanceiro(Double.parseDouble(campos[0]), campos[1]));
                            linhas = br.readLine();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Double soma = 0.0;
        Double somaPrevisao = 0.0;
        Double somaVendas = 0.0;
        for (MovimentoFinanceiro movimento : movimentos) {
            soma = movimento.getValor() + soma;
        }
        for (MovimentoFinanceiro movimento : previsao) {
            somaPrevisao = movimento.getValor() + somaPrevisao;
        }
        for (MovimentoFinanceiro movimento : vendas) {
            somaVendas += movimento.getValor();
        }

        // Valor total por Mes
//            Double somaMensal = 0.0;
//            for (MovimentoFinanceiro movimento : movimentos) {
//                for (int i = 1; i <= 12 ; i++) {
//                    if (movimento.getData().getMonthValue() == i) {
//                        somaMensal = movimento.getValor() + somaMensal;
//                        System.out.println(movimento.getData().getMonth() + " " + String.format("%,.2f", somaMensal));
//                    }
//                }
//            }

        Double jan = 0.0, fev = 0.0, mar = 0.0, abr = 0.0, mai = 0.0, jun = 0.0, jul = 0.0, ago = 0.0, set = 0.0, out = 0.0, nov = 0.0, dez = 0.0;

        for (MovimentoFinanceiro movimento : movimentos) {
            switch (movimento.getData().getMonthValue()) {
                case 1:
                    jan += movimento.getValor();
                    break;
                case 2:
                    fev += movimento.getValor();
                    break;
                case 3:
                    mar += movimento.getValor();
                    break;
                case 4:
                    abr += movimento.getValor();
                    break;
                case 5:
                    mai += movimento.getValor();
                    break;
                case 6:
                    jun += movimento.getValor();
                    break;
                case 7:
                    jul += movimento.getValor();
                    break;
                case 8:
                    ago += movimento.getValor();
                    break;
                case 9:
                    set += movimento.getValor();
                    break;
                case 10:
                    out += movimento.getValor();
                    break;
                case 11:
                    nov += movimento.getValor();
                    break;
                case 12:
                    dez += movimento.getValor();
                    break;
            }
        }

        System.out.println();
        System.out.printf("Faturamento (PJ): R$%,.2f%n", soma);
        System.out.printf("Valores em Aberto (PJ): R$%,.2f%n", somaPrevisao);
        System.out.println();
        System.out.printf("Faturamento Garantido (Executado + Previsto): R$%,.2f", soma+somaPrevisao);
        System.out.println();
        System.out.printf("Possível Faturamento proveniente de Vendas (PF): R$%,.2f", somaVendas);
        System.out.println();
        System.out.printf("Faturamento Total (PJ+PF): R$%,.2f", soma + somaPrevisao + somaVendas);
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
            System.out.println("Primeiro: " + String.format("%,.2f", jan + fev + mar));
            System.out.println("Segundo: " + String.format("%,.2f", abr + mai + jun));
            System.out.println("Terceiro: " + String.format("%,.2f", jul + ago + set));
            System.out.println("Quarto: " + String.format("%,.2f", out + nov + dez));
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
            if (previsao.isEmpty()) {
                System.out.println();
                System.out.println("Não há valores à receber");
                System.out.println();
            } else {
                System.out.println();
                System.out.println();
                System.out.printf("Pagamentos em Aberto (R$%,.2f)\n", somaPrevisao);
                previsao.sort(Comparator.comparing(MovimentoFinanceiro::getValor).reversed());
                for (MovimentoFinanceiro movimento : previsao){
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
