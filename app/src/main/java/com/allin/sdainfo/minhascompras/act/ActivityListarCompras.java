package com.allin.sdainfo.minhascompras.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.adapter.AdapterListaCompra;
import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.allin.sdainfo.minhascompras.helper.Mensagens;
import com.allin.sdainfo.minhascompras.helper.RecyclerItemClickListener;
import com.allin.sdainfo.minhascompras.model.Compras;
import com.allin.sdainfo.minhascompras.model.ItensCompras;
import com.allin.sdainfo.minhascompras.model.ItensListas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ActivityListarCompras extends AppCompatActivity {

    private Mensagens msg;

    private RecyclerView recyclerView;
    private AdapterListaCompra adapterCompra;

    private final List<Compras> listaDeCompras = new ArrayList<>();
    private Compras id_compra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_compras);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.lista_compra);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);

        iniciaComponentes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterCompra = new AdapterListaCompra(listaDeCompras, this);
        recyclerView.setAdapter(adapterCompra);

        listarCompras();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                irDetalhesCompra();
            }

            @Override
            public void onLongItemClick(View view, int position) {

                id_compra = listaDeCompras.get(position);

                alertaDialogExcluir();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        }
        ));


    }

    private void irDetalhesCompra() {
        Intent intent = new Intent(this, ActivityDetalhesCompra.class);
        intent.putExtra(Constantes.ID_COMPRA, id_compra);
        startActivity(intent);

    }

    private void iniciaComponentes() {

        Locale.setDefault(Locale.US); //forçar a usar o ponto ao inves da virgula para separar casas decimais, padrão americano

        msg = new Mensagens(getApplicationContext());

        recyclerView = findViewById(R.id.rv_lista_compras);

    }

    private void alertaDialogExcluir() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a apagar,\nA compra do dia : " + id_compra + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.apagar, (dialogInterface, i) -> {
                    deletarCompras(id_compra);
                    msg.msgLonga("apagando...\n" + id_compra + ", na base de dados.");
                })
                .create()
                .show();
    }

    public void deletarCompras(Compras id) {
        DatabaseReference tb_lista_compra = ConfigurarFirebase.getBanco().child(Constantes.DATA)
                .child(id.toString());
        tb_lista_compra.removeValue();
    }

    private void listarCompras() {
        Query tb_compras = ConfigurarFirebase.getBanco().child(Constantes.DATA);
        tb_compras.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaDeCompras.clear();
                for (DataSnapshot item : snapshot.getChildren()) {



                    listaDeCompras.add(id_compra);
                }
                Collections.reverse(listaDeCompras);
                adapterCompra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}