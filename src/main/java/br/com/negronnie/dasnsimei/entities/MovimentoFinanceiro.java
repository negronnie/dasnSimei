package br.com.negronnie.dasnsimei.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MovimentoFinanceiro implements Comparable<MovimentoFinanceiro>{

    LocalDate data;
    Double valor;
    String identificador;
    String descricao;

    public MovimentoFinanceiro(Double valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public MovimentoFinanceiro(LocalDate data, Double valor, String identificador, String descricao) {
        this.data = data;
        this.valor = valor;
        this.identificador = identificador;
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovimentoFinanceiro that = (MovimentoFinanceiro) o;
        return Objects.equals(getData(), that.getData()) && Objects.equals(getIdentificador(), that.getIdentificador());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getIdentificador());
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        if (getData() != null){
            builder.append(getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .append(" - R$")
                    .append(String.format("%,.2f", getValor()))
                    .append(" (")
                    .append(getDescricao())
                    .append(")");
        } else {
            builder.append("R$")
                    .append(String.format("%,.2f", getValor()))
                    .append(" (")
                    .append(getDescricao())
                    .append(")");
        }
        return builder.toString();

    }


    @Override
    public int compareTo(MovimentoFinanceiro outroMovimento) {

        if (getData() != null) {
            return getData().compareTo(outroMovimento.getData());
        } else {
            return getDescricao().compareToIgnoreCase(outroMovimento.getDescricao());
        }
    }
}
