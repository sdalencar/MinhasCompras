package com.allin.sdainfo.minhascompras.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.adapter.AdapterCriarLista;
import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.allin.sdainfo.minhascompras.helper.Mensagens;
import com.allin.sdainfo.minhascompras.helper.RecyclerItemClickListener;
import com.allin.sdainfo.minhascompras.model.ItensListas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityCriarLista extends AppCompatActivity {

    private EditText eitem;
    private String sitem;
    private Mensagens msg;

    private RecyclerView recyclerView;
    private AdapterCriarLista adapterCriarLista;
    private final List<ItensListas> itemDaLista = new ArrayList<>();
    private ItensListas itens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_lista);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.voltar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);

        iniciaComponentes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterCriarLista = new AdapterCriarLista(itemDaLista, this);
        recyclerView.setAdapter(adapterCriarLista);


        listarItensParaCompras();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itens = itemDaLista.get(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                itens = itemDaLista.get(position);
                alertaDialogExcluir();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        }
        ));

    }

    @SuppressLint("NonConstantResourceId")
    public void onclickSelecao(View view) {
        switch (view.getId()) {
            case R.id.bt_ir_para_comprar:
                startActivity(new Intent(this, ActivityComprar.class));
                break;
            case R.id.bt_incluir_item_lista:
                verificaCampos();
                break;
        }
    }

    private void iniciaComponentes() {

        msg = new Mensagens(getApplicationContext());

        eitem = findViewById(R.id.et_criar_lista_compra_item);

        recyclerView = findViewById(R.id.rv_lista_compra_nome_produtos);

    }

    private void verificaCampos() {
        sitem = eitem.getText().toString();

            if (!sitem.isEmpty()) {
                addItem(sitem);
            } else {
                msg.msgCurta("digite o nome do produto.");
            }
    }

    private void addItem(String produto) {

        itens = new ItensListas();
        itens.setProduto(produto);

        itens.salvarItens(Constantes.TB_LISTA);

        msg.msgCurta(itens.getProduto() + ", adicionado a lista com sucesso.");
        eitem.setText("");
    }

    private void limparCampos() {
        eitem.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_user_1:
                if (!itens.getId().isEmpty()) {
                    alertaDialogEditar();
                }
                break;
            case R.id.menu_user_2:
                if (!itens.getId().isEmpty()) {
                    alertaDialogExcluir();
                }
                break;
            case R.id.menu_user_3:
                msg.msgLonga("voltando...");
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void alertaDialogEditar() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a mudar as informções de " + sitem + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.alterar, (dialogInterface, i) -> {

                    itens.setProduto(sitem);
                    itens.atualizarItens(Constantes.TB_LISTA, itens.getId());
                    msg.msgLonga("alterando...\n" + sitem + ", na base de dados.");
                    limparCampos();
                    itemDaLista.clear();
                })
                .create()
                .show();
    }

    private void alertaDialogExcluir() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a apagar, " + sitem + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.apagar, (dialogInterface, i) -> {
                    itens.deletarItens(Constantes.TB_LISTA, itens.getId());
                    msg.msgLonga("apagando...\n" + sitem + ", na base de dados.");
                    limparCampos();
                })
                .create()
                .show();
    }

    private void listarItensParaCompras() {
        //____ordernar
        Query tb_lista = ConfigurarFirebase.getBanco().child(Constantes.TB_LISTA).orderByChild(Constantes.PRODUTO);
        tb_lista.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemDaLista.clear();
                for (DataSnapshot itens : snapshot.getChildren()) {
                    ItensListas item = itens.getValue(ItensListas.class);
                    itemDaLista.add(item);
                }
                adapterCriarLista.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}