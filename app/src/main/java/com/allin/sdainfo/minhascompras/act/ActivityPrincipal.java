package com.allin.sdainfo.minhascompras.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.helper.Mensagens;


public class ActivityPrincipal extends AppCompatActivity {
    private Mensagens msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        msg = new Mensagens(getApplicationContext());
    }

    public void onclickSelecao(View view) {
        switch (view.getId()) {
            case R.id.bt_ir_para_comprar:
                msg.msgLonga("Ir para as compras");
                startActivity(new Intent(this, ActivityComprar.class));
                break;
            case R.id.bt_incluir_item_lista:
                msg.msgLonga("Criar lista de compras");
                startActivity(new Intent(this, ActivityCriarLista.class));
                break;
            case R.id.bt_ir_para_user:
                msg.msgLonga("Criar usuário");
                startActivity(new Intent(this, ActivityCriarUsuario.class));
                break;
        }
    }
}