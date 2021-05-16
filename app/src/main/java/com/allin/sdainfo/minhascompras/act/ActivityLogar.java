package com.allin.sdainfo.minhascompras.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.allin.sdainfo.minhascompras.helper.Mensagens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ActivityLogar extends AppCompatActivity {
    private EditText email, esenha;
    private String semail, ssenha;
    private Mensagens msg;
    private FirebaseAuth auth;
    private DatabaseReference tb_id = ConfigurarFirebase.getBanco();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar);

        email = findViewById(R.id.et_logar_email);
        esenha = findViewById(R.id.et_logar_email);

        msg = new Mensagens(getApplicationContext());

        auth = ConfigurarFirebase.getAutenticacao();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, ActivityPrincipal.class));
        }

    }

    public void onclickSelecao(View view) {
        switch (view.getId()){
            case R.id.bt_logar_entrar:
                verificaCampos();
                break;
            case R.id.tv_logar_usuario:
                startActivity(new Intent(ActivityLogar.this, ActivityCriarUsuario.class));
                break;
        }


    }

    private void verificaCampos(){
        semail = email.getText().toString();
        ssenha = esenha.getText().toString();
        if (!semail.isEmpty()) {
            if (!ssenha.isEmpty()) {
                logar(semail, ssenha);
            } else {
                msg.msgCurta("digite a senha");
            }
        } else {
            msg.msgCurta("digite o email");
        }
    }

    private void logar(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            limparCampos();
                            startActivity(new Intent(ActivityLogar.this, ActivityPrincipal.class));
                        } else {
                            msg.msgLonga("Erro: " + task.getException());
                        }
                    }
                });
    }



    private void limparCampos() {
        email.setText("");
        esenha.setText("");
    }
}
