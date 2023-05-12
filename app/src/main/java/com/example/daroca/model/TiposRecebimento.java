package com.example.daroca.model;

public enum TiposRecebimento {
    PIX(1, "pix"),
    ESPECIE(2, "especie"),
    CARTÃO(3, "cartao");

    private final int valor;
    private final String descricao;

    TiposRecebimento(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }
}
