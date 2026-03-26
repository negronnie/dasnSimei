package br.com.negronnie.dasnSimei.model.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

//@Entity
public class ArquivosCSV {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(nullable = false)
    private String caminho;

//    @Column(nullable = false)
    private LocalDateTime dataImportacao;

    public ArquivosCSV(String caminho) {
        this.caminho = caminho;
    }

    public ArquivosCSV() {

    }

    public int getId() {
        return id;
    }

    public String getCaminho() {
        return caminho;
    }


    public LocalDateTime getDataImportacao() {
        return dataImportacao;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArquivosCSV that = (ArquivosCSV) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
