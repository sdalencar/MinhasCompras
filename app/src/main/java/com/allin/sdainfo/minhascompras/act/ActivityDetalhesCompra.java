package com.allin.sdainfo.minhascompras.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.adapter.AdapterDetalheCompra;
import com.allin.sdainfo.minhascompras.adapter.AdapterListaCompra;
import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.allin.sdainfo.minhascompras.helper.Mensagens;
import com.allin.sdainfo.minhascompras.helper.RecyclerItemClickListener;
import com.allin.sdainfo.minhascompras.model.Compras;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class ActivityDetalhesCompra extends AppCompatActivity {

    private Bundle id_compra;
    private RecyclerView recyclerView;
    private AdapterDetalheCompra adapterDetalheCompra;
    private Compras compras;
    private List<Compras> listaCompras;
    private Mensagens msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_compra);

        Intent it = getIntent();
        if(it != null) id_compra = it.getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.detalhes_compra);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);

        recyclerView = findViewById(R.id.rv_detalhes_compras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterDetalheCompra = new AdapterDetalheCompra(listaCompras, this);
        recyclerView.setAdapter(adapterDetalheCompra);

        msg = new Mensagens(getApplicationContext());

        listarDetalhesCompras();

    }
    private void listarDetalhesCompras() {
        Query tb_compras = ConfigurarFirebase.getBanco().child(Constantes.DATA).child(id_compra.toString());
        tb_compras.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Compras compra = snapshot.getValue(Compras.class);
                compras.setItemLista(compra.getItemLista());
                listaCompras.add(compras);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                msg.msgLonga("Erro : " + error.getMessage());
            }
        });
    }

}