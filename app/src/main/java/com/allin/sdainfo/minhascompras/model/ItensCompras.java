package com.allin.sdainfo.minhascompras.model;

import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ItensCompras implements Serializable {

    private String id;
    private String produto;
    private double preco;
    private double qtde;
    private double total;


    public ItensCompras() {
        //tabela do banco de dados
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA);
        //id autoincrement
        setId(tb_itens.push().getKey());
    }

    public void salvarItens(String id_compra) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA)
                .child(id_compra)
                .child(getId());
        tb_itens.setValue(this);
    }

    public void salvarItensId(String id_compra, String id) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA)
                .child(id_compra)
                .child(id);
        tb_itens.setValue(this);
    }

    public void atualizarItens(String id_compra, String id) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA)
                .child(id_compra)
                .child(id);
        tb_itens.updateChildren(converterEmMap());
    }

    @Exclude
    public Map<String, Object> converterEmMap() {
        HashMap<String, Object> map_Itens = new HashMap<>();
        map_Itens.put(Constantes.PRODUTO, getProduto());
        map_Itens.put(Constantes.PRECO, getPreco());
        map_Itens.put(Constantes.QTDE, getQtde());
        map_Itens.put(Constantes.TOTAL, getTotal());
        map_Itens.put(Constantes.ID, getId());

        return map_Itens;
    }

    public void deletarItens(String id_compra, String id) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA)
                .child(id_compra)
                .child(id);
        tb_itens.removeValue();
    }

    public void deletarCompra(String id_compra) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA)
                .child(id_compra);
        tb_itens.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getQtde() {
        return qtde;
    }

    public void setQtde(double qtde) {
        this.qtde = qtde;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
