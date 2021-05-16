package com.allin.sdainfo.minhascompras.model;

import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compras implements Serializable {
    private String id;
    private List<ItensCompras> itemLista;

    public Compras() {
        //tabela do banco de dados
        DatabaseReference tabela_lista_compras = ConfigurarFirebase.getBanco().child(Constantes.DATA);
        //id autoincrement
        //setId(tabela_lista_compras.push().getKey()); // id da lista de compra
    }

    public void salvarCompras(String id) {
        DatabaseReference tabela_banco = ConfigurarFirebase.getBanco().child(Constantes.DATA)
                .child(getId());
        tabela_banco.setValue(this);
    }

    public void atualizarCompras() {
        DatabaseReference tabela = ConfigurarFirebase.getBanco().child(Constantes.DATA)
                .child(id);
        tabela.updateChildren(converterEmMap());
    }

    @Exclude
    public Map<java.lang.String, Object> converterEmMap() {
        HashMap<java.lang.String, Object> map_lista = new HashMap<>();
        map_lista.put(Constantes.ITEM_LISTA, getItemLista());
        map_lista.put(Constantes.ID, getId());

        return map_lista;
    }

    public void deletarCompras(String id) {
        DatabaseReference tb_lista_compra = ConfigurarFirebase.getBanco().child(Constantes.DATA)
                .child(id);
        tb_lista_compra.removeValue();
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItensCompras> getItemLista() {
        return itemLista;
    }

    public void setItemLista(List<ItensCompras> itemLista) {
        this.itemLista = itemLista;
    }

    @Override
    public String toString() {
        return id;
    }
}
