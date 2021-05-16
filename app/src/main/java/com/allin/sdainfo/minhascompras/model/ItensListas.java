package com.allin.sdainfo.minhascompras.model;

import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ItensListas implements Serializable {

    private String id;
    private String produto;


    public ItensListas() {
        //tabela do banco de dados
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco();
        //id autoincrement
        setId(tb_itens.push().getKey());
    }

    public void salvarItens(String id_compra) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco()
                .child(id_compra)
                .child(getId());
        tb_itens.setValue(this);
    }

    public void atualizarItens(String id_compra, String id) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco()
                .child(id_compra)
                .child(id);
        tb_itens.updateChildren(converterEmMap());
    }

    @Exclude
    public Map<String, Object> converterEmMap() {
        HashMap<String, Object> map_Itens = new HashMap<>();
        map_Itens.put(Constantes.PRODUTO, getProduto());
        map_Itens.put(Constantes.ID, getId());

        return map_Itens;
    }

    public void deletarItens(String id_compra, String id) {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco()
                .child(id_compra)
                .child(id);
        tb_itens.removeValue();
    }
    @Exclude
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

    @Override
    public String toString() {
        return produto;
    }
}
