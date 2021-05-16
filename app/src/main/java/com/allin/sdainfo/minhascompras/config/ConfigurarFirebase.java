package com.allin.sdainfo.minhascompras.config;


import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigurarFirebase {

    public static DatabaseReference referencia_banco_dados;
    public static FirebaseAuth referencia_autenticacao;
    public static FirebaseUser referencia_usuraio;
    public static StorageReference referencia_storage;

    public static DatabaseReference getBanco() {
        if (referencia_banco_dados == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            referencia_banco_dados = FirebaseDatabase.getInstance().getReference().child(Constantes.BANCO_DADOS);
        }
        return referencia_banco_dados;
    }

    public static FirebaseAuth getAutenticacao() {
        if (referencia_autenticacao == null) {
            referencia_autenticacao = FirebaseAuth.getInstance();
        }
        return referencia_autenticacao;
    }

    public static StorageReference getStorage() {
        if (referencia_storage == null) {
            referencia_storage = FirebaseStorage.getInstance().getReference().child(Constantes.BD_STORAGE);
        }
        return referencia_storage;
    }
    //retorno id_usuario
    public static FirebaseUser getIdUsuario(){
        if (referencia_usuraio == null) {
            referencia_usuraio = FirebaseAuth.getInstance().getCurrentUser();
        }
        return referencia_usuraio;
    }
}
