package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Produto implements Serializable {

    public Produto() {
    }

    public Produto(Produto outroProduto) {
        this.nome = outroProduto.getNome();
        this.descricao = outroProduto.getDescricao();
        this.preco = outroProduto.getPreco();
        this.idUsuario = outroProduto.getIdUsuario();
        this.foto = outroProduto.getFoto();
        this.quantidade = outroProduto.getQuantidade();

        this.setKey(outroProduto.getKey());
    }

    public Produto(String descricao, String nome, double preco, int quantidade, String categoria) {
        this.descricao = descricao;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    private String key;
    private int avaliacao;
    private String comentarios;
    private int curtidas;
    private String descricao;
    private String foto;
    private String nome;
    private double preco;
    private int quantidade;
    private String categoria;
    private String idUsuario;
    private String tiposRecebimento;
    private String locaisEntrega;
    private double taxaEntrega;
    private double valorMinimoParaEntregaGratis;

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTiposRecebimento() {
        return tiposRecebimento;
    }

    public void setTiposRecebimento(String tiposRecebimento) {
        this.tiposRecebimento = tiposRecebimento;
    }

    public String getLocaisEntrega() {
        return locaisEntrega;
    }

    public void setLocaisEntrega(String locaisEntrega) {
        this.locaisEntrega = locaisEntrega;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(double taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public double getValorMinimoParaEntregaGratis() {
        return valorMinimoParaEntregaGratis;
    }

    public void setValorMinimoParaEntregaGratis(double valorMinimoParaEntregaGratis) {
        this.valorMinimoParaEntregaGratis = valorMinimoParaEntregaGratis;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "key='" + key + '\'' +
                ", avaliacao=" + avaliacao +
                ", comentarios='" + comentarios + '\'' +
                ", curtidas=" + curtidas +
                ", descricao='" + descricao + '\'' +
                ", foto='" + foto + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", categoria='" + categoria + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", tiposRecebimento='" + tiposRecebimento + '\'' +
                ", locaisEntrega='" + locaisEntrega + '\'' +
                ", taxaEntrega=" + taxaEntrega +
                ", valorMinimoParaEntregaGratis=" + valorMinimoParaEntregaGratis +
                '}';
    }

    public void salvarProduto(){
        DatabaseReference firebase = ConfiguracaoAuthFirebase.getFirebaseDatabase();
        firebase.child("produto")
                .child(this.idUsuario)
                .push()
                .setValue(this);
    }
}
