package com.allin.sdainfo.minhascompras.model;

import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String data;

    public Usuario() {
        //tabela do banco de dados
        DatabaseReference tabela_usuario = ConfigurarFirebase.getBanco().child(Constantes.TB_USUARIO);
        //id autoincrement
        setId(tabela_usuario.push().getKey()); // email criado na criação do user/email
    }

    public void salvarUsuario() {
        DatabaseReference tabela_banco = ConfigurarFirebase.getBanco()
                .child(Constantes.TB_USUARIO)
                .child(getId());
        tabela_banco.setValue(this);
    }

    public void atualizarUsuario() {
        DatabaseReference tabela = ConfigurarFirebase.getBanco()
                .child(Constantes.TB_USUARIO).child(id);
        tabela.updateChildren(converterEmMap());
    }

    @Exclude
    public Map<String, Object> converterEmMap() {
        HashMap<String, Object> map_Usuario = new HashMap<>();
        map_Usuario.put(Constantes.NOME, getNome());
        map_Usuario.put(Constantes.EMAIL, getEmail());
        map_Usuario.put(Constantes.SENHA, getSenha());
        map_Usuario.put(Constantes.USER_ID, getId());

        return map_Usuario;
    }

    public void deletarUsuario(String id) {

        DatabaseReference tb_nome_usuer = ConfigurarFirebase.getBanco()
                .child(Constantes.TB_USUARIO)
                .child(id);
        tb_nome_usuer.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    @Exclude
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
