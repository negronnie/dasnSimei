package br.com.negronnie.dasnsimei.entities;

import java.util.Objects;

public class ArquivosCSV {

    protected String caminho;

    public ArquivosCSV(String caminho) {
        this.caminho = caminho;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArquivosCSV that = (ArquivosCSV) o;
        return Objects.equals(getCaminho(), that.getCaminho());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCaminho());
    }
}
