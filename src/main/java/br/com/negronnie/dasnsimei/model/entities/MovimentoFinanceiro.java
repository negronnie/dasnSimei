package br.com.negronnie.dasnSimei.model.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_movimento", discriminatorType = DiscriminatorType.STRING)
public abstract class MovimentoFinanceiro implements Comparable<MovimentoFinanceiro>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate data;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column
    private String identificadorTransacao;

    @Column
    private String descricao;

    public MovimentoFinanceiro(BigDecimal valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public MovimentoFinanceiro(LocalDate data, BigDecimal valor, String identificador, String descricao) {
        this.data = data;
        this.valor = valor;
        this.identificadorTransacao = identificador;
        this.descricao = descricao;
    }

    public MovimentoFinanceiro() {

    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getIdentificadorTransacao() {
        return identificadorTransacao;
    }

    public void setIdentificadorTransacao(String identificadorTransacao) {
        this.identificadorTransacao = identificadorTransacao;
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
        return Objects.equals(getData(), that.getData()) && Objects.equals(getIdentificadorTransacao(), that.getIdentificadorTransacao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getIdentificadorTransacao());
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
